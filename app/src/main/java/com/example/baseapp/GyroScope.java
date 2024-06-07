package com.example.baseapp;

import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Display;
import android.view.ViewTreeObserver;
import androidx.appcompat.app.AppCompatActivity;

public class GyroScope extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeSensorListener;
    private BallView ballView;
    private Handler handler = new Handler();
    private long startTime;
    private int targetCount = 0;
    private long totalTime = 0;
    private boolean isOnTarget = false;
    private long myTime = 0;
    private long enemyTime = 0;
    private static final long GAME_DURATION = 30000; // 30 seconds
    private boolean gameActive = true;

    public long getMyTime() {
        return myTime;
    }

    public void setEnemyTime(long enemyTime) {
        this.enemyTime = enemyTime;
    }

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
        ballView.onSizeChanged(screenWidth, screenHeight, 640, 320);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (!gameActive) return;

                float angularSpeedX = event.values[0];
                float angularSpeedY = event.values[1];
                ballView.updateBall(angularSpeedX, angularSpeedY);
                checkBallPosition();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);

        // Ensure the target is generated after the layout is complete
        ballView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ballView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                generateRandomTarget();
            }
        });

        // Start game timer
        handler.postDelayed(gameTimerRunnable, GAME_DURATION);
    }

    private void checkBallPosition() {
        if (ballView.isBallOnTarget() && !isOnTarget) {
            isOnTarget = true;
            startTime = SystemClock.elapsedRealtime();
            handler.postDelayed(targetRunnable, 1000);
        } else if (!ballView.isBallOnTarget() && isOnTarget) {
            isOnTarget = false;
            handler.removeCallbacks(targetRunnable);
        }
    }

    private Runnable targetRunnable = new Runnable() {
        @Override
        public void run() {
            if (isOnTarget) {
                totalTime += SystemClock.elapsedRealtime() - startTime;
                targetCount++;
                if (targetCount < 5 && gameActive) {
                    generateRandomTarget();
                }
                isOnTarget = false;
            }
        }
    };

    private void generateRandomTarget() {
        int screenWidth = ballView.getWidth();
        int screenHeight = ballView.getHeight();
        int targetX = (int) (Math.random() * screenWidth);
        int targetY = (int) (Math.random() * screenHeight);
        ballView.setTargetPosition(targetX, targetY);
    }

    private void displayTotalTime() {
        if (!gameActive) return;

        gameActive = false;
        myTime = totalTime;
        Intent intent1 = new Intent(this, ResultActivity.class);
        //적의 스코어 설정.
        intent1.putExtra("TOTAL_TIME", myTime);
        intent1.putExtra("ENEMY_TOTAL_TIME", enemyTime);
        startActivity(intent1);

        // Finish the GyroScope activity and return to StartActivity
       // finish();
    }

    private Runnable gameTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (gameActive) {
                displayTotalTime();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gyroscopeSensorListener);
        gameActive = false;
        handler.removeCallbacks(gameTimerRunnable);
        handler.removeCallbacks(targetRunnable);
    }
}
