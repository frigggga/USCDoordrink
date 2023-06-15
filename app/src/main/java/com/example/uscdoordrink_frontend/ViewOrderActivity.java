package com.example.uscdoordrink_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.adaptor.RecyclerItemClickListener;
import com.example.uscdoordrink_frontend.adaptor.ViewOrderAdapter;
import com.example.uscdoordrink_frontend.entity.Drink;
import com.example.uscdoordrink_frontend.entity.Order;
import com.example.uscdoordrink_frontend.entity.Request;
import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.entity.UserType;
import com.example.uscdoordrink_frontend.service.OrderService;
import com.example.uscdoordrink_frontend.service.UserService;

import java.util.ArrayList;



public class ViewOrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button rToMap;
    ViewOrderAdapter viewOrderAdapter;
    ArrayList<Request> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        rToMap = findViewById(R.id.return_to_map);
        recyclerView = findViewById(R.id.recycler_view_order);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        loadOrders();




        rToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewOrderActivity.this, MapsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void loadOrders(){
        requests = (ArrayList<Request>) Constants.currentUser.getOrderHistory();
        viewOrderAdapter = new ViewOrderAdapter(this, requests);
        recyclerView.setAdapter(viewOrderAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(ViewOrderActivity.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent i = new Intent(ViewOrderActivity.this, OrderDetailActivity.class);
                        i.putExtra("position", position);
                        startActivity(i);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }

}
