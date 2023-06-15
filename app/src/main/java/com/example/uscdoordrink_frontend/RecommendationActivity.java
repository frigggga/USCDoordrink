package com.example.uscdoordrink_frontend;

import android.content.Intent;
import android.os.Bundle;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.entity.Order;
import com.example.uscdoordrink_frontend.entity.Request;
import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.entity.UserType;
import com.example.uscdoordrink_frontend.viewmodels.AddStoreViewModel;


import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.Instant;
import java.util.*;

public class RecommendationActivity extends AppCompatActivity {
    TextView recommendation1, recommendation2, recommendation3, recommendation4, recommendation5;
    Button btReturn, GS1, GS2, GS3, GS4, GS5;
    Set hasViewed = new HashSet<>();
    List<Order> orderHistory = new ArrayList<>();
    Map<String, Integer> frequency = new TreeMap<>();
    List<Integer> frequencyList = new ArrayList<>();
    List<String> ItemList = new ArrayList<>();
    String storeUID1, storeUID2, storeUID3, storeUID4, storeUID5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        btReturn = findViewById(R.id.btn_return);
        recommendation1 = findViewById(R.id.recommendation1);
        recommendation2 = findViewById(R.id.recommendation2);
        recommendation3 = findViewById(R.id.recommendation3);
        recommendation4 = findViewById(R.id.recommendation4);
        recommendation5 = findViewById(R.id.recommendation5);
        GS1 = findViewById(R.id.goToStore1);
        GS2 = findViewById(R.id.goToStore2);
        GS3 = findViewById(R.id.goToStore3);
        GS4 = findViewById(R.id.goToStore4);
        GS5 = findViewById(R.id.goToStore5);
//        User user = new User("abc", "123", "456", UserType.CUSTOMER);
//        List<Order> orders = new ArrayList<>();
//        Order test = new Order("Milk Tea", "efg", 1, 20, 0);
//        orders.add(test);
//        List<Request> testList = new ArrayList<>();
//        Request testR = new Request(Instant.now(), "Wade", "123", "abc", "efg", 20, orders);
//        testList.add(testR);
//        Constants.currentUser = user;
//        user.setOrderHistory(testList);
        if(Constants.currentUser.getOrderHistory() == null){
            GS1.setVisibility(View.GONE);
            GS2.setVisibility(View.GONE);
            GS3.setVisibility(View.GONE);
            GS4.setVisibility(View.GONE);
            GS5.setVisibility(View.GONE);
            recommendation2.setVisibility(View.GONE);
            recommendation3.setVisibility(View.GONE);
            recommendation4.setVisibility(View.GONE);
            recommendation5.setVisibility(View.GONE);
            recommendation1.setText("I am sorry. We can't tell which drink you favor because we don't have your order history record.");
        }else {
            for (Request request : Constants.currentUser.getOrderHistory()) {
                for (Order order : request.getOrders()) {
                    orderHistory.add(order);
                }
            }
            for (Order order : orderHistory) {
                if (!hasViewed.contains(order)) {
                    frequency.put(order.getDrink(), Collections.frequency(orderHistory, order));
                    hasViewed.add(order);
                }
            }
            frequency.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue());
            if(frequency.size() == 0){
                GS1.setVisibility(View.GONE);
                GS2.setVisibility(View.GONE);
                GS3.setVisibility(View.GONE);
                GS4.setVisibility(View.GONE);
                GS5.setVisibility(View.GONE);
                recommendation2.setVisibility(View.GONE);
                recommendation3.setVisibility(View.GONE);
                recommendation4.setVisibility(View.GONE);
                recommendation5.setVisibility(View.GONE);
                recommendation1.setText("I am sorry. We can't tell which drink you favor because we don't have your order history record.");
            }else {
                for (int i = 0; i < 5; i++) {
                    if (frequency.keySet().toArray()[frequency.size() - i - 1].toString() != null) {
                        ItemList.add(frequency.keySet().toArray()[frequency.size() - i - 1].toString());
                        if(frequency.size() - i - 1 == 0){
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if(ItemList.size() == 1){
                    GS1.setVisibility(View.VISIBLE);
                    GS2.setVisibility(View.GONE);
                    GS3.setVisibility(View.GONE);
                    GS4.setVisibility(View.GONE);
                    GS5.setVisibility(View.GONE);
                    recommendation1.setVisibility(View.VISIBLE);
                    recommendation1.setText(ItemList.get(0));
                    recommendation2.setVisibility(View.GONE);
                    recommendation3.setVisibility(View.GONE);
                    recommendation4.setVisibility(View.GONE);
                    recommendation5.setVisibility(View.GONE);
                }else if(ItemList.size() == 2){
                    GS1.setVisibility(View.VISIBLE);
                    GS2.setVisibility(View.VISIBLE);
                    GS3.setVisibility(View.GONE);
                    GS4.setVisibility(View.GONE);
                    GS5.setVisibility(View.GONE);
                    recommendation1.setVisibility(View.VISIBLE);
                    recommendation1.setText(ItemList.get(0));
                    recommendation2.setVisibility(View.VISIBLE);
                    recommendation2.setText(ItemList.get(1));
                    recommendation3.setVisibility(View.GONE);
                    recommendation4.setVisibility(View.GONE);
                    recommendation5.setVisibility(View.GONE);
                }else if(ItemList.size() == 3){
                    GS1.setVisibility(View.VISIBLE);
                    GS2.setVisibility(View.VISIBLE);
                    GS3.setVisibility(View.VISIBLE);
                    GS4.setVisibility(View.GONE);
                    GS5.setVisibility(View.GONE);
                    recommendation1.setVisibility(View.VISIBLE);
                    recommendation1.setText(ItemList.get(0));
                    recommendation2.setVisibility(View.VISIBLE);
                    recommendation2.setText(ItemList.get(1));
                    recommendation3.setVisibility(View.VISIBLE);
                    recommendation3.setText(ItemList.get(2));
                    recommendation4.setVisibility(View.GONE);
                    recommendation5.setVisibility(View.GONE);
                }else if(ItemList.size() == 4){
                    GS1.setVisibility(View.VISIBLE);
                    GS2.setVisibility(View.VISIBLE);
                    GS3.setVisibility(View.VISIBLE);
                    GS4.setVisibility(View.VISIBLE);
                    GS5.setVisibility(View.GONE);
                    recommendation1.setVisibility(View.VISIBLE);
                    recommendation1.setText(ItemList.get(0));
                    recommendation2.setVisibility(View.VISIBLE);
                    recommendation2.setText(ItemList.get(1));
                    recommendation3.setVisibility(View.VISIBLE);
                    recommendation3.setText(ItemList.get(2));
                    recommendation4.setVisibility(View.VISIBLE);
                    recommendation4.setText(ItemList.get(3));
                    recommendation5.setVisibility(View.GONE);
                }else if(ItemList.size() == 5){
                    GS1.setVisibility(View.VISIBLE);
                    GS2.setVisibility(View.VISIBLE);
                    GS3.setVisibility(View.VISIBLE);
                    GS4.setVisibility(View.VISIBLE);
                    GS5.setVisibility(View.VISIBLE);
                    recommendation1.setVisibility(View.VISIBLE);
                    recommendation1.setText(ItemList.get(0));
                    recommendation2.setVisibility(View.VISIBLE);
                    recommendation2.setText(ItemList.get(1));
                    recommendation3.setVisibility(View.VISIBLE);
                    recommendation3.setText(ItemList.get(2));
                    recommendation4.setVisibility(View.VISIBLE);
                    recommendation4.setText(ItemList.get(3));
                    recommendation5.setVisibility(View.VISIBLE);
                    recommendation5.setText(ItemList.get(4));
                }
            }
        }

        if(ItemList.size() >= 1){
            for(Order order: orderHistory){
                if(order.getDrink().equals(ItemList.get(0))){
                    storeUID1 = order.getStoreUID();
                    break;
                }
            }
        }

        if(ItemList.size() >= 2){
            for(Order order: orderHistory){
                if(order.getDrink().equals(ItemList.get(1))){
                    storeUID2 = order.getStoreUID();
                    break;
                }
            }
        }

        if(ItemList.size() >= 3){
            for(Order order: orderHistory){
                if(order.getDrink().equals(ItemList.get(2))){
                    storeUID3 = order.getStoreUID();
                    break;
                }
            }
        }

        if(ItemList.size() >= 4){
            for(Order order: orderHistory){
                if(order.getDrink().equals(ItemList.get(3))){
                    storeUID4 = order.getStoreUID();
                    break;
                }
            }
        }

        if(ItemList.size() >= 5){
            for(Order order: orderHistory){
                if(order.getDrink().equals(ItemList.get(4))){
                    storeUID5 = order.getStoreUID();
                    break;
                }
            }
        }

        GS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecommendationActivity.this, ViewMenuActivity.class);
                i.putExtra("storeUID", storeUID1);
                startActivity(i);
                finish();
            }
        });

        GS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecommendationActivity.this, ViewMenuActivity.class);
                i.putExtra("storeUID", storeUID2);
                startActivity(i);
                finish();
            }
        });

        GS3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecommendationActivity.this, ViewMenuActivity.class);
                i.putExtra("storeUID", storeUID3);
                startActivity(i);
                finish();
            }
        });

        GS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecommendationActivity.this, ViewMenuActivity.class);
                i.putExtra("storeUID", storeUID4);
                startActivity(i);
                finish();
            }
        });

        GS5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecommendationActivity.this, ViewMenuActivity.class);
                i.putExtra("storeUID", storeUID5);
                startActivity(i);
                finish();
            }
        });

        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecommendationActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });
    }
}