package com.example.uscdoordrink_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.entity.Drink;
import com.example.uscdoordrink_frontend.entity.Order;
import com.example.uscdoordrink_frontend.entity.Request;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ViewChartActivity extends AppCompatActivity {
    Button bt_dailyBasis, bt_weeklyBasis, bt_monthlyBasis, bt_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_chart);
        bt_dailyBasis = findViewById(R.id.btn_dailyBasis);
        bt_weeklyBasis = findViewById(R.id.btn_weeklyBasis);
        bt_monthlyBasis = findViewById(R.id.btn_monthlyBasis);
        bt_return = findViewById(R.id.btn_ReturnToProfile);

        bt_dailyBasis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewChartActivity.this, ChartDetailActivity.class);
                i.putExtra("Basis", "Daily");
                startActivity(i);
            }
        });
        bt_weeklyBasis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewChartActivity.this, ChartDetailActivity.class);
                i.putExtra("Basis", "Weekly");
                startActivity(i);
            }
        });
        bt_monthlyBasis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewChartActivity.this, ChartDetailActivity.class);
                i.putExtra("Basis", "Monthly");
                startActivity(i);
            }
        });
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewChartActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });
    }
}