package com.joma.jomashop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements DataTransferInterface {

    private ArrayList<Product> ShoppingList = ShoppingListHolder.getInstance().getShoppingList();
    private ListView ListViewShopping;
    private TextView textViewTotalPrice;
    private TextView currencySymbol1;
    private TextView currencySymbol2;
    private ProductAdapter productAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // display currency symbols. These are those two in the top.
        currencySymbol1 = (TextView) findViewById(R.id.currencySymbol1);
        currencySymbol2 = (TextView) findViewById(R.id.currencySymbol2);
        currencySymbol1.setText(lib.CurrencySymbol());
        currencySymbol2.setText(lib.CurrencySymbol());
        //total price
        textViewTotalPrice = (TextView) findViewById(R.id.textViewTotalPrice);
        // set adapter
        productAdapter = new ProductAdapter(this, ShoppingList, this);
        //assign listview, sort it, set adapter.
        ListViewShopping = (ListView) findViewById(R.id.ShoppingList);
        sortShoppingList();
        ListViewShopping.setAdapter(productAdapter);
    }

    /*  Here I get the intent with the product and position that I edited, and try to update it
        in shopping list, sort it and then notifydataset.there might be no intent so NullPointer.
    */
    @Override
    public void onResume() {
        super.onResume();  //on resume happens when I return from Editproduct
        try {
            int editedPosition = getIntent().getExtras().getInt("position");
            Product editedProduct = (Product) getIntent().getSerializableExtra("product");
            ShoppingList.set(editedPosition, editedProduct);
            sortShoppingList();
            updateTotalPrice();
            productAdapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            Log.e(lib.JOMAex, e.toString());
        }

    }

    /*
    Button for adding product.
     */
    public void newProduct(View view) {
        Product newProduct = new Product();
        ShoppingList.add(newProduct);
        updateTotalPrice();
        ListViewShopping.setAdapter(productAdapter);
    }


    @Override
    public Product productToEdit(Product changedProduct, int position) {
        ShoppingList.set(position, changedProduct);
        updateTotalPrice();
        sortShoppingList();
        ListViewShopping.setAdapter(productAdapter);
        return null;
    }

    @Override
    public double deletedProductValue(double value) {
        updateTotalPrice();
        sortShoppingList();
        ListViewShopping.setAdapter(productAdapter);
        return 0;
    }

    private void updateTotalPrice() {
        textViewTotalPrice.setText(ShoppingListHolder.getTotalPrice() + "");

    }

    private void sortShoppingList() {
        Collections.sort(ShoppingList, new ProductComparator());
    }
}
