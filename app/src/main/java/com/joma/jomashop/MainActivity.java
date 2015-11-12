package com.joma.jomashop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataTransferInterface {

    private ArrayList<Product> ShoppingList;
    private ListView ListViewShopping;
    private TextView textViewTotalPrice;
    private TextView currencySymbol1;
    private TextView currencySymbol2;
    private ProductAdapter productAdapter;
    private AutoCompleteTextView productNameSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShoppingList = ShoppingListHolder.getInstance().getShoppingList();
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
        updateTotalPrice();
        ListViewShopping.setAdapter(productAdapter);

        //autocomplete
        AutoCompleteTextView productNameSearch = (AutoCompleteTextView) findViewById(R.id.editTextProductNameSearch);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getAllProductNames());
        // Set adapter data to TextView
        productNameSearch.setAdapter(adapter);


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
        Product newProduct = new Product(lib.TestingObject); //true means, it's a testproduct
        ShoppingList.add(newProduct);
        updateTotalPrice();
        ListViewShopping.setAdapter(productAdapter);
    }

    public void buttonDone(View view) {

        ShoppingCart groceries = new ShoppingCart(ShoppingList, ShoppingListHolder.getTotalPrice(), new Date());
        for (Product product : groceries.getShoppingList()) {
            product.save();
        }
        ShoppingListHolder.deleteContent();
        Intent intent = new Intent(this, ResultOfShopping.class);
        intent.putExtra("groceries", groceries);
        startActivity(intent);

        Toast.makeText(MainActivity.this, "DONE! products added to DB", Toast.LENGTH_SHORT).show();
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

    /**
     * @return String[] of all product names in DB
     */
    private String[] getAllProductNames() {
        //Create AL to get all products from query.Then get all the names to arraylist

        List<Product> products = new ArrayList<>();
        products = Product.findWithQuery(Product.class, "SELECT DISTINCT productname FROM Product");
        ArrayList<String> productnames = new ArrayList<>();
        try {
            for (Product product :
                    products) {
                productnames.add(product.getProductname()); //getting all the names
            }
        } catch (NullPointerException e) {
            Log.e(lib.JOMAex, e.toString());
        }
        //convert to String[] because that's what adapter needs and then return it.
        String[] names = productnames.toArray(new String[productnames.size()]);//converting to array
        return names;
    }

    /**
     * Here I get the message and update stuff in listview ...
     *
     * @param changedProduct product that I will recevie trough interface
     * @param position       the same with position
     * @return
     */
    @Override
    public Product productToEdit(Product changedProduct, int position) {
        ShoppingList.set(position, changedProduct);
        updateTotalPrice();
        sortShoppingList();
        ListViewShopping.setAdapter(productAdapter);
        return null;
    }

}
