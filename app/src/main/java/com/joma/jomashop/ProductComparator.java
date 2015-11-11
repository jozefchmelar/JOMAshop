package com.joma.jomashop;

import java.util.Comparator;

/**
 * Created by Jozef on 5.11.2015.
 */
public class ProductComparator implements Comparator<Product> {
    @Override
    public int compare(Product lhs, Product rhs) {
        if(lhs.getPrice()>rhs.getPrice() )return -1;
        if(lhs.getPrice()<rhs.getPrice()) return 1;
        return 0;
    }
}
