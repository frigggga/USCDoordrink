package com.example.uscdoordrink_frontend.whiteboxTest.map;

import android.Manifest;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.uscdoordrink_frontend.MapsActivity;
import com.example.uscdoordrink_frontend.entity.Store;
import com.example.uscdoordrink_frontend.service.CallBack.OnFailureCallBack;
import com.example.uscdoordrink_frontend.service.CallBack.OnSuccessCallBack;
import com.example.uscdoordrink_frontend.utils.DirectionHelper;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.awaitility.Awaitility.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Yuxuan Liao
 * @Date: 2022/4/11 12:48
 */
@RunWith(AndroidJUnit4.class)
public class MapActivityTest {

    private final static String TAG = "MapActivityTest";

    @Rule
    public GrantPermissionRule mRuntimeLocationPermission = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public ActivityScenarioRule<MapsActivity> activityScenarioRule = new ActivityScenarioRule<MapsActivity>(MapsActivity.class);

    @Test
    public void locationPermissionTest(){
        ActivityScenario<MapsActivity> scenario = activityScenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            assertTrue("has location permission", activity.isLocationPermissionGranted());
        });
    }

    @Test
    public void testSetUpMarkersThread(){
        ActivityScenario<MapsActivity> scenario = activityScenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            // Waits the setUpMarkers() in testThread to complete, and assert the result to be a list of 2
            Thread testThread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    await().atMost(5, TimeUnit.SECONDS)
                            .until(() -> activity.getMarkersNearby() != null && activity.getMarkersNearby().size() == 2);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<String> markerStoreList = new ArrayList<>();
                            for (Marker m : activity.getMarkersNearby()){
                                assertTrue("type of tag is store", m.getTag() instanceof Store);
                                markerStoreList.add(((Store) m.getTag()).getStoreUID());
                            }
                            List<String> expected = new ArrayList<>();
                            expected.add("DGcKm39rOfkd2FJ5yfbh");
                            expected.add("iCeVSwKCgwMVcDdQFJGv");
                            Collections.sort(markerStoreList);
                            Collections.sort(expected);
                            assertEquals("markers store list on west", expected, markerStoreList);
                            Log.d(TAG, "second setUpMarkers test finished");
                        }
                    });
                }
            });

            // Waits the setUpMarkers() in main thread to complete, and assert the result to be a list of 5
            // Then changes the device location and starts testThread2
            Thread testThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    await().atMost(5, TimeUnit.SECONDS)
                            .until(() -> activity.getMarkersNearby() != null && activity.getMarkersNearby().size() == 5);
                    List<String> markerStoreList = new ArrayList<>();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (Marker m : activity.getMarkersNearby()){
                                assertTrue("type of tag is store", m.getTag() instanceof Store);
                                markerStoreList.add(((Store) m.getTag()).getStoreUID());
                            }
                            List<String> expected = new ArrayList<>();
                            expected.add("09a6rdzbl1SFxQOPXkuT");
                            expected.add("7PqA0Yca8mKTntrvIHPT");
                            expected.add("DGcKm39rOfkd2FJ5yfbh");
                            expected.add("VNU8salaXDOGcyMIn6o7");
                            expected.add("iCeVSwKCgwMVcDdQFJGv");
                            Collections.sort(markerStoreList);
                            Collections.sort(expected);
                            assertEquals("markers store list", expected, markerStoreList);
                            assertNotNull("lastKnownLocation", activity.getLastKnownLocation());
                            Log.d(TAG, "first setUpMarkers test finished");

                            // change the location so that only 2 markers should be spawned
                            activity.getLastKnownLocation().setLatitude(34.0255);
                            activity.getLastKnownLocation().setLongitude(-118.3247);
                            activity.setUpMarkers();
                            testThread2.start();
                            Log.d(TAG, "second setUpMarkers test start");
                        }
                    });
                }
            });

            //Assure lastKnownLocation is non-null. Then set the location and start testThread
            assertTrue("has location permission", activity.isLocationPermissionGranted());
            activity.updateLocation(new OnSuccessCallBack<Location>() {
                @Override
                public void onSuccess(Location input) {
                    assertNotNull("lastKnownLocation", input);
                    input.setLatitude(34.0259);
                    input.setLongitude(-118.2874);
                    activity.setUpMarkers();
                    testThread.start();
                    Log.d(TAG, "first setUpMarkers test start, please got to Logcat for more info");
                }
            }, new OnFailureCallBack<Exception>() {
                @Override
                public void onFailure(Exception input) {
                    fail("cannot update location");
                }
            });
        });
    }
}
