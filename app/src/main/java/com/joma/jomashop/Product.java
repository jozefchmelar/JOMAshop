package com.joma.jomashop;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.io.Serializable;

/**
 * Created by Jozef on 4.11.2015.
 */
public class Product extends SugarRecord<Product> implements Serializable {

    String name;
    double price;
    int quantity;
    String barcode;
    @Ignore
    boolean visiblesettings; // Edit and delete button in listview
    @Ignore
    boolean favourite;

    //constructor for ORM
    public Product() {
        this.quantity = 1;
    }

    public Product(String name, double price, int quantity, boolean visiblesettings, boolean favourite, String barcode) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.visiblesettings = visiblesettings;
        this.favourite = favourite;
        this.barcode = barcode;
    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
        this.quantity = 1;
        this.visiblesettings = false;
        this.favourite = false;
        this.barcode = null;
    }

    public Product(boolean isTest) {
        this.name = lib.getRandomProductName();
        this.price = lib.randomInt(2, 100);
        this.quantity = lib.randomInt(1, 6);
        this.visiblesettings = false;
        this.favourite = false;
        this.barcode = "55665";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity > 0)
            this.quantity = quantity;
        else throw new IndexOutOfBoundsException();

    }

    public boolean isVisiblesettings() {
        return visiblesettings;
    }

    public void setVisiblesettings(boolean visiblesettings) {
        this.visiblesettings = visiblesettings;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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
        return this.quantity * this.price;
    }

    @Override
    public String toString() {
        return this.name + " " + this.price + lib.CurrencySymbol();
    }
}
