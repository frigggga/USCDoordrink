package com.example.uscdoordrink_frontend;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import static org.junit.Assert.*;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.Root;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.core.internal.deps.guava.collect.Maps;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.ComponentNameMatchers;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.entity.UserType;
import com.example.uscdoordrink_frontend.service.OrderNotificationService;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ProfileActivityTest {

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @Test
    public void testWordsDisplayedScenario() throws InterruptedException {
        Constants.currentUser = new User("Wade","1234", "1234", UserType.CUSTOMER);
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ProfileActivity.class);
        ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(intent);
        Espresso.onView(withText("Username")).check(matches(isDisplayed()));
        Espresso.onView(withText("ContactInfo")).check(matches(isDisplayed()));
        Espresso.onView(withText("Password")).check(matches(isDisplayed()));
        Espresso.onView(withText("View Chart")).check(matches(isDisplayed()));
    }

    @Test
    public void testUserNameCorrectScenario(){
        Constants.currentUser = new User("Wade","1234", "1234", UserType.CUSTOMER);
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ProfileActivity.class);
        ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.username)).check(matches(withText("Wade")));
    }

    @Test
    public void testUserContactInfoCorrectScenario(){
        Constants.currentUser = new User("Wade","1234", "1234", UserType.CUSTOMER);
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ProfileActivity.class);
        ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.contactInfo)).check(matches(withText("1234")));
    }

    @Test
    public void testUserPasswordCorrectScenario(){
        Constants.currentUser = new User("Wade","1234", "1234", UserType.CUSTOMER);
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ProfileActivity.class);
        ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.Password)).check(matches(withText("1234")));
    }

    @Test
    public void testViewOrderHistoryScenario() throws InterruptedException {
        Constants.currentUser = new User("Wade","1234", "1234", UserType.CUSTOMER);
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ProfileActivity.class);
        ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.btn_ViewChart)).perform(click());
        Thread.sleep(1000);
        intended(hasComponent(ViewChartActivity.class.getName()));
    }

    @Test
    public void testReturnToMapScenario() throws InterruptedException {
        Constants.currentUser = new User("Wade","1234", "1234", UserType.CUSTOMER);
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ProfileActivity.class);
        ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.back_to_map)).perform(click());
        Thread.sleep(3000);
        intended(hasComponent(MapsActivity.class.getName()));
    }

    @Test
    public void testManageOrdersScenario() throws InterruptedException {
        Constants.currentUser = new User("Wade","1234", "1234", UserType.CUSTOMER);
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ProfileActivity.class);
        ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.manage_order)).perform(click());
        Thread.sleep(3000);
        intended(hasComponent(ViewOrderActivity.class.getName()));
    }
    @After
    public void tearDown() throws Exception {
        Constants.currentUser = null;
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), OrderNotificationService.class);
        InstrumentationRegistry.getInstrumentation().getTargetContext().stopService(intent);
        Intents.release();
    }
}