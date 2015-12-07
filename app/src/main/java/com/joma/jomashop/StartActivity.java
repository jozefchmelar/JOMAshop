package com.joma.jomashop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;


public class StartActivity extends AppCompatActivity {
    private NumberPicker picker;
    private int pickerValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        picker = new NumberPicker(this);

    }

    public void btn_start(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // numberPicker.
        picker.setWrapSelectorWheel(true);
        picker.setMinValue(1);
        picker.setMaxValue(100);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pickerValue=picker.getValue();
                launchIntent();
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        })
        .setTitle("Set limit :)")
        .setView(picker);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void btnStatistics(View view) {
        Intent intent = new Intent(StartActivity.this, StatisticsActivity.class);
        startActivity(intent);
    }

    private void launchIntent() {
        Intent it = new Intent(this, MainActivity.class);
        it.putExtra("limit", pickerValue);
        startActivity(it);
        finish();
    }
}
