package com.example.velmurugan.getcurrentspeedandroidexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FirstActivity extends AppCompatActivity {

    EditText etFixedPrice,etChargePerKm,etWaitingCharge;
    Button btSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        btSet=findViewById(R.id.btSet);

        btSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MainAct = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(MainAct);
            }
        });
    }
}
