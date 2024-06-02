package com.example.baseapp;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ShakeDetector extends AppCompatActivity implements SensorEventListener {
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 50;
    private static final int DETECTION_DURATION_MS = 10000; // 10 seconds

    private TextView shakeCountTextView;
    private ImageView animationImageView;
    private int shakeCount = 0;
    private int finalShakeCount = 0;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long mShakeTimestamp;
    private boolean isTimerRunning = false;
    private Handler timerHandler = new Handler();
    private Button startButton;
    private TextView time_alarm;
    private TextView title;
    private long startTime;

    int my_score=0,enemy_score=0;

    public int getScore() {
        return my_score;
    }

    public void setScore(int enemy_score) {
        this.enemy_score = enemy_score;
    }

    public void startAlarm(){
        // Your alarm implementation
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_count_activity);
        this.time_alarm=findViewById(R.id.time_alarm);
        this.title = findViewById(R.id.title);
        shakeCountTextView = findViewById(R.id.shakeCountTextView);
        animationImageView = findViewById(R.id.animationImageView);
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
        startPreGameAnimation();
    }

    private void startPreGameAnimation() {
        animationImageView.setImageResource(R.drawable.phone1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationImageView.setImageResource(R.drawable.phone2);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startPreGameAnimation();
                    }
                }, 500);
            }
        }, 500);
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
            startButton.setText("시작!");
            startPreGameAnimation(); // Restart pre-game animation
        }
    };

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            if (isTimerRunning) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long remainingTime = DETECTION_DURATION_MS - elapsedTime;
                if (remainingTime > 0) {
                    title.setText("흔들어!!");
                    title.setTextColor(Color.parseColor("#ff0000"));
                    time_alarm.setVisibility(View.VISIBLE);
                    time_alarm.setText("남은 시간 : " + remainingTime / 1000 + " 초");
                    timerHandler.postDelayed(this, 100);
                } else {
                    time_alarm.setVisibility(View.GONE);
                    title.setText("쉐킷쉐킷");
                    title.setTextColor(Color.parseColor("#ff0000"));
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

            // 흔들림을 감지할 때마다 이미지 변경 및 진동
            if (shakeCount % 2 == 0) {
                animationImageView.setImageResource(R.drawable.phone3);
            } else {
                animationImageView.setImageResource(R.drawable.phone4);
            }

            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(50); // 0.05초간 진동
            }
        }
    }

    private void startShakeDetectionTimer() {
        startAlarm();
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
        my_score = shakeCount;
        if(isTimerRunning) {
            shakeCountTextView.setText("흔든 횟수: " + shakeCount);
        }
        else{
            shakeCountTextView.setText("시간내에 상대보다 많이 폰을 흔드세요!");

        }
    }
}
