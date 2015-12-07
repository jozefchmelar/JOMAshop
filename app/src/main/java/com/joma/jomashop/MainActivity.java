package com.joma.jomashop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import com.squareup.seismic.ShakeDetector;


public class MainActivity extends AppCompatActivity implements DataTransferInterface {

    private int limit;
    private ArrayList<Product> shoppingList;
    private ProductAdapter productAdapter;
    private AutoCompleteTextView productNameSearch;
    private ArrayAdapter<Product> adapter;
    private ListView ListViewShopping;
    private TextView textViewTotalPrice;
    private TextView currencySymbol1;
    private TextView currencySymbol2;
    private TextView txtViewLimit;



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignComponentsToVariables();
        init();
        try {
            limit = getIntent().getExtras().getInt("limit");
            txtViewLimit.setText(limit + "");
        } catch (NullPointerException e) {
            Log.e(lib.JOMAex, e.toString());
            txtViewLimit.setText(limit + ""); // Set LIMIT textview to the number I set in ProductPicker.
        }
        autoComplete();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /*  Here I get the intent with the product and position that I edited, and try to update it
        in shopping list, sort it and then notifydataset.there might be no intent so NullPointer.
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case lib.ADD_PRODUCT:
                try {
                    int editedPosition = data.getExtras().getInt("position");
                    Product editedProduct = (Product) data.getSerializableExtra("product");
                    // if I add new product add it to the end of list.
                    // but if i edit product (pos=-1) change the current one
                    if (editedPosition == -1 || shoppingList.size()==0)
                        addProductToList(editedProduct);
                    else
                        shoppingList.set(editedPosition, editedProduct);

                    productAdapter.notifyDataSetChanged();
                } catch (NullPointerException e) {
                    Log.e(lib.JOMAex, e.toString());
                }

                updateTotalPrice();
                break;
            case lib.SCAN_BARCODE:
                final String query = "SELECT DISTINCT name,price FROM Product WHERE barcode=" + data.getExtras().getString("barcode");
                final List<Product> queryResult = Product.findWithQuery(Product.class, query);
                if (queryResult.isEmpty()) {
                    Intent intent = new Intent(this, ProductActivity.class);
                    final String code = data.getExtras().getString("barcode");
                    intent.putExtra("barcode", code);
                    startActivityForResult(intent, lib.ADD_PRODUCT);
                } else {
                    addProductToList(queryResult.get(queryResult.size() - 1));
                }
                break;
        }
        getIntent().removeExtra("product");
        getIntent().removeExtra("position");
        getIntent().removeExtra("barcode");
        txtViewLimit.setText(limit + ""); // Set LIMIT textview to the number I set in ProductPicker.
    }

    //Button Add Product
    public void newProduct(View view) {
        Intent intent = new Intent(this, ProductActivity.class);
        startActivityForResult(intent, lib.ADD_PRODUCT);
    }

    //Button Scan Barcode
    public void btnScanBarcode(View view) {
        Intent intent = new Intent(this, CameraTestActivity.class);
        startActivityForResult(intent, lib.SCAN_BARCODE);
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
        intent.putExtra("limit", limit);
        Gson gson = new Gson();
        String json = gson.toJson(groceries);
        Log.w("json", json.toString());

        finish();

        startActivity(intent);
    }

    /**
     * If I delete product in ProductAdapter, through interface I will get this message
     * Then I just update everythiung
     *
     * @param value - how much the value changed.
     * @return
     */
    @Override
    public double deletedProductValue(double value) {
        updateTotalPrice();
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
        ListViewShopping.setAdapter(productAdapter);
        return null;
    }

    private boolean addProductToList(final Product product) {
        // if there's the same product it will increase the quantity instead of adding new product.
        for (Product p : this.shoppingList) {
            if (p.equalsTo(product)) {
                p.setQuantity(p.getQuantity() + 1);
                this.productAdapter.notifyDataSetChanged();
                updateTotalPrice();
                return true;
            }
        }
        this.shoppingList.add(product);
        this.productNameSearch.setText("");
        this.productAdapter.notifyDataSetChanged();
        updateTotalPrice();
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

    @Override
    public void onBackPressed() {
        //   super.onBackPressed();
        AlertDialog.Builder AlertBuilder = new AlertDialog.Builder(this);
        AlertBuilder
                .setTitle("Change limit or leave?")
                .setPositiveButton("Change Limit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeLimitDialog();
                    }
                })
                .setNegativeButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);//exit.
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = AlertBuilder.create();
        alert.show();
    }

    /**
     * Dialog that will have a value picker
     * if user confirms, limit will change to the value in picker
     * otherwise it will close the dialog.
     */
    private void changeLimitDialog() {
        AlertDialog.Builder AlertBuilder = new AlertDialog.Builder(this);
        final NumberPicker picker = new NumberPicker(this);
        int pickerValue = -1;
        picker.setWrapSelectorWheel(true);
        picker.setMinValue(1);
        picker.setMaxValue(100);
        AlertBuilder
                .setTitle("Set limit :)")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        limit = picker.getValue();
                        txtViewLimit.setText(limit + "");

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setView(picker);
        AlertDialog alert = AlertBuilder.create();
        alert.show();
    }



}
