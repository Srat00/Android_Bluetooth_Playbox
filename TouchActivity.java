package com.example.opensourceproject;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TouchActivity extends AppCompatActivity {
    TextView timerTextView;
    TextView countTextView;
    CountDownTimer countDownTimer;
    int touchCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);

        timerTextView = findViewById(R.id.timerTextView);
        countTextView = findViewById(R.id.countTextView);
        View purpleArea = findViewById(R.id.purpleArea);
        Button meButton = findViewById(R.id.meButton);
        Button youButton = findViewById(R.id.youButton);

        // Me 버튼 클릭 시
        meButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        // You 버튼 클릭 시
        youButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        // 보라색 구역 터치 시
        purpleArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchCount++;
                    updateTouchCount();
                }
                return true;
            }
        });
    }

    // 타이머 시작
    private void startTimer() {
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("남은 시간: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                // 타이머 종료 시
                timerTextView.setText("남은 시간: 0");
                updateTouchCount();
            }
        }.start();
    }

    // 터치 횟수 업데이트
    private void updateTouchCount() {
        countTextView.setText("터치 횟수: " + touchCount);
    }
}
