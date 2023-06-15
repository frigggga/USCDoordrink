package com.example.uscdoordrink_frontend;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.entity.Order;
import com.example.uscdoordrink_frontend.entity.Request;
import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.service.OrderService;
import com.example.uscdoordrink_frontend.service.UserService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderDetailActivityTest {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "OrderDetailActivityTest";

    private int position = 0;
    private User seller = null;
    private User customer = null;

    static Intent intent;

    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), OrderDetailActivity.class);
        int position = 0;
        intent.putExtra("position", position);
    }

    @Before
    public void setUp() throws Exception {
        DocumentReference customRef = db.collection("User").document("friggga");
        customRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, "success");
                customer = documentSnapshot.toObject(User.class);
            }
        });

        DocumentReference sellerRef = db.collection("User").document("sellerTest");
        sellerRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, "success");
                seller = documentSnapshot.toObject(User.class);
            }
        });
        Thread.sleep(3000);
        Constants.currentUser = customer;
        Constants.currentRequest = customer.getOrderHistory().get(position);
        ActivityScenario.launch(intent);
        Intents.init();

    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
        UserService userService = new UserService();
        List<Request> customerRequest = customer.getOrderHistory();
        Constants.currentRequest.setStatus("2");
        customerRequest.set(position, Constants.currentRequest);
        if(customer != null){
            userService.changeUserRequest(customer.getUserName(), (ArrayList<Request>) customerRequest);
        }
        if(seller != null){
            userService.changeUserRequest(seller.getUserName(), (ArrayList<Request>) customerRequest);
        }
        Thread.sleep(3000);
    }

    @Test
    public void onOrderDetailActivityDisplayTest() throws InterruptedException {
        Constants.currentUser = customer;
        assertNotNull(Constants.currentUser);
        onView(allOf(withId(R.id.order_n), withText(Constants.currentUser.getUserName())));
        onView(allOf(withId(R.id.order_uid), withText(Constants.currentUser.getOrderHistory().get(position).getStoreUID())));
        onView(allOf(withId(R.id.order_s), withText(Constants.currentUser.getOrderHistory().get(position).getStatus())));
        onView(allOf(withId(R.id.order_ci), withText(Constants.currentUser.getContactInformation())));
        onView(allOf(withId(R.id.order_a), withText(Constants.currentUser.getOrderHistory().get(position).getAddress())));
        onView(allOf(withId(R.id.order_total), withText(String.valueOf(Constants.currentUser.getOrderHistory().get(position).getTotal()))));
    }

    @Test
    public void customer_completeOrderTest() throws InterruptedException {
        Constants.currentUser = customer;
        assertNotNull(Constants.currentUser);
        onView(withId(R.id.order_c)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(1000);
        onView(withText("Complete order"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        Thread.sleep(1000);
        assertEquals("current request status is 4. ", Constants.currentRequest.getStatus(), "4");

        //change status back to 2
        OrderService orderService = new OrderService();
        orderService.updateRequest(Constants.currentRequest, "status", "2");
    }

    @Test
    public void seller_manageOrderTest() throws InterruptedException {
        Constants.currentUser = seller;
        assertNotNull(Constants.currentUser);
        ActivityScenario.launch(intent);
        onView(withId(R.id.order_status_change)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(1000);
        Intents.intended(hasComponent(OrderManagementActivity.class.getName()));
    }

}