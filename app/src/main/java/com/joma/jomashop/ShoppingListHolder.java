package com.joma.jomashop;

import java.util.ArrayList;

/**
 * Created by Jozef on 5.11.2015.
 */
public class ShoppingListHolder {
//singleton class for all the data.
    private static ShoppingListHolder ourInstance = new ShoppingListHolder();
    private static ArrayList<Product> ShoppingList;

    private ShoppingListHolder() {

        ShoppingList = new ArrayList<>();
    }

    public static ShoppingListHolder getInstance() {
        return ourInstance;
    }

    public static double getTotalPrice() {
        double sum = 0;
        for (Product product : ShoppingList) {
            sum += product.getTotalPrice();
        }
        return sum;
    }

    public static boolean deleteContent() {
        ShoppingList = new ArrayList<>();
        return ShoppingList.isEmpty() ? true : false;
    }
    public ArrayList<Product> getShoppingList(){

        return ShoppingList;
    }

}
