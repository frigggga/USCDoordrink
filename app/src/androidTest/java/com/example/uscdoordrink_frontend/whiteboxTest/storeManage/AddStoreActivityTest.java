package com.example.uscdoordrink_frontend.whiteboxTest.storeManage;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.uscdoordrink_frontend.AddStoreActivity;
import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.entity.Drink;
import com.example.uscdoordrink_frontend.entity.Store;
import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.entity.UserType;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.awaitility.Awaitility.*;

import android.app.Instrumentation;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Yuxuan Liao
 * @Date: 2022/4/11 2:27
 */
@RunWith(AndroidJUnit4.class)
public class AddStoreActivityTest {
    @Test
    public void createWithoutInformationTest(){
        try(ActivityScenario<AddStoreActivity> scenario = ActivityScenario.launch(AddStoreActivity.class)){
            scenario.onActivity(activity -> {
                Store testStore = new Store();
                assertNotNull(activity.theStore);
                assertEquals("Both stores should be empty", testStore, activity.theStore.mStoreModel.getValue());
            });
        }
    }

    @Test
    public void createWithInformationTest(){
        Store s = new Store();
        s.setStoreUID("TestUID");
        s.setStoreName("Atelier Crenn");
        s.setStoreAddress(37.7983, -122.4360);
        s.setAddressString("3127 Fillmore St., San Francisco, 94123, United States");
        List<Drink> menu = new ArrayList<>();

        Drink drink1 = new Drink();
        drink1.setPrice(20);
        drink1.setDrinkName("Brandy");
        menu.add(drink1);
        s.setMenu(menu);
        Constants.currentStore = s;
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), AddStoreActivity.class);
        intent.putExtra("storeUID", s.getStoreUID());
        try(ActivityScenario<AddStoreActivity> scenario = ActivityScenario.launch(intent)){
            scenario.onActivity(activity -> {
                assertNotNull(activity.theStore);
                assertEquals("Stores from other activities", s, activity.theStore.mStoreModel.getValue());
            });
        }
    }

    @Test
    public void toProfileTest(){
        Instrumentation.ActivityMonitor monitor = InstrumentationRegistry.getInstrumentation()
                .addMonitor("com.example.uscdoordrink_frontend.ProfileActivity", null, false);
        try(ActivityScenario<AddStoreActivity> scenario = ActivityScenario.launch(AddStoreActivity.class)){
            Constants.currentUser = new User("TestName", "1234", "contact", UserType.CUSTOMER);
            scenario.onActivity(AddStoreActivity::toProfile);
            assertEquals("Record of profile activity monitor", 1, monitor.getHits());
        }
    }

    @After
    public void cleanUp(){
        Constants.currentUser = null;
        Constants.currentStore = null;
    }
}
