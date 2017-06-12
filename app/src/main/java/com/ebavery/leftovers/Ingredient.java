package com.ebavery.leftovers;

/**
 * Created by Elizabeth on 4/17/2017.
 */

public class Ingredient {
    private String name;
    private int daysGood;

    public Ingredient (String name){

        this.name = name;

        this.daysGood = findDaysGood(this.name);
    }

    private int findDaysGood(String name){
        switch (name){
            case "beans":
                return 3;
            case "cooked meat":
                return 3;
            case "lunch meat":
                return 4;
            case "cooked fish":
                return 3;
            case "fresh fruit":
                return 1;
            case "soft fresh vegetables (cucumber, tomato, pepper, greens)":
                return 2;
            case "hard fresh vegetables (carrot, cabbage, beets)":
                return 4;
            case "cooked seafood":
                return 1;
            case "cooked vegetables":
                return 3;
            case "rice":
                return 5;
            case "tofu":
                return 4;
            case "dairy":
                return 7;
            case "milk alternative":
                return 6;
            case "pasta":
                return 4;
            default:
                return 0;

        }
    }

    public int getDaysGood(){
        return this.daysGood;
    }

}
