package com.joma.jomashop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity implements DataTransferInterface {
    private ArrayList<Product> shoppingList;
    private ListView ListViewShopping;
    private TextView textViewTotalPrice;
    private TextView currencySymbol1;
    private TextView currencySymbol2;
    private TextView txtViewLimit;
    private int limit;

    private ProductAdapter productAdapter;
    private AutoCompleteTextView productNameSearch;
    private ArrayAdapter<Product> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignComponentsToVariables();
        init();
        try {
            limit = getIntent().getExtras().getInt("limit");
            txtViewLimit.setText(limit + ""); // Set LIMIT textview to the number I set in ProductPicker.
            // getIntent().removeExtra("txtViewLimit");
        } catch (NullPointerException e) {
            Log.e(lib.JOMAex, e.toString());
            txtViewLimit.setText(limit + ""); // Set LIMIT textview to the number I set in ProductPicker.
        }
        autoComplete();
    }



    /*  Here I get the intent with the product and position that I edited, and try to update it
        in shopping list, sort it and then notifydataset.there might be no intent so NullPointer.
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //on resume happens when I return from Editproduct
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case lib.ADD_PRODUCT:
                try {
                    int editedPosition = data.getExtras().getInt("position");
                    data.removeExtra("position");
                    Product editedProduct = (Product) data.getSerializableExtra("product");
                    getIntent().removeExtra("product");
                    if (editedPosition == -1) addProductToList(editedProduct);
                    else shoppingList.set(editedPosition, editedProduct);
                    sortShoppingList();

                    productAdapter.notifyDataSetChanged();
                } catch (NullPointerException e) {
                    Log.e(lib.JOMAex, e.toString());
                } catch (Exception e) {
                    Log.e(lib.JOMAex, "exception on resume main" + e.toString());
                }
                txtViewLimit.setText(limit + ""); // Set LIMIT textview to the number I set in ProductPicker.
                updateTotalPrice();
            case 2:
                Toast.makeText(MainActivity.this, ""+data.getExtras().get("barcode"), Toast.LENGTH_SHORT).show();

        }
    }

    //Button Add Product

    public void newProduct(View view) {
        Intent intent = new Intent(this, ProductActivity.class);
        startActivityForResult(intent, lib.ADD_PRODUCT);
    }

    //Button I'm done
    public void buttonDone(View view) {
        ShoppingCart groceries = new ShoppingCart(shoppingList, ShoppingListHolder.getTotalPrice(), new Date());
        for (Product product : groceries.getShoppingList()) {
            product.save();
        }
        ShoppingListHolder.deleteContent();
        Intent intent = new Intent(this, ResultOfShopping.class);
        intent.putExtra("groceries", groceries);
        intent.putExtra("totalprice", textViewTotalPrice.getText().toString());
        intent.putExtra("limit", txtViewLimit.getText().toString());
        finish();
        startActivity(intent);
    }

    //Button Scan Barcode
    public void btnScanBarcode(View view) {
        Intent intent = new Intent(this, CameraTestActivity.class);
        startActivityForResult(intent, lib.SCAN_BARCODE);
    }

    /**
     * If I delete product in ProductAdapter, through interface I will get this message
     * Then I just update everythiung     *
     *
     * @param value - how much the value changed.
     * @return
     */
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
        Collections.sort(shoppingList, new ProductComparator());
    }

    /**
     * Here I get the message and update stuff in listview ..
     *
     * @param changedProduct product that I will recevie trough interface
     * @param position       the same with position
     * @return
     */
    @Override
    public Product productToEdit(Product changedProduct, int position) {
        shoppingList.set(position, changedProduct);
        updateTotalPrice();
        sortShoppingList();
        ListViewShopping.setAdapter(productAdapter);
        return null;
    }

    private boolean addProductToList(final Product product) {
        this.shoppingList.add(product);
        this.productNameSearch.setText("");
        this.productAdapter.notifyDataSetChanged();
        return true;
    }

    private void assignComponentsToVariables() {
        this.shoppingList = ShoppingListHolder.getInstance().getShoppingList();
        this.productNameSearch = (AutoCompleteTextView) findViewById(R.id.editTextProductNameSearch);
        productNameSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productNameSearch.setText("");
            }
        });
        // display currency symbols. These are those two in the top.
        this.currencySymbol1 = (TextView) findViewById(R.id.currencySymbol1);
        this.currencySymbol2 = (TextView) findViewById(R.id.currencySymbol2);
        this.currencySymbol1.setText(lib.CurrencySymbol());
        this.currencySymbol2.setText(lib.CurrencySymbol());
        //txtViewLimit text view
        this.txtViewLimit = (TextView) findViewById(R.id.wantedToSpend);
        //total price
        this.textViewTotalPrice = (TextView) findViewById(R.id.textViewTotalPrice);
        //assign listview, sort it, set adapter.
        ListViewShopping = (ListView) findViewById(R.id.ShoppingList);
    }

    private void init() {
        // set adapter
        productAdapter = new ProductAdapter(this, shoppingList, this);
        ListViewShopping.setAdapter(productAdapter);
    }

    private void autoComplete() {
        List<Product> queryResult = Product.findWithQuery(Product.class, "SELECT DISTINCT name,price FROM Product");
        this.adapter = new ArrayAdapter<Product>(this, android.R.layout.simple_list_item_1, queryResult);
        this.productNameSearch.setAdapter(this.adapter);
        this.productNameSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addProductToList(adapter.getItem(position));
                updateTotalPrice();
            }
        });
    }
}