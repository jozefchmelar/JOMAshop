package com.joma.jomashop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ResultOfShopping extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_of_shopping);
        //okey tu nastavim nejake sracky
        //TODO zachytit intent,pridat data, nabindovat komponenty
        double total = (double) getIntent().getExtras().getDouble("totalprice");
        int limit = getIntent().getExtras().getInt("limit");
        TextView txtTotal = (TextView) findViewById(R.id.reallySpend);
        TextView txtLimit = (TextView) findViewById(R.id.wantedToSpend);
        txtTotal.setText(total + "");
        txtLimit.setText(limit + "");
    }

    public void btnContinue(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
