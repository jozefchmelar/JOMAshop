package com.joma.jomashop;

import android.util.Log;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Jozef on 4.11.2015.
 */
public class Product implements Serializable {
    private String ProductName;
    private double Price;
    private int Quantity;
    private boolean VisibleSettings; // Edit and delete button in listview
    private boolean Favourite;
    private String Barcode;

    public Product(String productName, double price, int quantity, boolean visibleSettings, boolean favourite, String barcode) {
        this.ProductName = productName;
        this.Price = price;
        this.Quantity = quantity;
        this.VisibleSettings = visibleSettings;
        this.Favourite = favourite;
        this.Barcode = barcode;
    }

    public Product(String productName, double price) {
        this.ProductName = productName;
        this.Price = price;
        this.Quantity = 1;
        this.VisibleSettings = false;
        this.Favourite = false;
        this.Barcode = null;
    }

    public Product() {
        this.ProductName = lib.getRandomProductName();
        this.Price = lib.randomInt(2, 100);
        this.Quantity = lib.randomInt(1, 6);
        this.VisibleSettings = false;
        this.Favourite = false;
        this.Barcode = null;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        this.ProductName = productName;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        this.Price = price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity > 0)
            this.Quantity = quantity;
        else throw new IndexOutOfBoundsException();

    }

    public boolean isVisibleSettings() {
        return VisibleSettings;
    }

    public void setVisibleSettings(boolean visibleSettings) {
        this.VisibleSettings = visibleSettings;
    }

    public boolean isFavourite() {
        return Favourite;
    }

    public void setFavourite(boolean favourite) {
        this.Favourite = favourite;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        this.Barcode = barcode;
    }


    public void QuantityMinus(int number) {
        try {
            setQuantity(getQuantity() - number);
        } catch (IndexOutOfBoundsException e) {
            Log.e(lib.JOMAex, e.toString());
        }
    }

    public void QuantityPlus(int number) {
        setQuantity(getQuantity() + number);
    }

    public double getTotalPrice() {
        return this.Quantity * this.Price;
    }
}
