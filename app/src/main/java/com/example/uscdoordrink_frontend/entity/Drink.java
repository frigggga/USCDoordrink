package com.example.uscdoordrink_frontend.entity;



import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Yuxuan Liao
 * @Date: 2022/3/18 2:15
 */


public class Drink {
    private String storeUID;

    private String drinkName;

    private double discount;


    private List<String> ingredients = new ArrayList<>();

    private double price;

    private boolean visibility = false;

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public Drink(){}
    
    public String getStoreUID() {
        return storeUID;
    }

    public void setStoreUID(String storeUID) {
        this.storeUID = storeUID;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }


    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        if (!(o instanceof Drink)){
            return false;
        }
        Drink d = (Drink) o;
        return Objects.equals(this.storeUID, d.getStoreUID()) &&
                Objects.equals(this.drinkName, d.getDrinkName()) &&
                this.discount == d.getDiscount() &&
                this.ingredients.equals(d.getIngredients()) &&
                this.price == d.getPrice();
    }

    public boolean equalContent(Drink d){
        if (d == this){
            return true;
        }
        return Objects.equals(this.drinkName, d.getDrinkName()) &&
                this.discount == d.getDiscount() &&
                this.ingredients.equals(d.getIngredients()) &&
                this.price == d.getPrice();
    }
}