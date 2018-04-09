package com.example.velmurugan.getcurrentspeedandroidexample;

import java.math.BigDecimal;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements GPSCallback{
    private GPSManager gpsManager = null;
    private double speed = 0.0,fixedPrice,waitingCharge,chargePerKm, totalCost;
    private String totalCostStr;
    Boolean isGPSEnabled=false;
    LocationManager locationManager;
    double currentSpeed,kmphSpeed,currentDistance;
    EditText etFixedPrice,etWaitingCharge,etRunningCost;
    private TextView txtview;

    long timeSwap = 0L;
    long timeInMillies = 0L;
    private long startTime = 0L;
    long finalTime = 0L;
    int minutes,seconds;
    private TextView textTimer;
    private TextView textFare;

    private Location prevLocation;
    private double distance = 0.0;
    private double distance1 = 0.0;
    private TextView distance_textView;
    private Handler myHandler = new Handler();

    int baseFareR;
    int chargePerKmR;

    int waitChargeR;

    //VarTimer


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baseFareR= getIntent().getExtras().getInt("baseFareI");
        chargePerKmR=getIntent().getExtras().getInt("chargePerKmI");
        waitChargeR=getIntent().getExtras().getInt("waitChargeI");


        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        txtview = findViewById(R.id.info);
        distance_textView = findViewById(R.id.infoDistance);
        textTimer = findViewById(R.id.textTimer);
        textFare=findViewById(R.id.textFare);
//        etFixedPrice=findViewById(R.id.etFixedPrice);
//        etWaitingCharge=findViewById(R.id.etWaitingCharge);
//        etRunningCost=findViewById(R.id.etChargePerKm);
    }

    public void getCurrentSpeed(View view){
        txtview.setText(getString(R.string.info));
       distance_textView.setText(getString(R.string.info));
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        gpsManager = new GPSManager(MainActivity.this);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled) {
            gpsManager.startListening(getApplicationContext());
            gpsManager.setGPSCallback(this);
        } else {
            gpsManager.showSettingsAlert();
        }

//        fixedPrice = Double.valueOf(etFixedPrice.getText().toString());
//        waitingCharge = Double.valueOf(etWaitingCharge.getText().toString());
//        runningCost= Double.valueOf(etRunningCost.getText().toString());
//        totalCost = 0.0;
    }
    ///Test for Distance

    private void updateDistance(Location location) {
        //distance_textView.setText(getString(R.string.infoDistance));
        try {
            double distanceToLast = location.distanceTo(prevLocation);
            //if less than 10 metres, do not record
            distance += distanceToLast;
            distance1 = distance / 1000;
            //Toast.makeText(this, distance + "", Toast.LENGTH_LONG).show();
            distance_textView.setText(distance1+"Km");
            prevLocation = location;
        }catch (NullPointerException ex){
            prevLocation = location;
            ex.printStackTrace();
        }
    }




    @Override
    public void onGPSUpdate(Location location) {

        if (location != null) {
            updateDistance(location);
            speed = location.getSpeed();
            currentSpeed = round(speed,3,BigDecimal.ROUND_HALF_UP);
            kmphSpeed = round((currentSpeed*3.6),3,BigDecimal.ROUND_HALF_UP);
        }
        //fff
        distance1= round(distance1,3,BigDecimal.ROUND_HALF_UP);

        //time

        txtview.setText(kmphSpeed+"km/h");
        startTime = SystemClock.uptimeMillis();

        if (kmphSpeed == 0.0) {
            timeSwap += timeInMillies;
            myHandler.postDelayed(updateTimerMethod, 0);
        } else {
            startTime = SystemClock.uptimeMillis();
            myHandler.removeCallbacks(updateTimerMethod);
        }
        //Log.d("Status", "Hi");
        totalCost=baseFareR + distance1*chargePerKmR + minutes*waitChargeR + (seconds/60)*waitChargeR;
        textFare.setText(String.format("%s", totalCost));
    }



    @Override
    protected void onDestroy() {
        gpsManager.stopListening();
        gpsManager.setGPSCallback(null);
        gpsManager = null;
        super.onDestroy();
    }

    public static double round(double unrounded, int precision, int roundingMode) {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }

    private Runnable updateTimerMethod = new Runnable() {

        public void run() {
            timeInMillies = SystemClock.uptimeMillis() - startTime;
            finalTime = timeSwap + timeInMillies;

            seconds = (int) (finalTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (finalTime % 1000);

            textTimer.setText(String.format("%d:%s:%s", minutes, String.format("%02d", seconds),String.format("%03d",milliseconds)));
            myHandler.postDelayed(this, 0);
        }
    };
}
