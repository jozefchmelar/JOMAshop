package com.joma.jomashop;

import java.util.ArrayList;

/**
 * Created by Jozef on 5.11.2015.
 */
public class ShoppingListHolder {
//singleton class for all the data.
    private static ShoppingListHolder ourInstance = new ShoppingListHolder();
    private ArrayList<Product> ShoppingList;

    public static ShoppingListHolder getInstance() {
        return ourInstance;
    }

    private ShoppingListHolder() {
        ShoppingList = new ArrayList<>();
    }

    public ArrayList<Product> getShoppingList(){
        return ShoppingList;
    }
}
