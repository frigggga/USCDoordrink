package com.example.uscdoordrink_frontend;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.entity.Order;
import com.example.uscdoordrink_frontend.entity.Request;
import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.entity.UserType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RecommendationActivityTest {


    @Before
    public void setUp() throws Exception {
        Constants.currentUser = new User("Wade","1234", "1234", UserType.CUSTOMER);
        List<Request> orderHistory = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        Order order = new Order("Pineapple Lemonade","7PqA0Yca8mKTntrvIHPT",1,10.0, 0.0);
        orders.add(order);
        Request request = new Request(Instant.now().toString(),"Wade","1234","usc","7PqA0Yca8mKTntrvIHPT",10.0,orders);
        orderHistory.add(request);
        Constants.currentUser.setOrderHistory(orderHistory);
        Thread.sleep(3000);
        Intents.init();
    }

    @Test
    public void testRecommendationsDisplayedScenario(){
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), RecommendationActivity.class);
        ActivityScenario<RecommendationActivity> scenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.recommendations)).check(matches(withText("Guess you like:")));
    }

    @Test
    public void testRecommendationsDisplayedNoRecordScenario() throws InterruptedException {
        Constants.currentUser = new User("Wade2","1234", "1234", UserType.CUSTOMER);
        Thread.sleep(3000);
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), RecommendationActivity.class);
        ActivityScenario<RecommendationActivity> scenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.recommendation1)).check(matches(withText("I am sorry. We can't tell which drink you favor because we don't have your order history record.")));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}