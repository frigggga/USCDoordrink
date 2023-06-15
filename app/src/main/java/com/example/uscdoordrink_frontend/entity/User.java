package com.example.uscdoordrink_frontend.entity;


import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yuxuan Liao
 * @Date: 2022/3/18 2:13
 */


public class User {

    private String userName;

    private String contactInformation;

    private String password;

    private List<Order> currentOrder = new ArrayList<>();

    private List<Request> orderHistory = new ArrayList<>();

    private UserType userType;


    private String storeUID;

    public User(){}


    public User(String u, String p, String c, UserType t) {
        this.userName = u;
        this.contactInformation = c;
        this.password = p;
        if(t == UserType.CUSTOMER){
            this.userType = UserType.CUSTOMER;
        } else {
            this.userType = UserType.SELLER;
        }
        this.storeUID = "toBeAssigned";

        currentOrder = new ArrayList<Order>();
        orderHistory = new ArrayList<Request>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Order> getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(List<Order> currentOrder) {
        this.currentOrder = currentOrder;
    }

    public List<Request> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<Request> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getStoreUID() {
        return storeUID;
    }

    public void setStoreUID(String storeUID) {
        this.storeUID = storeUID;
    }

    public void addOrderToHistory(Request r){
        orderHistory.add(r);
    }
}