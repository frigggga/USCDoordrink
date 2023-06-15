package com.example.uscdoordrink_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uscdoordrink_frontend.adaptor.MenuAdapter;
import com.example.uscdoordrink_frontend.entity.Drink;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ViewMenuActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MenuAdapter menuAdapter;
    ArrayList<String> drinkNameList;
    ArrayList<String> priceList;
    ArrayList<String> ingredients;
    ArrayList<Drink> drinks;
    FirebaseFirestore db;
    String storeUID;
    String drinkUID;
    Button btSelect;
    static final String TAG = "ViewMenuActivity";
    List<Drink> menu = new ArrayList<>();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu);
        btSelect = findViewById(R.id.btn_GoToCart);
        storeUID = getIntent().getStringExtra("storeUID");
        try {
            readData(new FirestoreCallback() {
                @Override
                public void onCallback(List<Drink> list) {
                    recyclerView = findViewById(R.id.recycler_menu);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setHasFixedSize(true);
                    drinks = new ArrayList<Drink>();
                    for(Drink drink: list){
                        drinkNameList.add(drink.getDrinkName());
                        priceList.add(String.valueOf(drink.getPrice()));
                        drinks.add(drink);
                    }
                    drinkNameList = new ArrayList<String>();
                    priceList = new ArrayList<String>();
                    menuAdapter = new MenuAdapter(context, drinks);
                    recyclerView.setAdapter(menuAdapter);
                }
            });
        } finally {
            recyclerView = findViewById(R.id.recycler_menu);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            drinks = new ArrayList<Drink>();
            drinkNameList = new ArrayList<String>();
            priceList = new ArrayList<String>();
            menuAdapter = new MenuAdapter(this, drinks);
            recyclerView.setAdapter(menuAdapter);
            btSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ViewMenuActivity.this, CartActivity.class);
                    startActivity(i);
                }
            });
        }
    }

    private void readData(FirestoreCallback firestoreCallback){
        db = FirebaseFirestore.getInstance();
        db.collection("Store")
                .document(storeUID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            List<Map<String, Object>> drinkList = (List<Map<String, Object>>) document.get("menu");
                            for (int i = 0; i < drinkList.size(); i++) {
                                Drink drink = new Drink();
                                drink.setDrinkName(drinkList.get(i).get("drinkName").toString());
                                drink.setPrice((Double) drinkList.get(i).get("price"));
                                drink.setDiscount((Double) drinkList.get(i).get("discount"));
                                drink.setIngredients(castList(drinkList.get(i).get("ingredients"), String.class));
                                drink.setStoreUID(drinkList.get(i).get("storeUID").toString());
                                menu.add(drink);
                                Log.d("Test:", "2");
                            }
                            firestoreCallback.onCallback(menu);
                        } else {
                            Log.w(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private interface FirestoreCallback{
        void onCallback(List<Drink> list);
    }

    public static <T> List<T> castList(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }
}


