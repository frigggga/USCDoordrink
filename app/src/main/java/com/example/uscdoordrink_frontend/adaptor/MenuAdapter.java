package com.example.uscdoordrink_frontend.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.entity.Drink;
import com.example.uscdoordrink_frontend.R;
import com.example.uscdoordrink_frontend.entity.Order;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {


    Context context;
    ArrayList<Drink> drinks;

    public MenuAdapter(Context context, ArrayList<Drink> drinks) {
        this.context = context;
        this.drinks = drinks;
    }

    public void onclick(View view, int position, boolean isLongClick) {
//        Toast.makeText(CartActivity.this, orders.get(position).getDrink(), Toast.LENGTH_SHORT).show();
    }




    @NonNull
    @Override
    public MenuAdapter.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_drink_menu, parent, false);
        return new MenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MenuViewHolder holder, int position) {

        Drink drink = drinks.get(position);
        holder.drinkName.setText(drink.getDrinkName());
        holder.price.setText(String.valueOf(drink.getPrice()));
        holder.ingredients.setText("Ingredients: " + drink.getIngredients().toString());
        boolean isVisibility = drink.isVisibility();
        holder.constraintLayout.setVisibility(isVisibility ? View.VISIBLE: View.GONE);
        holder.drinkName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drink drink = drinks.get(holder.getAdapterPosition());
                drink.setVisibility(!drink.isVisibility());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drink drink = drinks.get(holder.getAdapterPosition());
                List<Order> temp = Constants.currentUser.getCurrentOrder();
                temp.add(new Order(drink.getDrinkName(), drink.getStoreUID(), 1, drink.getPrice(), drink.getDiscount()));
                Constants.currentUser.setCurrentOrder(temp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder{

        TextView drinkName;
        TextView price;
        TextView ingredients;
        Button select;
        ConstraintLayout constraintLayout;
        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            drinkName = itemView.findViewById(R.id.itemName);
            price = itemView.findViewById(R.id.price);
            select = itemView.findViewById(R.id.select);
            ingredients = itemView.findViewById(R.id.ingredientsDetail);
            constraintLayout = itemView.findViewById(R.id.expandedLayout);
        }
    }
}
