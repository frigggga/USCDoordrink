package com.example.uscdoordrink_frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.entity.Drink;
import com.example.uscdoordrink_frontend.entity.Order;
import com.example.uscdoordrink_frontend.entity.Request;
import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.entity.UserType;
import com.example.uscdoordrink_frontend.service.CallBack.OnFailureCallBack;
import com.example.uscdoordrink_frontend.service.CallBack.OnSuccessCallBack;
import com.example.uscdoordrink_frontend.service.OrderNotificationService;
import com.example.uscdoordrink_frontend.service.OrderService;
import com.example.uscdoordrink_frontend.service.UserService;
import com.google.firebase.firestore.FirebaseFirestore;


import java.time.Duration;
import java.util.ArrayList;
import java.time.Instant;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    public double subtotal;
    public double discounts;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "UserService";

    Button order;
    UserService user = new UserService();
    TextView priceSub, pricedc, pricetax, priceSt, priceTotal;
    String address = null;
    double total;
    int cartQuantity = 0;
    int newQuantity = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        subtotal = getIntent().getDoubleExtra("subtotal", 0.0);
        discounts = getIntent().getDoubleExtra("discounts", 0.0);

        order = findViewById(R.id.place_order);
        priceSub = findViewById(R.id.pricesub);
        pricedc = findViewById(R.id.pricedc);
        pricetax = findViewById(R.id.pricetax);
        priceSt = findViewById(R.id.priceSt);
        priceTotal = findViewById(R.id.pricetotal);

        double tax = subtotal * 0.1;
        double deliverFee = 1.99;

        total = subtotal - discounts + tax + deliverFee;

        priceSub.setText(String.valueOf(subtotal));
        pricedc.setText(String.valueOf(discounts));
        pricetax.setText(String.valueOf(tax));
        priceSt.setText(String.valueOf(deliverFee));
        priceTotal.setText(String.valueOf(total));



        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Order o : Constants.currentUser.getCurrentOrder()){
                    cartQuantity += o.getQuantity();
                }

                int oldQuantity = checkDrinkQuantityInPastDay();
                if(oldQuantity + cartQuantity >= 5) {
                    showAlert(oldQuantity);
                }else{
                    showDialog();
                }
            }

        });
    }

    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("One more step!");
        builder.setMessage("Enter your Address: ");
        final EditText editText = new EditText(this);
        editText.setId(R.id.edittext_address);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        editText.setLayoutParams(layoutParams);
        builder.setView(editText);
//        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Instant instant = Instant.now();
                String start = instant.toString();

                Request req = new Request(start, Constants.currentUser.getUserName(),
                        Constants.currentUser.getContactInformation(),
                        editText.getText().toString(),
                        Constants.currentUser.getCurrentOrder().get(0).getStoreUID(),
                        total,
                        Constants.currentUser.getCurrentOrder());

                //sending request to firebase
                OrderService s = new OrderService();
                s.addRequest(req, new OnSuccessCallBack<Void>() {
                    @Override
                    public void onSuccess(Void input) {
                        Constants.currentUser.setCurrentOrder(new ArrayList<Order>());
                        Constants.currentRequest = req;
                        user.addUserRequest(Constants.currentUser.getUserName(), req);
                    }
                }, new OnFailureCallBack<Exception>() {
                    @Override
                    public void onFailure(Exception input) {
                        Toast.makeText(getApplicationContext(),
                                "order failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Constants.currentUser.addOrderToHistory(req);
                Toast.makeText(OrderActivity.this, "Order is placed. Thank You!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OrderActivity.this, ViewOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    private void showAlert(int oldQuantity){
        AlertDialog.Builder b = new AlertDialog.Builder(OrderActivity.this);
        b.setTitle("Attention!");
        b.setMessage("You have already drunk " + oldQuantity + " cups of tea or coffee today, you are about to order " + cartQuantity +" cups of drink. Your caffeine intake is too much.");
        b.setPositiveButton("Dismiss, continue ordering", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDialog();
            }
        });

        b.setNegativeButton("cancel order, back to map", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(OrderActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        b.show();
    }

    public int checkDrinkQuantityInPastDay(){
        int result = 0;
        for (Request request : Constants.currentUser.getOrderHistory()) {
            if (Duration.between(Instant.parse(request.getStart()), Instant.now()).getSeconds() <= 86400) {
                for (Order order : request.getOrders()) {
                    for (int i = 1; i <= order.getQuantity(); i++) {
                        result += 1;
                    }
                }
            }
        }
        return result;
    }
}