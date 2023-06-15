package com.example.uscdoordrink_frontend;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.entity.UserType;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ViewChartActivityTest {

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @Test
    public void testDailyBasisScenario() throws InterruptedException {
        Constants.currentUser = new User("Wade","1234", "1234", UserType.CUSTOMER);
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ViewChartActivity.class);
        ActivityScenario<ViewChartActivity> scenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.btn_dailyBasis)).perform(click());
        Thread.sleep(1000);
        intended(hasComponent(ChartDetailActivity.class.getName()));
    }

    @Test
    public void testWeeklyBasisScenario() throws InterruptedException {
        Constants.currentUser = new User("Wade","1234", "1234", UserType.CUSTOMER);
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ViewChartActivity.class);
        ActivityScenario<ViewChartActivity> scenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.btn_weeklyBasis)).perform(click());
        Thread.sleep(1000);
        intended(hasComponent(ChartDetailActivity.class.getName()));
    }

    @Test
    public void testMonthlyBasisScenario() throws InterruptedException {
        Constants.currentUser = new User("Wade","1234", "1234", UserType.CUSTOMER);
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ViewChartActivity.class);
        ActivityScenario<ViewChartActivity> scenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.btn_monthlyBasis)).perform(click());
        Thread.sleep(1000);
        intended(hasComponent(ChartDetailActivity.class.getName()));
    }

    @Test
    public void testReturnToProfileScenario() throws InterruptedException {
        Constants.currentUser = new User("Wade","1234", "1234", UserType.CUSTOMER);
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ViewChartActivity.class);
        ActivityScenario<ViewChartActivity> scenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.btn_ReturnToProfile)).perform(click());
        Thread.sleep(1000);
        intended(hasComponent(ProfileActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}