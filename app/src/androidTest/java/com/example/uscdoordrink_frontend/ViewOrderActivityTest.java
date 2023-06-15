package com.example.uscdoordrink_frontend;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.internal.util.Checks.checkNotNull;
import static org.junit.Assert.*;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.entity.Order;
import com.example.uscdoordrink_frontend.entity.Request;
import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.entity.UserType;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Instant;
import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class ViewOrderActivityTest {

    String TAG = "CartActivityTest";
    User customer = null;
    Request req = null;
    String start_time = Instant.now().toString();

    @Before
    public void setUp() throws Exception {
        customer = new User("test", "123456", "3333333333", UserType.CUSTOMER);
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order("lemonade", "aaaa", 1, 4.5, 0.0));
        req = new Request(start_time, customer.getUserName(), customer.getContactInformation(),
                "325 W Adams St, Los Angeles, 90007", "aaaa", 4.5, orders);
        customer.addOrderToHistory(req);
        Constants.currentUser = customer;
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ViewOrderActivity.class);
        ActivityScenario<ViewOrderActivity> scenario = ActivityScenario.launch(intent);
        Intents.init();

    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
        if(customer != null){
            customer = null;
        }
        if(req != null){
            req = null;
        }
    }

    @Test
    public void onCartActivityDisplayTest() {
        onView(withId(R.id.recycler_view_order))
                .check(matches(atPosition(0, hasDescendant(withText(start_time.substring(0, 10))))));
        onView(withId(R.id.recycler_view_order))
                .check(matches(atPosition(0, hasDescendant(withText(Constants.getOrderStatus(req.getStatus()))))));
        onView(withId(R.id.recycler_view_order))
                .check(matches(atPosition(0, hasDescendant(withText(Constants.currentUser.getContactInformation())))));
        onView(withId(R.id.recycler_view_order))
                .check(matches(atPosition(0, hasDescendant(withText(req.getAddress())))));

    }

    @Test
    public void clickToViewOrderDetailTest() throws InterruptedException{
        onView(withId(R.id.recycler_view_order))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(1000);
        Intents.intended(hasComponent(OrderDetailActivity.class.getName()));
    }

    @Test
    public void clickToReturnToMapTest() throws InterruptedException{
        onView(withId(R.id.return_to_map))
                .perform(click());
        Thread.sleep(3000);
        Intents.intended(hasComponent(MapsActivity.class.getName()));
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