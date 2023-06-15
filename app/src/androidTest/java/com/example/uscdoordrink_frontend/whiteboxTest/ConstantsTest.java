package com.example.uscdoordrink_frontend.whiteboxTest;

import static org.junit.Assert.*;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.uscdoordrink_frontend.Constants.Constants;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ConstantsTest {

    private final String TAG = "ConstantsTest";
    private final String result0 = "Order placed. ";
    private final String result1 = "Delivering in progress. ";
    private final String result2 = "Order delivered. ";
    private final String result3 = "Cannot track order. ";
    private final String result4 = "Order completed. ";

    @Test
    public void getOrderStatusTest() {
        String status0 = Constants.getOrderStatus("0");
        assertEquals("status 0", result0, status0);

        String status1 = Constants.getOrderStatus("1");
        assertEquals("status 1", result1, status1);

        String status2 = Constants.getOrderStatus("2");
        assertEquals("status 2", result2, status2);

        String status3 = Constants.getOrderStatus("3");
        assertEquals("status 3", result3, status3);

        String status4 = Constants.getOrderStatus("4");
        assertEquals("status 4", result4, status4);
    }
}