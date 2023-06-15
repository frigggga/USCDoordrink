package com.example.uscdoordrink_frontend.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uscdoordrink_frontend.AddStoreActivity;
import com.example.uscdoordrink_frontend.R;
import com.example.uscdoordrink_frontend.entity.Drink;
import com.example.uscdoordrink_frontend.entity.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddStoreDrink extends Fragment {


    private Drink currentDrink;
    private String previousName;

    public AddStoreDrink() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String storeUID = getArguments().getString("storeUID");
            String drinkName = getArguments().getString("drinkName");
            double discount = getArguments().getDouble("discount");
            ArrayList<String> ingredients = getArguments().getStringArrayList("ingredients");
            double price = getArguments().getDouble("price");
            if (drinkName != null && ingredients != null ){
                currentDrink = new Drink();
                currentDrink.setStoreUID(storeUID);
                currentDrink.setDrinkName(drinkName);
                currentDrink.setDiscount(discount);
                currentDrink.setIngredients((List<String>) ingredients);
                currentDrink.setPrice(price);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_add_store_drink, container, false);
        EditText textName = (EditText) mainView.findViewById(R.id.editTextAddDrinkName);
        String defaultName = textName.getHint().toString();
        EditText textIngredientOne = (EditText) mainView.findViewById(R.id.editTextAddDrinkIngredientOne);
        EditText textIngredientTwo = (EditText) mainView.findViewById(R.id.editTextAddDrinkIngredientTwo);
        EditText textIngredientThree = (EditText) mainView.findViewById(R.id.editTextAddDrinkIngredientThree);
        List<EditText> textIngredients = new ArrayList<>();
        textIngredients.add(textIngredientOne);
        textIngredients.add(textIngredientTwo);
        textIngredients.add(textIngredientThree);
        String defaultIngredient = textIngredientOne.getHint().toString();
        EditText textPrice = (EditText) mainView.findViewById(R.id.editTextAddDrinkPrice);
        EditText textDiscount = (EditText) mainView.findViewById(R.id.editTextAddDrinkDiscount);
        String defaultDiscount = "";
        Button confirmDrink = (Button) mainView.findViewById(R.id.button_confirm_drink);
        Button deleteDrink = (Button) mainView.findViewById(R.id.button_delete_drink);
        @NonNull Store store = Objects.requireNonNull(((AddStoreActivity) requireActivity()).theStore.mStoreModel.getValue());

        if (currentDrink != null){
            previousName = currentDrink.getDrinkName();
            textName.setText(currentDrink.getDrinkName());
            textDiscount.setText(String.valueOf(currentDrink.getDiscount()));
            for (int i = 0; i < currentDrink.getIngredients().size(); i++) {
                textIngredients.get(i).setText(currentDrink.getIngredients().get(i));
            }
            textPrice.setText(String.valueOf(currentDrink.getPrice()));
            deleteDrink.setVisibility(View.VISIBLE);
        }

        confirmDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(textName.getText().toString()) || defaultName.equals(textName.getText().toString())){
                    Toast.makeText(getContext(), "Please Enter your drinkName", Toast.LENGTH_SHORT).show();
                }else{
                    try{
                        Drink drink = new Drink();
                        drink.setDrinkName(textName.getText().toString());
                        addIngredient(drink, textIngredientOne.getText().toString(), defaultIngredient);
                        addIngredient(drink, textIngredientTwo.getText().toString(), defaultIngredient);
                        addIngredient(drink, textIngredientThree.getText().toString(), defaultIngredient);
                        double price = Double.parseDouble(textPrice.getText().toString());
                        drink.setPrice(price);

                        if (defaultDiscount.equals(textDiscount.getText().toString())){
                            drink.setDiscount(0.0);
                        }else{
                            double discount = Double.parseDouble(textDiscount.getText().toString());
                            if (discount > price || discount < 0){
                                throw new NumberFormatException();
                            }
                            drink.setDiscount(discount);
                        }

                        boolean found = false;
                        for (Drink d : store.getMenu()){
                            if (Objects.equals(drink.getDrinkName(), d.getDrinkName())){
                                found = true;
                                d.setStoreUID(drink.getStoreUID());
                                d.setDrinkName(drink.getDrinkName());
                                d.setDiscount(drink.getDiscount());
                                d.setIngredients(drink.getIngredients());
                                d.setPrice(drink.getPrice());
                                break;
                            }
                        }
                        if (!found){
                            if (currentDrink != null && previousName != null){
                                for (Drink d : store.getMenu()){
                                    if (Objects.equals(previousName, d.getDrinkName())){
                                        d.setStoreUID(drink.getStoreUID());
                                        d.setDrinkName(drink.getDrinkName());
                                        d.setDiscount(drink.getDiscount());
                                        d.setIngredients(drink.getIngredients());
                                        d.setPrice(drink.getPrice());
                                        break;
                                    }
                                }
                            }else{
                                store.getMenu().add(drink);
                            }
                        }
                        Navigation.findNavController(view).navigate(R.id.action_drink_to_menu);
                    }catch (NumberFormatException e){
                        Toast.makeText(getContext(), "Please Enter a valid price/discount", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        deleteDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentDrink == null || previousName == null){
                    Toast.makeText(getContext(), "Error: cannot delete an non-existing drink", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean found = false;
                Drink toRemove = null;
                for (Drink d : store.getMenu()){
                    if (Objects.equals(previousName, d.getDrinkName())){
                        toRemove = d;
                        found = true;
                        break;
                    }
                }
                if (!found){
                    Toast.makeText(getContext(), "Error: cannot delete an non-existing drink", Toast.LENGTH_SHORT).show();
                }else{
                    store.getMenu().remove(Objects.requireNonNull(toRemove));
                    Navigation.findNavController(view).navigate(R.id.action_drink_to_menu);
                }
            }
        });

        return mainView;
    }

    private void addIngredient(Drink drink, String ingredient, @NonNull String defaultIngredient){
        if (!defaultIngredient.equals(ingredient) && !"".equals(ingredient)){
            drink.getIngredients().add(ingredient);
        }
    }
}