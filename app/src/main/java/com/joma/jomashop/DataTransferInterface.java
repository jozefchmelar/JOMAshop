package com.joma.jomashop;

/**
 * Created by Jozef on 12.11.2015.
 */
public interface DataTransferInterface {
    public Product productToEdit(Product changedProduct, int position);

    public double deletedProductValue(double value);
}
