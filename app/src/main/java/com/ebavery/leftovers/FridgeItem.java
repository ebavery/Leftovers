package com.ebavery.leftovers;

/**
 * Created by Elizabeth on 5/5/2017.
 */

public class FridgeItem {

    private final String name;
    private final String expDate;
    private final int imageResource;

    public FridgeItem(String name, String expDate, int imageResource){
        this.name = name;
        this.expDate = expDate;
        this.imageResource = imageResource;
    }

    public String getName(){
        return this.name;
    }

    public String getExpDate(){
        return this.expDate;
    }

    public int getImageResource(){
        return this.imageResource;
    }
}
