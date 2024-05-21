package com.example.baseapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class Compass extends AppCompatActivity implements CompassSensor.CompassListener {
    private CompassSensor compassSensor;
    private TextView azimuthView;
    private TextView targetAzimuthView;
    private TextView timerView;
    private Button startButton;

    private int targetAzimuth;
    private long startTime;
    private boolean isTargetSet = false;
    private boolean isWithinRange = false;
    private Handler handler = new Handler();
    private Handler rangeHandler = new Handler();
    private Runnable rangeCheckRunnable;

    private static final int RANGE_DURATION_MS = 1000; // 1 second
    private static final int RANGE_THRESHOLD_DEGREES = 5; // 5 degrees

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        azimuthView = findViewById(R.id.azimuth_text);
        targetAzimuthView = findViewById(R.id.target_azimuth_text);
        timerView = findViewById(R.id.timer_text);
        startButton = findViewById(R.id.start_button);

        compassSensor = new CompassSensor(this, this);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
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
        handler.removeCallbacks(timerRunnable);
        rangeHandler.removeCallbacks(rangeCheckRunnable);
    }

    @Override
    public void onNewAzimuth(float azimuth) {
        azimuthView.setText("Azimuth: " + azimuth + " degrees");
        if (isTargetSet) {
            if (Math.abs(azimuth - targetAzimuth) < RANGE_THRESHOLD_DEGREES) {
                if (!isWithinRange) {
                    isWithinRange = true;
                    startRangeCheck();
                }
            } else {
                isWithinRange = false;
                rangeHandler.removeCallbacks(rangeCheckRunnable);
            }
        }
    }

    private void startGame() {
        setNewTargetAzimuth();
        startTime = System.currentTimeMillis();
        updateTimer();
        startButton.setVisibility(View.GONE); // Hide the start button
        targetAzimuthView.setVisibility(View.VISIBLE);
        timerView.setVisibility(View.VISIBLE);
    }

    private void setNewTargetAzimuth() {
        Random random = new Random();
        targetAzimuth = random.nextInt(360);
        targetAzimuthView.setText("Target: " + targetAzimuth + " degrees");
        isTargetSet = true;
        isWithinRange = false;
    }

    private void updateTimer() {
        handler.post(timerRunnable);
    }

    private void startRangeCheck() {
        rangeCheckRunnable = new Runnable() {
            @Override
            public void run() {
                if (isWithinRange) {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    timerView.setText("Time: " + elapsedTime / 1000.0 + " seconds");
                    isTargetSet = false;
                    startButton.setVisibility(View.VISIBLE); // Show the start button again
                    targetAzimuthView.setVisibility(View.GONE);
                    timerView.setVisibility(View.GONE);
                }
            }
        };
        rangeHandler.postDelayed(rangeCheckRunnable, RANGE_DURATION_MS);
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isTargetSet) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                timerView.setText("Time: " + elapsedTime / 1000.0 + " seconds");
                handler.postDelayed(this, 100);
            }
        }
    };
}
