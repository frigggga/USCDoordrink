package com.example.uscdoordrink_frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.adaptor.ChartAdapter;
import com.example.uscdoordrink_frontend.adaptor.MenuAdapter;
import com.example.uscdoordrink_frontend.entity.Drink;
import com.example.uscdoordrink_frontend.entity.Order;
import com.example.uscdoordrink_frontend.entity.Request;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ChartDetailActivity extends AppCompatActivity {
    TextView drinkName, storeName;
    ArrayList<Drink> drinkList = new ArrayList<>();
    RecyclerView recyclerView;
    ChartAdapter chartAdapter;
    Button Return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_detail);
        drinkName = findViewById(R.id.itemNameChart);
        storeName = findViewById(R.id.storeName);
        if (getIntent().getStringExtra("Basis").equals("Daily")) {
            for (Request request : Constants.currentUser.getOrderHistory()) {
                if (Duration.between(Instant.parse(request.getStart()), Instant.now()).getSeconds() <= 86400) {
                    for (Order order : request.getOrders()) {
                        Drink drink = new Drink();
                        drink.setStoreUID(order.getStoreUID());
                        drink.setDrinkName(order.getDrink());
                        for (int i = 1; i <= order.getQuantity(); i++) {
                            drinkList.add(drink);
                        }
                    }
                }
            }
        } else if (getIntent().getStringExtra("Basis").equals("Weekly")) {
            for (Request request : Constants.currentUser.getOrderHistory()) {
                if (Duration.between(Instant.parse(request.getStart()), Instant.now()).getSeconds() <= 604800) {
                    for (Order order : request.getOrders()) {
                        Drink drink = new Drink();
                        drink.setStoreUID(order.getStoreUID());
                        drink.setDrinkName(order.getDrink());
                        for (int i = 1; i <= order.getQuantity(); i++) {
                            drinkList.add(drink);
                        }
                    }
                }
            }
        }else if (getIntent().getStringExtra("Basis").equals("Monthly")) {
            for (Request request : Constants.currentUser.getOrderHistory()) {
                if (Duration.between(Instant.parse(request.getStart()), Instant.now()).getSeconds() <= 2592000) {
                    for (Order order : request.getOrders()) {
                        Drink drink = new Drink();
                        drink.setStoreUID(order.getStoreUID());
                        drink.setDrinkName(order.getDrink());
                        for (int i = 1; i <= order.getQuantity(); i++) {
                            drinkList.add(drink);
                        }
                    }
                }
            }
            }
        recyclerView = findViewById(R.id.recycler_chart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        chartAdapter = new ChartAdapter(this, drinkList);
        recyclerView.setAdapter(chartAdapter);
        Return = findViewById(R.id.btn_ReturnToBasis);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChartDetailActivity.this, ViewChartActivity.class);
                startActivity(i);
            }
        });
        }
    }