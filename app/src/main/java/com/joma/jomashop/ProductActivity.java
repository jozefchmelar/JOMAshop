package com.joma.jomashop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ProductActivity extends AppCompatActivity {
    private Product product;
    private int position;
    private EditText productName;
    private EditText productPrice;
    private CheckBox productIsFavourite;
    private Button scanBarcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        initializeComponents();
        try { // I'm trying to get intent in case that user decided to edit product insted of creating a new one
            product = (Product) getIntent().getSerializableExtra("product");
            position = getIntent().getExtras().getInt("position");
            fillFieldsWithDataFromIntent(product);
        } catch (NullPointerException e) {
            Log.e(lib.JOMAex, e.toString());
        }
    }


    public void onClickSave(View view) {
        //sending intent 'n stuff
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("product", createProductFromDatafields());
        intent.putExtra("position", position);
        startActivity(intent);
    }


    private void initializeComponents() {
        productName = (EditText) findViewById(R.id.editTextProductName);
        productPrice = (EditText) findViewById(R.id.editTextPrice);
        productIsFavourite = (CheckBox) findViewById(R.id.checkBoxFavourite);
        scanBarcode = (Button) findViewById(R.id.buttonScanBarcode);
        //save = (Button) findViewById(R.id.buttonSave);
    }

    private Product createProductFromDatafields() {
        // I'm just gettting the data from fields and creating a new Product object.
        boolean ok= true;
        String ProductName = productName.getText().toString();
        double Price=0;
        try {
             Price = Double.parseDouble(productPrice.getText().toString());
        } catch (NumberFormatException e) {
            Log.e(lib.JOMAex, e.toString());
            ok=false;
        }
        int Quantity = product.getQuantity();
        boolean VisibleSettings = product.isVisibleSettings();
        boolean Favourite = productIsFavourite.isChecked();
        String Barcode = "";
        if(!ok) Toast.makeText(ProductActivity.this, "Please insert correct data", Toast.LENGTH_SHORT).show();

        return new Product(ProductName, Price, Quantity, VisibleSettings, Favourite, Barcode);
    }

    private void fillFieldsWithDataFromIntent(Product product) {
        // I use this method when I get some data from intent.
        productName.setText(product.getProductName());
        productPrice.setText(product.getPrice() + "");
        productIsFavourite.setChecked(product.isFavourite());

    }
}
