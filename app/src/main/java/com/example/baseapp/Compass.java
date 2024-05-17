package com.example.baseapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Compass extends AppCompatActivity implements CompassSensor.CompassListener {
    private CompassSensor compassSensor;
    private TextView azimuthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        azimuthView = findViewById(R.id.azimuth_text);
        compassSensor = new CompassSensor(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        compassSensor.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compassSensor.stop();
    }

    @Override
    public void onNewAzimuth(float azimuth) {
        azimuthView.setText("Azimuth: " + azimuth + " degrees");
    }
}
