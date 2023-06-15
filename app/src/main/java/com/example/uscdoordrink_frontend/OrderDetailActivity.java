package com.example.uscdoordrink_frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.entity.Order;
import com.example.uscdoordrink_frontend.entity.Request;
import com.example.uscdoordrink_frontend.entity.UserType;
import com.example.uscdoordrink_frontend.service.OrderService;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    TextView order_name, order_UID, order_status, order_ci, order_address, order_price, order_lst;
    Button manage, complete;
    Request r;
    int position;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_name = findViewById(R.id.order_n);
        order_UID = findViewById(R.id.order_uid);
        order_status = findViewById(R.id.order_s);
        order_ci = findViewById(R.id.order_ci);
        order_address = findViewById(R.id.order_a);
        order_price = findViewById(R.id.order_total);
        order_lst = findViewById(R.id.order_lst);

        position = getIntent().getIntExtra("position", 0);


        manage = findViewById(R.id.order_status_change);
        complete = findViewById(R.id.order_c);


        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OrderDetailActivity.this, OrderManagementActivity.class);
                i.putExtra("position", position);
                startActivity(i);
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage();
            }
        });

        showDisplay();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.finish();
        return true;
    }

    public void showMessage(){
        AlertDialog.Builder b = new AlertDialog.Builder(OrderDetailActivity.this);
        b.setTitle("Complete order");
        b.setMessage("Are you sure you want to complete this order? This action cannot be undone");
        b.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Constants.currentRequest.setStatus("4");
                List<Request> rs =  Constants.currentUser.getOrderHistory();
                rs.get(position).setStatus("4");
                OrderService orderService = new OrderService();
                orderService.updateRequest(r, "status","4");


            }
        });

        b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        b.show();
    }

    public void showDisplay(){
        r = Constants.currentUser.getOrderHistory().get(position);
        ArrayList<Order> orders = (ArrayList<Order>) r.getOrders();
        String order_string = "";
        for(Order o : orders){
            order_string += o.getDrink();
            order_string += ", " + o.getQuantity() + " cup\n";
        }

        order_name.setText("name: " + r.getName());
        order_UID.setText("store: " + r.getStoreUID());
        order_status.setText("order status: " + Constants.getOrderStatus(r.getStatus()));
        order_ci.setText("contact info: " + r.getContactInformation());
        order_address.setText("address: " + r.getAddress());
        order_price.setText("total: " + r.getTotal());
        order_lst.setText("drinks: \n" + order_string);

        if(Constants.currentUser.getUserType() == UserType.SELLER && !r.getStatus().equals("4")){
            manage.setVisibility(View.VISIBLE);
        }else{
            manage.setVisibility(View.INVISIBLE);
        }

        if(Constants.currentUser.getUserType() == UserType.CUSTOMER && !r.getStatus().equals("4")){
            complete.setVisibility(View.VISIBLE);
        }else{
            complete.setVisibility(View.INVISIBLE);
        }
    }
}