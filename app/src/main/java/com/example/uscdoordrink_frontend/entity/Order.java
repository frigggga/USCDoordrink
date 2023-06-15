package com.example.uscdoordrink_frontend.entity;

import android.util.Pair;

import java.util.Date;

/**
 * @Author: Yuxuan Liao
 * @Date: 2022/3/18 2:15
 */

/**
 * @Author: Yuxiang Zhang
 * @Date: 2022/3/20 18:24
 */


public class Order {

    private String drink;

    private String storeUID;

    private int quantity;

    private double price;

    private double discount;

    public Order(){}

    public Order(String drink, String storeUID, int quantity, double price, double discount) {
        this.drink = drink;
        this.storeUID = storeUID;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getStoreUID() {
        return storeUID;
    }

    public void setStoreUID(String storeUID) {
        this.storeUID = storeUID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getOrderPrice() {
        return price;
    }

    public void setOrderPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
