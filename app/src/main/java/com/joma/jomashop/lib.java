package com.joma.jomashop;

import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Jozef on 4.11.2015.
 */
public class lib {
    public static final String JOMAex = "Jomashop exception";
    public static final String JOMAtest = "Jomashop testing";
    public static final double randomDouble(int min,int max){
        Random r = new Random();
        return    round( min + (max - min) * r.nextDouble(),2);
    }
    public static final double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    public static final int randomInt(int min,int max){
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
    public static final String getRandomProductName(){
        ArrayList<String > list = new ArrayList<>();
        list.add("apple");
        list.add("coca cola");
        list.add("fanta");
        list.add("sprite");
        list.add("cofee");
        list.add("kozel");
        list.add("mozarella");
        list.add("dental floss");
        list.add("chips");
        list.add("fries");
        list.add("chewing gum");
        list.add("eggs");
        return list.get(randomInt(0,list.size()-1));
    }

    /**
     *
     * @return currency symbol .ie $ €...
     */
    public static final String CurrencySymbol() {
        Currency currency = Currency.getInstance(Locale.getDefault()); // you get the dollar,euro symbol...
        return currency.getSymbol();
    }
    /**
     * This code will take TextView with String value that I expect do be some double and
     * @param toEdit textview that has a double in it.
     * @param value  plus or minus some value
     * @return new value as string.
     */
    public static final String addValueToTextView(TextView toEdit, double value){
        double currentValue = Double.parseDouble(toEdit.getText().toString());
        double newValue= currentValue + value;
        return newValue +"";
    }

}
