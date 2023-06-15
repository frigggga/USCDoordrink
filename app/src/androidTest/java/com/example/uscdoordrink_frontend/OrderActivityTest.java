package com.example.uscdoordrink_frontend;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.times;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.entity.Order;
import com.example.uscdoordrink_frontend.entity.Request;
import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.entity.UserType;
import com.example.uscdoordrink_frontend.service.CallBack.OnFailureCallBack;
import com.example.uscdoordrink_frontend.service.CallBack.OnSuccessCallBack;
import com.example.uscdoordrink_frontend.service.OrderService;
import com.example.uscdoordrink_frontend.service.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Instant;
import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class OrderActivityTest {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "OrderActivityTest";
    private double subtotal = 21.0;
    private double discounts = 3.0;
    private double tax = subtotal * 0.1;
    private double deliverFee = 1.99;
    private double total = subtotal - discounts + tax + deliverFee;
    boolean addedRequest = false;
    private Request r = null;
    private String start_time = Instant.now().toString();

    static Intent intent;

    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), OrderActivity.class);
        double subtotal = 21.0;
        double discounts = 3.0;
        intent.putExtra("subtotal", subtotal);
        intent.putExtra("discounts", discounts);
    }

    @Rule
    public ActivityScenarioRule<OrderActivity> orderActivityActivityScenarioRule =
            new ActivityScenarioRule<OrderActivity>(intent);



    @Before
    public void setUp() throws Exception {
        Intents.init();
        DocumentReference docRef = db.collection("User").document("hehe");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, "success");
                Constants.currentUser = documentSnapshot.toObject(User.class);
                ArrayList<Order> orders = new ArrayList<>();
                orders.add(new Order("lemonade", "7PqA0Yca8mKTntrvIHPT", 1, 2.0, 0.0));
                Constants.currentUser.setCurrentOrder(orders);
            }
        });

    }

    @Test
    public void onOrderActivityDisplay(){
        onView(allOf(withId(R.id.pricesub), withText(String.valueOf(subtotal))));
        onView(allOf(withId(R.id.pricedc), withText(String.valueOf(discounts))));
        onView(allOf(withId(R.id.pricetax), withText(String.valueOf(tax))));
        onView(allOf(withId(R.id.priceSt), withText(String.valueOf(deliverFee))));
        onView(allOf(withId(R.id.priceSt), withText(String.valueOf(total))));
    }

    @Test
    public void orderWithinLimit() throws InterruptedException {
        Thread.sleep(3000);
        onView(withId(R.id.place_order)).perform(click());
        Thread.sleep(1000);
        onView(withText("One more step!"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withId(R.id.edittext_address)).perform(typeText("325 W Adams St, Los Angeles, 90007"));
        r = new Request(start_time, Constants.currentUser.getUserName(), Constants.currentUser.getContactInformation(),
                "325 W Adams St, Los Angeles, 90007", "7PqA0Yca8mKTntrvIHPT", total,
                Constants.currentUser.getCurrentOrder());
        onView(withId(android.R.id.button1)).perform(click());
        Thread.sleep(1000);
        Intents.intended(hasComponent(ViewOrderActivity.class.getName()));
        addedRequest = true;
    }

    @Test
    public void orderBeyondLimit() throws InterruptedException{
        Thread.sleep(3000);
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order("lemonade", "7PqA0Yca8mKTntrvIHPT", 1, 2.0, 0.0));
        orders.add(new Order("lemonade", "7PqA0Yca8mKTntrvIHPT", 1, 2.0, 0.0));
        orders.add(new Order("lemonade", "7PqA0Yca8mKTntrvIHPT", 1, 2.0, 0.0));
        orders.add(new Order("lemonade", "7PqA0Yca8mKTntrvIHPT", 1, 2.0, 0.0));
        orders.add(new Order("lemonade", "7PqA0Yca8mKTntrvIHPT", 1, 2.0, 0.0));
        Constants.currentUser.setCurrentOrder(orders);

        onView(withId(R.id.place_order)).perform(click());
        Thread.sleep(1000);
        onView(withText("Attention!"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withId(android.R.id.button2)).perform(click());
        Thread.sleep(1000);
        Intents.intended(hasComponent(MapsActivity.class.getName()));


    }


    @After
    public void tearDown() throws Exception {
        Constants.currentUser = null;
        Constants.currentRequest = null;
        Intents.release();
        if(addedRequest){
            //get request start time
            db.collection("Request").document(r.getName()).collection("Orders")
                    .whereEqualTo("total", 22.09)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Request toDelete = document.toObject(Request.class);

                                    //delete request
                                    UserService userService = new UserService();
                                    OrderService orderService = new OrderService();
                                    orderService.deleteRequest(toDelete,new OnSuccessCallBack<Void>() {
                                        @Override
                                        public void onSuccess(Void input) {
                                            Log.d(TAG, "delete request successful");
                                            userService.changeUserRequest("hehe", new ArrayList<Request>());
                                        }
                                    }, new OnFailureCallBack<Exception>() {
                                        @Override
                                        public void onFailure(Exception input) {
                                            Log.w(TAG, "failed to delete request");
                                        }
                                    });
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
            Thread.sleep(1000);
        }
    }

}