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
        // I'm trying to get intent in case that user decided to edit product insted of creating a new one
        try {
            this.product = (Product) getIntent().getSerializableExtra("product");
            this.position = getIntent().getExtras().getInt("position");
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
        String productName = this.productName.getText().toString();
        double price = 0;//get price from product price textarea
        try {
            price = Double.parseDouble(productPrice.getText().toString());
        } catch (NumberFormatException e) {
            Log.e(lib.JOMAex, e.toString());
            ok=false;
        }
        int quantity = product.getQuantity();
        boolean visibleSettings = product.isVisiblesettings();
        boolean favourite = productIsFavourite.isChecked();
        String barcode = "";
        if(!ok) Toast.makeText(ProductActivity.this, "Please insert correct data", Toast.LENGTH_SHORT).show();
        return new Product(productName, price, quantity, visibleSettings, favourite, barcode);
    }

    private void fillFieldsWithDataFromIntent(Product product) {
        // I use this method when I get some data from intent.
        productName.setText(product.getName());
        productPrice.setText(product.getPrice() + "");
        productIsFavourite.setChecked(product.isFavourite());

    }
}
