package com.example.uscdoordrink_frontend;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
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
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.Root;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
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

import com.example.uscdoordrink_frontend.service.OrderNotificationService;
import com.google.android.gms.location.ActivityTransition;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> loginActivityActivityScenarioRule =
            new ActivityScenarioRule<LoginActivity>(LoginActivity.class);


    private String userName = "Wade";
    private String password = "Lyz007354";
    private String sellerTest = "sellerTest";
    private String sellerPassword = "111111";
    private String sellerTest2 = "Testcase2";
    private String sellerPassword2 = "1234";
    private View decorView;


    @Before
    public void setUp() throws Exception {
        Intents.init();
        loginActivityActivityScenarioRule.getScenario().onActivity(activity -> {
            decorView = activity.getWindow().getDecorView();
        });
    }


    @Test
    public void testCustomerLoginToProfileCorrectScenario() throws InterruptedException {
        //Input the userName and password
        Espresso.onView(withId(R.id.et_account)).perform(clearText()).perform(typeText(userName), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_password)).perform(clearText()).perform(typeText(password), ViewActions.closeSoftKeyboard());
        //Click the button
        Espresso.onView(withId(R.id.btn_login)).perform(click());
        //See whether it will login successfully
        Thread.sleep(1000);
        intended(hasComponent(ProfileActivity.class.getName()));
    }

    @Test
    public void testSellerWithUIDLoginToProfileCorrectScenario() throws InterruptedException {
        //Input the userName and password
        Espresso.onView(withId(R.id.et_account)).perform(clearText()).perform(typeText(sellerTest), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_password)).perform(clearText()).perform(typeText(sellerPassword), ViewActions.closeSoftKeyboard());
        //Click the button
        Espresso.onView(withId(R.id.btn_login)).perform(click());
        //See whether it will login successfully
        Thread.sleep(1000);
        intended(hasComponent(ProfileActivity.class.getName()));
    }

    @Test
    public void testSellerWithoutUIDLoginToProfileCorrectScenario() throws InterruptedException {
        //Input the userName and password
        Espresso.onView(withId(R.id.et_account)).perform(clearText()).perform(typeText(sellerTest2), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.et_password)).perform(clearText()).perform(typeText(sellerPassword2), ViewActions.closeSoftKeyboard());
        //Click the button
        Espresso.onView(withId(R.id.btn_login)).perform(click());
        //See whether it will login successfully
        Thread.sleep(3000);
        intended(hasComponent(AddStoreActivity.class.getName()));
    }




   @Test
    public void testUserLoginToSignUpScenario() throws InterruptedException {
        Espresso.onView(withId(R.id.Register)).perform(click());
        Thread.sleep(1000);
        intended(hasComponent(SignUpActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
        Intent intent = new Intent(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext(), OrderNotificationService.class);
        InstrumentationRegistry.getInstrumentation().getTargetContext().stopService(intent);

    }
}