package com.joma.jomashop;

import java.util.ArrayList;

/**
 * Created by Jozef on 11.11.2015.
 */
public class ShoppingCart {

    private ArrayList<Product> ShoppingList = ShoppingListHolder.getInstance().getShoppingList();
    private double totalValue;

}
