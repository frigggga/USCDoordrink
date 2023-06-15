package com.example.uscdoordrink_frontend;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import androidx.test.espresso.Espresso;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.internal.util.Checks.checkNotNull;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.entity.Order;
import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.entity.UserType;
import com.google.firebase.firestore.FirebaseFirestore;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import io.cucumber.java.sl.In;


public class ViewMenuActivityTest {

    String TAG = "ViewMenuActivityTest";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static Intent intent;

    ActivityScenario toLaunch;
    static {
        intent = new Intent(getApplicationContext(), ViewMenuActivity.class);
        String storeUID = "7PqA0Yca8mKTntrvIHPT";
        intent.putExtra("storeUID", storeUID);
    }

    @Before
    public void setUp() throws Exception {
        Constants.currentUser = new User("Wade", "1234", "1234" ,UserType.CUSTOMER);
        toLaunch.launch(intent);
        Thread.sleep(3000);
        Intents.init();
    }

    @Test
    public void testMenuDisplayScenario() {
        Espresso.onView(withId(R.id.recycler_menu))
                .check(matches(atPosition(0, hasDescendant(withText("Pineapple Lemonade")))));
        Espresso.onView(withId(R.id.price))
                .check(matches(withText("5.0")));
    }

    @Test
    public void testViewCartScenario() throws InterruptedException {
        Espresso.onView(withId(R.id.btn_GoToCart))
                .perform(click());
        Thread.sleep(1000);
        intended(hasComponent(CartActivity.class.getName()));
    }

    @Test
    public void testIngredientsScenario() throws InterruptedException {
        Espresso.onView(withId(R.id.itemName)).perform(click());
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.ingredientsDetail)).check(matches(withText("Ingredients: [Lemon, Pineapple]")));
    }

    @Test
    public void testIngredientsGoneScenario() throws InterruptedException {
        Espresso.onView(withId(R.id.itemName)).perform(click());
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.itemName)).perform(click());
        Thread.sleep(1000);
        Espresso.onView(withText("Ingredients: [Lemon, Pineapple]")).check(matches(not(isDisplayed())));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}