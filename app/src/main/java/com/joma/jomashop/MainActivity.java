package com.joma.jomashop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity  {

    private ArrayList<Product> ShoppingList = ShoppingListHolder.getInstance().getShoppingList();
    private ListView ListViewShopping;
    private TextView textViewTotalPrice;
    private TextView currencySymbol1;
    private TextView currencySymbol2;
    private ProductAdapter productAdapter;
    @Override
    public void onResume() {
        super.onResume();  //on resume happens when I return from Editproduct
        try {
            //getting the intent
            int editedPosition = getIntent().getExtras().getInt("position");
            Product editedProduct = (Product) getIntent().getSerializableExtra("product");
            ShoppingList.set(editedPosition, editedProduct);
            Collections.sort(ShoppingList, new ProductComparator()); //sorting ArrayList but if I get no intent it wont sort anything
            productAdapter.notifyDataSetChanged();
         } catch (NullPointerException e) {
            Log.e(lib.JOMAex, e.toString());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // display currency symbols.
        currencySymbol1 = (TextView) findViewById(R.id.currencySymbol1);
        currencySymbol1.setText(lib.CurrencySymbol());
        currencySymbol2 = (TextView) findViewById(R.id.currencySymbol2);
        currencySymbol2.setText(lib.CurrencySymbol());
        // set adapter
         productAdapter=new ProductAdapter(this, ShoppingList);
        //assign listview, sort it, set adapter.
        ListViewShopping = (ListView) findViewById(R.id.ShoppingList);
        Collections.sort(ShoppingList, new ProductComparator());
        ListViewShopping.setAdapter(productAdapter);
        textViewTotalPrice = (TextView) findViewById(R.id.textViewTotalPrice);

    }

    public void newProduct(View view) {
        Product newProduct = new Product();
        ShoppingList.add(newProduct);
        textViewTotalPrice.setText(lib.addValueToTextView(textViewTotalPrice,newProduct.getTotalPrice()));
        //sets price to the total price of the product.

        ListViewShopping.setAdapter(productAdapter);
    }




}
