package com.joma.jomashop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jozef on 12.11.2015.
 */
public class ShoppingCart implements Serializable {
    private double totalPrice;
    private ArrayList<Product> ShoppingList;
    private Date date;

    public ShoppingCart(ArrayList<Product> shoppingList, double totalPrice, Date date) {
        ShoppingList = shoppingList;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<Product> getShoppingList() {
        return ShoppingList;
    }

    public void setShoppingList(ArrayList<Product> shoppingList) {
        ShoppingList = shoppingList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
