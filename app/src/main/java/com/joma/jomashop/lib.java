package com.joma.jomashop;

import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Jozef on 4.11.2015.
 */
public class lib {

    public static final String JOMAex = "Jomashop exception";
    public static final boolean TESTING_OBJECT = true;
    public static final String JOMAtest = "Jomashop testing";
    public static final int SCAN_BARCODE = 2;
    public static final int ADD_PRODUCT = 1;

    public static final double randomDouble(final int min, final int max) {
        Random r = new Random();
        return round(min + (max - min) * r.nextDouble(), 2);
    }

    public static final double round(final double value, final int decimalPlaces) {
        if (decimalPlaces < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, decimalPlaces);
        long tmp = Math.round(value * factor);
        return (double) tmp / factor;
    }

    public static final int randomInt(final int min, final int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static final String getRandomProductName() {
        ArrayList<String> list = new ArrayList<>();
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
        return list.get(randomInt(0, list.size() - 1));
    }

    /**
     * @return currency symbol .ie $ €...
     */
    public static final String CurrencySymbol() {
        Currency currency = Currency.getInstance(Locale.getDefault()); // you get the dollar,euro symbol...
        return currency.getSymbol();
    }

    /**
     * This code will take TextView with String value that I expect do be some double and
     * add the VALUE paramater to it , returns strin
     *
     * @param toEdit textview that has a double in it.
     * @param value  plus or minus some value
     * @return new value as string.
     */
    public static final String addValueToTextView(final TextView toEdit, final double value) {
        double currentValue = Double.parseDouble(toEdit.getText().toString());
        double newValue = currentValue + value;
        return newValue + "";
    }

    public static final String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMdyyyy' 'kh");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    /**
     * This method will take text, add every letter until it finds " " empty space char.
     *
     * @param text
     * @return will return first word
     */
    public static String getFirstWordFromString(String text) {
        String result = "";
        int position = 0;
        while (text.charAt(position) != ' ') {
            result += text.charAt(position++);
        }
        return result;
    }

    /**
     * This method is looking for the position of first char in text.
     *
     * @param text
     * @return
     */
    public static int getPositionOfFirstChar(String text, char charToFind) {
        int result = 0;
        while (text.charAt(result) != charToFind) {
            result++;
        }
        return result;
    }

}
