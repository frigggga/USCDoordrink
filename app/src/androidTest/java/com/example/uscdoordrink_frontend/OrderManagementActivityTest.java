package com.example.uscdoordrink_frontend;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.entity.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class OrderManagementActivityTest {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "OrderManagementActivityTest";

    private int position = 0;
    private User seller = null;
    ActivityScenario toLaunch;

    static Intent intent;

    static {
        intent = new Intent(getApplicationContext(), OrderManagementActivity.class);
        int position = 0;
        intent.putExtra("position", position);
    }


    @Before
    public void setUp() throws Exception {
        DocumentReference docRef = db.collection("User").document("sellerTest");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, "success");
                seller = documentSnapshot.toObject(User.class);
            }
        });

        Thread.sleep(3000);
        Constants.currentUser = seller;
        Constants.currentRequest = seller.getOrderHistory().get(position);
        toLaunch.launch(intent);
        Intents.init();
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

    @Test
    public void onOrderManagementDisplay(){
        assertNotNull(Constants.currentUser);
        Constants.currentRequest = Constants.currentUser.getOrderHistory().get(position);
        onView(allOf(withId(R.id.status_display), withText(Constants.getOrderStatus(Constants.currentRequest.getStatus()))));
        onView(allOf(withId(R.id.a), withText(Constants.currentUser.getUserName())));
        onView(allOf(withId(R.id.b), withText(Constants.currentUser.getContactInformation())));
        onView(allOf(withId(R.id.c), withText(Constants.currentRequest.getAddress())));
    }

    @Test
    public void applyStatusChangeTest(){
        onView(withId(R.id.seekBar)).perform(setProgress(3));
        onView(allOf(withId(R.id.status_display), withText("3")));
        onView(withId(R.id.apply_change)).check(matches(isDisplayed())).perform(click());
//        String expected1 = "Order status successfully changed!";
////        onView(withText(expected1)).inRoot(new ToastMatcher())
////                .check(matches(isDisplayed()));

        //change status back to 2;
        onView(withId(R.id.seekBar)).perform(setProgress(2));
        onView(allOf(withId(R.id.status_display), withText("2")));
        onView(withId(R.id.apply_change)).check(matches(isDisplayed())).perform(click());
//        String expected2 = "----- Order arrived -----";
//        onView(withText(expected2)).inRoot(new ToastMatcher())
//                .check(matches(isDisplayed()));
    }

    @Test
    public void returnButtonTest(){
        ActivityScenario<OrderManagementActivity> scenario = ActivityScenario.launch(intent);
        onView(withId(R.id.complete_order)).check(matches(isDisplayed())).perform(click());
        assertTrue(scenario.getResult().getResultCode() == Activity.RESULT_CANCELED);
    }

    public static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }
            @Override
            public String getDescription() {
                return "Set a progress on a SeekBar";
            }
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }
}
