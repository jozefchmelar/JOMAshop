package com.joma.jomashop;

import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Button;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;

import android.widget.TextView;
import android.graphics.ImageFormat;


public class ProductActivity extends AppCompatActivity {

    private Product product;
    private int position;
    private String barcode;
    private EditText productName;
    private EditText productPrice;
    private CheckBox productIsFavourite;
    private Button scanBarcode;
    private TextView textViewBarcode;
    //camera
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    public ProductActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        initializeComponents();
        // I'm trying to get intent in case that user decided to edit product insted of creating a new one
        position = -1;
        try {
            this.product = (Product) getIntent().getSerializableExtra("product");
            this.position = getIntent().getExtras().getInt("position");
            this.barcode = getIntent().getExtras().getString("barcode");
            textViewBarcode.setText(this.barcode);

            Toast.makeText(ProductActivity.this, "product "+this.barcode, Toast.LENGTH_SHORT).show();
            getIntent().removeExtra("product");
            getIntent().removeExtra("position");
            getIntent().removeExtra("barcode");

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
        setResult(1, intent);
        finish();
    }

    private void initializeComponents() {
        productName = (EditText) findViewById(R.id.editTextProductName);
        productName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName.setText("");
            }
        });
        productPrice = (EditText) findViewById(R.id.editTextPrice);
        productIsFavourite = (CheckBox) findViewById(R.id.checkBoxFavourite);
        scanBarcode = (Button) findViewById(R.id.buttonScanBarcode);
        textViewBarcode = (TextView) findViewById(R.id.textViewBarcode);
    }

    private Product createProductFromDatafields() {
        // I'm just gettting the data from fields and creating a new Product object.
        boolean ok = true;
        String productName = this.productName.getText().toString();
        double price = 0;//get price from product price textarea
        try {
            price = Double.parseDouble(productPrice.getText().toString());
        } catch (NumberFormatException e) {
            Log.e(lib.JOMAex, e.toString());
            ok = false;
        }
        int quantity = -1;
        try {
            quantity = this.product.getQuantity();
        } catch (NullPointerException e) {
            quantity = 1;
        }
        boolean visibleSettings = false;
        boolean favourite = productIsFavourite.isChecked();

        if (!ok)
            Toast.makeText(ProductActivity.this, "Please insert correct data", Toast.LENGTH_SHORT).show();
        return new Product(productName, price, quantity, visibleSettings, favourite, this.barcode);
    }

    private void fillFieldsWithDataFromIntent(Product product) {
        // I use this method when I get some data from intent.
        productName.setText(product.getName());
        productPrice.setText(product.getPrice() + "");
        textViewBarcode.setText(this.barcode);
    Toast.makeText(ProductActivity.this, "FILL"+this.barcode, Toast.LENGTH_SHORT).show();
        productIsFavourite.setChecked(product.isFavourite());
    }

    public void buttonScanBarcode (View view){
        Intent intent = new Intent(this, CameraTestActivity.class);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        this.barcode=data.getExtras().getString("barcode");
        Toast.makeText(ProductActivity.this, ""+barcode, Toast.LENGTH_SHORT).show();
        textViewBarcode.setText(barcode);
    }
}
