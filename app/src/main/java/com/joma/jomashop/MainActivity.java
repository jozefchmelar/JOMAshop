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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DataTransferInterface {
    private ArrayList<Product> shoppingList;
    private ListView ListViewShopping;
    private TextView textViewTotalPrice;
    private TextView currencySymbol1;
    private TextView currencySymbol2;

    private ProductAdapter productAdapter;
    private AutoCompleteTextView productNameSearch;
    private ArrayAdapter<Product> adapter;
    private int limit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            this.limit = getIntent().getExtras().getInt("limit");

        } catch (NullPointerException e) {
            Log.e(lib.JOMAex, e.toString());
        }
        init();
        TextView limit = (TextView) findViewById(R.id.wantedToSpend);
        limit.setText(this.limit + "");
        autoComplete();
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

            shoppingList.set(editedPosition, editedProduct);
            sortShoppingList();
            updateTotalPrice();
            productAdapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            Log.e(lib.JOMAex, e.toString());
        } catch (Exception e) {
            Log.e(lib.JOMAex, "this should never happen");
        }
    }

    //Button Add Product
    public void newProduct(View view) {
        shoppingList.add(new Product(lib.TESTING_OBJECT));
        updateTotalPrice();
        this.productAdapter.notifyDataSetChanged();
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
        intent.putExtra("totalprice", ShoppingListHolder.getTotalPrice());
        intent.putExtra("limit", this.limit);
        startActivity(intent);
    }

    //Button Scan Barcode
    public void btnScanBarcode(View view) {
        Intent intent = new Intent(this, Scanner.class);
        startActivityForResult(intent, 0);
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
        // display currency symbols. These are those two in the top.
        this.currencySymbol1 = (TextView) findViewById(R.id.currencySymbol1);
        this.currencySymbol2 = (TextView) findViewById(R.id.currencySymbol2);
        this.currencySymbol1.setText(lib.CurrencySymbol());
        this.currencySymbol2.setText(lib.CurrencySymbol());
        //total price
        this.textViewTotalPrice = (TextView) findViewById(R.id.textViewTotalPrice);
        //assign listview, sort it, set adapter.
        ListViewShopping = (ListView) findViewById(R.id.ShoppingList);
    }

    private void init() {
        assignComponentsToVariables();
        sortShoppingList();
        updateTotalPrice();
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
            }
        });
    }


}
