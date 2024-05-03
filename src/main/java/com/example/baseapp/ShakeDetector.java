package com.example.baseapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class ShakeDetector extends AppCompatActivity implements SensorEventListener {
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 50;

    private TextView shakeCountTextView;
    private int shakeCount = 0;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long mShakeTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_count_activity);

        shakeCountTextView = findViewById(R.id.shakeCountTextView);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        registerSensor();

        updateShakeCountUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterSensor();
    }

    private void registerSensor() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    private void unregisterSensor() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;
        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            final long now = System.currentTimeMillis();
            if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                return;
            }

            mShakeTimestamp = now;
            shakeCount++;
            updateShakeCountUI();

            // 흔들림을 감지할 때마다 진동을 울림
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(50); // 0.05초간 진동
            }
        }
    }

    private void updateShakeCountUI() {
        shakeCountTextView.setText("Shake Count: " + shakeCount);
    }
}
