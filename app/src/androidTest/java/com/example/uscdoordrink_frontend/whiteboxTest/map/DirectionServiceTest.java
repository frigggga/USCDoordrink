package com.example.uscdoordrink_frontend.whiteboxTest.map;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ServiceTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.awaitility.Awaitility.*;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.uscdoordrink_frontend.service.CallBack.OnFailureCallBack;
import com.example.uscdoordrink_frontend.service.CallBack.OnSuccessCallBack;
import com.example.uscdoordrink_frontend.service.DirectionService;
import com.example.uscdoordrink_frontend.utils.DirectionHelper;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Yuxuan Liao
 * @Date: 2022/4/11 4:55
 */
@RunWith(AndroidJUnit4.class)
public class DirectionServiceTest {

    private final String TAG = "DirectionServiceTest";
    private Response response;
    private Callable<Boolean> hasResponse() {return () -> response != null; }

    @Rule
    public final ServiceTestRule serviceRule = new ServiceTestRule();

    @Before
    public void init(){
        response = null;
    }

    @Test
    public void directionServiceTest() throws TimeoutException {
        Intent serviceIntent = new Intent(ApplicationProvider.getApplicationContext(), DirectionService.class);
        IBinder binder = serviceRule.bindService(serviceIntent);
        DirectionService service = ((DirectionService.DirectionServiceBinder) binder).getService();

        String URL = DirectionHelper.setUpURL(new LatLng(34.0254, -118.2870),
                new LatLng(34.0338, -118.2419),
                DirectionHelper.Modes.driving);
        service.getDirections(URL, new OnSuccessCallBack<Response>() {
            @Override
            public void onSuccess(Response input) {
                response = input;
                Log.d(TAG, "direction service fetch success");
            }
        }, new OnFailureCallBack<Exception>() {
            @Override
            public void onFailure(Exception input) {
                Log.w(TAG, "direction service fetch failed", input);
            }
        });
        await().atMost(5, TimeUnit.SECONDS).until(hasResponse());
        List<LatLng> result = new ArrayList<>();
        String time = DirectionHelper.decodePolyline(response, result);
        assertNotNull("Total time should not be null, please refer to system output for more detailed information", time);
        assertNotEquals("The size of the result list", 0, result.size());
        Log.d(TAG, time);
        for (LatLng pos : result){
            Log.d(TAG, pos.toString());
        }
    }
}
