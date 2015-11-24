package com.joma.jomashop;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.TransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.app.Dialog;
import android.widget.SeekBar;


public class StartActivity extends AppCompatActivity {
    private NumberPicker picker;
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
        picker.setMinValue(0);
        picker.setMaxValue(100);


        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        //

    }

    private void launchIntent() {
        Intent it = new Intent(this, MainActivity.class);
        it.putExtra("limit", picker.getValue());
        startActivity(it);
    }
}
