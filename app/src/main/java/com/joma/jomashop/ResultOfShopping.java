package com.joma.jomashop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ResultOfShopping extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_of_shopping);
        //okey tu nastavim nejake sracky
        TextView txtreallySpend = (TextView) findViewById(R.id.reallySpend );
        TextView txtLimit = (TextView) findViewById(R.id.resultLimit);
        txtreallySpend.setText(getIntent().getExtras().getString("totalprice"));
        txtLimit.setText(getIntent().getExtras().getString("limit"));
    }

    public void btnClose(View view){
        finish();
        Toast.makeText(ResultOfShopping.this, "Thank you for using JOMAshop! :)", Toast.LENGTH_SHORT).show();
        System.exit(0);//exit.
    }
    public void btnContinue(View view) {
        Intent intent = new Intent(this, StartActivity.class);
        finish();
        startActivity(intent);
    }
}
