package com.example.baseapp;

import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

public class GyroScope extends AppCompatActivity {


    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeSensorListener;
    private BallView ballView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        super.onCreate(savedInstanceState);
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        ballView = new BallView(this);
        setContentView(ballView);
        ballView.onSizeChanged(screenWidth,screenHeight,640,320);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float angularSpeedX = event.values[0];
                float angularSpeedY = event.values[1];
                ballView.updateBall(angularSpeedX, angularSpeedY);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gyroscopeSensorListener);
    }
}
