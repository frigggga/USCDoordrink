package com.example.uscdoordrink_frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.adaptor.CartAdapter;
import com.example.uscdoordrink_frontend.entity.Order;

import java.util.ArrayList;
import java.util.List;


public class CartActivity extends AppCompatActivity {

        RecyclerView recyclerView;
        public TextView textViewPrice;
        Button buttonOrder, rToMap;
        double subtotal = 0;
        double discount = 0;

//        Order a = new Order("Pineapple Lemonade", "7PqA0Yca8mKTntrvIHPT", 1, 5, 0);


        CartAdapter cartAdapter;
        ArrayList<Order> orders = new ArrayList<>();


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cart);

            textViewPrice = findViewById(R.id.order_price);
            buttonOrder = findViewById(R.id.btnPlaceOrder);
            rToMap = findViewById(R.id.r_to_map);

            recyclerView = findViewById(R.id.recycler_cart);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//            orders.add(a);
            loadCart();

            rToMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(CartActivity.this, MapsActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            buttonOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CartActivity", textViewPrice.getText().toString().trim());
                    if (textViewPrice.getText().toString().trim().equals("$0.0"))
                        Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                    else
                        sendData();
                }
            });
        }


            //Helper Methods
            private void loadCart () {
                orders = (ArrayList<Order>) Constants.currentUser.getCurrentOrder();
                cartAdapter = new CartAdapter(this, orders) {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        Toast.makeText(CartActivity.this, orders.get(position).getDrink(), Toast.LENGTH_SHORT).show();
                    }
                };
                recyclerView.setAdapter(cartAdapter);

                //Calculating total price
                for (Order order : orders) {
                    subtotal += order.getOrderPrice() * order.getQuantity();
                    discount += order.getDiscount() * order.getQuantity();
                }
                textViewPrice.setText(String.format(" $%s", subtotal));
            }

            private void sendData(){
                Intent i = new Intent(CartActivity.this, OrderActivity.class);
                i.putExtra("subtotal", subtotal);
                i.putExtra("discounts", discount);
                startActivity(i);
                finish();
            }
        }
