package com.example.uscdoordrink_frontend.adaptor;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uscdoordrink_frontend.CartActivity;
import com.example.uscdoordrink_frontend.R;
import com.example.uscdoordrink_frontend.entity.Order;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    private List<Order> orders;
    private CartActivity context;

    public CartAdapter(CartActivity context, List<Order> orders) {
        this.orders = orders;
        this.context = context;
    }

    public void onclick(View view, int position, boolean isLongClick) {
//        Toast.makeText(CartActivity.this, orders.get(position).getDrink(), Toast.LENGTH_SHORT).show();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewItemName, textViewItemPrice, textViewItemQuantity;
        private Button increment;
        private Button decrement;

        ViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.cart_item_name);
            textViewItemPrice = itemView.findViewById(R.id.cart_item_price);
            textViewItemQuantity = itemView.findViewById(R.id.cart_item_quantity);
            increment = itemView.findViewById(R.id.cart_increment);
            decrement = itemView.findViewById(R.id.cart_decrement);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.textViewItemName.setText(orders.get(position).getDrink());

        Locale locale = new Locale("en", "US");
        final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        final double price = orders.get(position).getOrderPrice() * orders.get(position).getQuantity()
                - orders.get(position).getDiscount() * orders.get(position).getQuantity();
        holder.textViewItemPrice.setText(numberFormat.format(price));
        holder.textViewItemQuantity.setText(Integer.toString(orders.get(position).getQuantity()));

        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldQuantity = holder.textViewItemQuantity.getText().toString();
                int newQuantity = Integer.parseInt(oldQuantity) + 1;
                double result = orders.get(position).getOrderPrice() * newQuantity;
                holder.textViewItemPrice.setText(numberFormat.format(result));
                holder.textViewItemQuantity.setText(String.valueOf(newQuantity));

                //Update database
                Order order = orders.get(position);
                order.setQuantity(newQuantity);

                //Update total amount
                int total = 0;
                for(Order cartOrder: orders)
                {
                    total += (cartOrder.getOrderPrice()) * cartOrder.getQuantity()
                            - cartOrder.getDiscount() * cartOrder.getQuantity();
                }
                context.textViewPrice.setText(String.format(" $%s", total));
            }
        });

        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldQuantity = holder.textViewItemQuantity.getText().toString();
                int newQuantity = Integer.parseInt(oldQuantity) - 1;
                double result = orders.get(position).getOrderPrice() * newQuantity;
                holder.textViewItemPrice.setText(numberFormat.format(result));
                holder.textViewItemQuantity.setText(String.valueOf(newQuantity));

                //Update database
                Order order = orders.get(position);
                order.setQuantity(newQuantity);

                //Update total amount
                int total = 0;
                for(Order cartOrder: orders)
                {
                    total += (cartOrder.getOrderPrice()) * cartOrder.getQuantity()
                    - cartOrder.getDiscount() * cartOrder.getQuantity();
                }
                context.textViewPrice.setText(String.format(" $%s", total));
            }
        });

    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

}