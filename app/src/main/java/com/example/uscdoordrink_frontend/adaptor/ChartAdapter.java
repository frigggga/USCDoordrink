package com.example.uscdoordrink_frontend.adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uscdoordrink_frontend.R;
import com.example.uscdoordrink_frontend.entity.Drink;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.ChartViewHolder>{
    Context context;
    ArrayList<Drink> drinks;
    static final String TAG = "CartAdapter";

    public ChartAdapter(Context context, ArrayList<Drink> drinks) {
        this.context = context;
        this.drinks = drinks;
    }

    @NonNull
    @Override
    public ChartAdapter.ChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_chart, parent, false);
        return new ChartAdapter.ChartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartAdapter.ChartViewHolder holder, int position) {

        Drink drink = drinks.get(position);
        holder.drinkName.setText(drink.getDrinkName());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Store").document(drink.getStoreUID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    holder.storeName.setText(String.valueOf(documentSnapshot.get("storeName")));
                }
                else{
                    Log.w(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    public static class ChartViewHolder extends RecyclerView.ViewHolder{

        TextView drinkName;
        TextView storeName;
        public ChartViewHolder(@NonNull View itemView) {
            super(itemView);
            drinkName = itemView.findViewById(R.id.itemNameChart);
            storeName = itemView.findViewById(R.id.storeName);
        }
    }
}

