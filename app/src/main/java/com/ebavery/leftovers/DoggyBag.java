package com.ebavery.leftovers;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Elizabeth on 4/17/2017.
 */

public class DoggyBag {

    private Calendar dateMade;
    private Calendar expDate;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();

    public DoggyBag (Calendar dateMade){

        this.dateMade = dateMade;
    }

    public void setIngredients (String name){

        this.ingredients.add(new Ingredient(name));
    }

    public void calculateExpDate(){
        int days = leastDaysGood();
        this.expDate = this.dateMade;
        this.expDate.add(Calendar.DATE, days);
    }

    private int leastDaysGood(){
        int len = this.ingredients.size();
        int leastDaysGood = this.ingredients.get(0).getDaysGood();
        for (int i = 1; i < len; i++){
            int test = this.ingredients.get(i).getDaysGood();
            if (test < leastDaysGood) {
                leastDaysGood = test;
            }
        }
        return leastDaysGood;
    }

    public Calendar getDateMade(){
        return this.dateMade;
    }
    public Calendar getExpDate(){
        this.calculateExpDate();
        return this.expDate;
    }
    public ArrayList<Ingredient> getIngredients(){
        return this.ingredients;
    }

}
