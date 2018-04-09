package com.example.velmurugan.getcurrentspeedandroidexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FirstActivity extends AppCompatActivity {

    EditText etFixedPrice, etChargePerKm, etWaitingCharge;
    Button btSet;
    int baseFareI, chargePerKmI, waitChargeI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        btSet = findViewById(R.id.btSet);
        etFixedPrice = findViewById(R.id.etFixedPrice);
        etChargePerKm = findViewById(R.id.etChargePerKm);
        etWaitingCharge = findViewById(R.id.etWaitingCharge);


        btSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent MainAct = new Intent(FirstActivity.this, MainActivity.class);
//                startActivity(MainAct);
                Intent MainAct = new Intent(FirstActivity.this, MainActivity.class);
                //intent.putExtra ( "TextBox", editText.getText().toString() );

                baseFareI = Integer.parseInt(etFixedPrice.getText().toString());
                chargePerKmI = Integer.parseInt(etChargePerKm.getText().toString());
                waitChargeI = Integer.parseInt(etWaitingCharge.getText().toString());

                MainAct.putExtra("baseFareI", baseFareI);
                MainAct.putExtra("chargePerKmI", chargePerKmI);
                MainAct.putExtra("waitChargeI", waitChargeI);
                startActivity(MainAct);
            }
        });
    }
}
