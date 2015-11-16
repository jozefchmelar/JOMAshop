package com.joma.jomashop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ResultOfShopping extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_of_shopping);
        //okey tu nastavim nejake sracky
        //TODO zachytit intent,pridat data, nabindovat komponenty
    }

    public void btnContinue(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
