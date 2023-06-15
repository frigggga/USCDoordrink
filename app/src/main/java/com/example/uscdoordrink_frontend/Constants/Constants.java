package com.example.uscdoordrink_frontend.Constants;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.uscdoordrink_frontend.entity.Request;
import com.example.uscdoordrink_frontend.entity.Store;
import com.example.uscdoordrink_frontend.entity.User;

public class Constants {
    public static User currentUser;
    public static Store currentStore;
    public static Request currentRequest;

    public static String getOrderStatus(String status) {
        switch (status) {
            case "0":
                return "Order placed. ";
            case "1":
                return "Delivering in progress. ";
            case "2":
                return "Order delivered. " ;
            case "3":
                return "Cannot track order. ";
            case "4":
                return "Order completed. ";
            default:
                return "Sorry, your order status is currently not available.";
        }
    }

}
