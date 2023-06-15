package com.example.uscdoordrink_frontend.utils;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @Author: Yuxuan Liao
 * @Date: 2022/3/27 15:26
 */
public class DirectionHelper {
    public static enum Modes{
        driving,
        walking,
        bicycling
    }

    @SuppressLint("DefaultLocale")
    public static String setUpURL(LatLng origin, LatLng destination, Modes mode){

        return String.format("https://maps.googleapis.com/maps/api/directions/json?origin=%f,%f&destination=%f,%f&mode=%s&key=%s",
                origin.latitude, origin.longitude,
                destination.latitude, destination.longitude,
                mode.toString(),
                "AIzaSyCoj0HiPuANRmMbBN4fA51oCiRa8y_q8fA");
    }

    public static String decodePolyline(Response response, List<LatLng> result){
        try{
            JSONObject direction = new JSONObject(response.body().string());
            JSONObject leg = direction.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0);

            String time = leg.getJSONObject("duration").getString("text");
            JSONArray steps = leg.getJSONArray("steps");
            for (int i = 0; i < steps.length(); i++) {
                LatLng startLocation = new LatLng(steps.getJSONObject(i).getJSONObject("start_location").getDouble("lat"),
                        steps.getJSONObject(i).getJSONObject("start_location").getDouble("lng"));
                LatLng endLocation = new LatLng(steps.getJSONObject(i).getJSONObject("end_location").getDouble("lat"),
                        steps.getJSONObject(i).getJSONObject("end_location").getDouble("lng"));
                result.add(startLocation);
                result.add(endLocation);
            }
            return time;
        }catch(IOException e){
            Log.w("decodePolyline", "IOExecption occurred: " + e.getMessage());
            return null;
        }catch(JSONException e){
            Log.w("decodePolyline", "JSONException occurred" + e.getMessage());
            return null;
        }
    }

}
