package com.example.uscdoordrink_frontend;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
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
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.BoundedMatcher;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ChartDetailActivityTest {

    static Intent intent;
    ActivityScenario toLaunch;
    static {
        intent = new Intent(getApplicationContext(), ChartDetailActivity.class);
        intent.putExtra("Basis", "Daily");
    }
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
        toLaunch.launch(intent);
        Thread.sleep(3000);
        Intents.init();
    }

    @Test
    public void testOrderHistoryDisplayedScenario(){
        Espresso.onView(withId(R.id.recycler_chart))
                .check(matches(atPosition(0, hasDescendant(withText("Pineapple Lemonade")))));
        Espresso.onView(withId(R.id.storeName))
                .check(matches(withText("Northern Cafe")));
    }

    @Test
    public void testReturnToBasisScenario() throws InterruptedException {
        Espresso.onView(withId(R.id.btn_ReturnToBasis)).perform(click());
        Thread.sleep(1000);
        intended(hasComponent(ViewChartActivity.class.getName()));
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