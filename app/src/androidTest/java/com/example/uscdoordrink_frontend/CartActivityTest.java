package com.example.uscdoordrink_frontend;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.internal.util.Checks.checkNotNull;
import static org.junit.Assert.*;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
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

import java.time.Instant;
import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class CartActivityTest {

    String TAG = "CartActivityTest";
    User customer = null;

    @Before
    public void setUp() throws Exception {
        customer = new User("test", "123456", "3333333333", UserType.CUSTOMER);
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order("lemonade", "aaaa", 1, 4.5, 0.0));
        customer.setCurrentOrder(orders);
        Constants.currentUser = customer;
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), CartActivity.class);
        ActivityScenario<CartActivity> scenario = ActivityScenario.launch(intent);
        Intents.init();

    }

    @After
    public void tearDown() throws Exception {
        if(customer != null){
            customer = null;
        }
        Intents.release();
    }

    @Test
    public void onCartActivityDisplayTest() {
        onView(withId(R.id.recycler_cart))
                .check(matches(atPosition(0, hasDescendant(withText("lemonade")))));
        onView(withId(R.id.order_price))
                .check(matches(withText(" $4.5")));

    }

    @Test
    public void submitOrderTest() throws  InterruptedException{
        onView(withId(R.id.btnPlaceOrder))
                .check(matches(isDisplayed())).perform(click());
        Thread.sleep(1000);
        Intents.intended(hasComponent(OrderActivity.class.getName()));
    }

    @Test
    public void returnToMapTest() throws  InterruptedException{
        onView(withId(R.id.r_to_map))
                .check(matches(isDisplayed())).perform(click());
        Thread.sleep(3000);
        Intents.intended(hasComponent(MapsActivity.class.getName()));
    }

    @Test
    public void changeQuantityTest() throws  InterruptedException{
        onView(withId(R.id.recycler_cart)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.cart_increment)));
        Thread.sleep(1000);
//        onView(withId(R.id.recycler_cart))
//                .check(matches(atPosition(0, hasDescendant(withText("2")))));
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

class MyViewAction {

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

}


