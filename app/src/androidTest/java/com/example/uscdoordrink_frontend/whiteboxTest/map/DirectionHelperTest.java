package com.example.uscdoordrink_frontend.whiteboxTest.map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.app.Instrumentation;
import android.os.Bundle;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.uscdoordrink_frontend.utils.DirectionHelper;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import okio.Buffer;
import okio.BufferedSource;

/**
 * @Author: Yuxuan Liao
 * @Date: 2022/3/27 17:04
 */
@RunWith(AndroidJUnit4.class)
public class DirectionHelperTest {
    private final String TAG = "DecodePolyLineTest";
    private final String URL = "https://maps.googleapis.com/maps/api/directions/json?origin=34.022415,-118.285530&destination=34.025510,-118.277352&mode=driving&key=AIzaSyCoj0HiPuANRmMbBN4fA51oCiRa8y_q8fA";

    @Test
    public void setUpURLTest(){
        String result = DirectionHelper.setUpURL(new LatLng(34.022415, -118.285530),
                new LatLng(34.025510,-118.277352),
                DirectionHelper.Modes.driving);
        assertEquals("Result of URL", URL, result);
    }

    @Test
    public void decodePolylineTest() {
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(URL)
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            List<LatLng> result = new ArrayList<>();
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE); // request the entire body.
            Buffer buffer = source.buffer();
            String responseBodyString = buffer.clone().readString(StandardCharsets.UTF_8);
            String time = DirectionHelper.decodePolyline(response, result);
            assertNotNull("Total time should not be null, please refer to system output for more detailed information", time);
            Log.d(TAG, time);
            for (LatLng pos : result){
                Log.d(TAG, pos.toString());
            }
            Log.d(TAG, responseBodyString);
        }catch(IOException e){
            Log.w(TAG, "IOException", e);
            fail("Failed when fetching direction results");
        }
    }
}
