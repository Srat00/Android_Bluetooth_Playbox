package com.example.baseapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ShakeDetector extends AppCompatActivity implements SensorEventListener {
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 50;
    private static final int DETECTION_DURATION_MS = 10000; // 10 seconds

    private TextView shakeCountTextView;
    private TextView timerTextView;
    private int shakeCount = 0;
    private int finalShakeCount = 0;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long mShakeTimestamp;
    private boolean isTimerRunning = false;
    private Handler timerHandler = new Handler();
    private Button startButton;

    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_count_activity);

        shakeCountTextView = findViewById(R.id.shakeCountTextView);

        startButton = findViewById(R.id.button1);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startShakeDetectionTimer();
            }
        });

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
        timerHandler.removeCallbacks(stopShakeDetection);
        unregisterSensor();
    }

    private Runnable stopShakeDetection = new Runnable() {
        @Override
        public void run() {
            unregisterSensor();
            isTimerRunning = false;
            finalShakeCount = shakeCount;
            Toast.makeText(ShakeDetector.this, "Shake detection stopped", Toast.LENGTH_SHORT).show();
            startButton.setText("누르면 시작합니다!");
        }
    };

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            if (isTimerRunning) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long remainingTime = DETECTION_DURATION_MS - elapsedTime;
                if (remainingTime > 0) {
                    startButton.setText("Time remaining: " + remainingTime / 1000 + " seconds");
                    timerHandler.postDelayed(this, 100);
                } else {
                    startButton.setText("Time remaining: 0 seconds");
                }
            }
        }
    };

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
        if (!isTimerRunning) {
            return; // 타이머가 종료되면 더 이상 흔들림을 감지하지 않습니다.
        }
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

    private void startShakeDetectionTimer() {
        if (isTimerRunning) {
            return; // 이미 타이머가 실행 중이면 다시 시작하지 않음
        }
        shakeCount = 0;
        updateShakeCountUI();
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(stopShakeDetection, DETECTION_DURATION_MS);
        timerHandler.post(updateTimer); // Start the timer update runnable
        registerSensor();
        isTimerRunning = true;
        Toast.makeText(ShakeDetector.this, "Shake detection started", Toast.LENGTH_SHORT).show();
    }

    private void updateShakeCountUI() {
        shakeCountTextView.setText("Shake Count: " + shakeCount);
    }
}
