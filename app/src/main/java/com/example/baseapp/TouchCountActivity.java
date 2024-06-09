package com.example.baseapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TouchCountActivity extends AppCompatActivity {

    private int touchCount = 0;
    private TextView touchCountTextView;
    private TextView touchText;
    private View touchAreaView;
    private Button touchStartBtn;
    private long startTime;

    private boolean isStart = false;
    private Handler timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_count);

        touchCountTextView = findViewById(R.id.touchCountTextView);
        touchAreaView = findViewById(R.id.touchAreaView);
        touchStartBtn = findViewById(R.id.touchStartBtn);
        touchText = findViewById(R.id.SmashText);

        touchStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭 이벤트 처리
                touchStart();
            }
        });

    }

    private void touchStart()
    {
        if (isStart) {
            return; // 이미 타이머가 실행 중이면 다시 시작하지 않음
        }
        touchStartBtn.setVisibility(View.GONE);
        touchCount = 0;
        startTime = System.currentTimeMillis();

        timerHandler.postDelayed(stopDetection, 10000);
        timerHandler.post(updateTimer); // Start the timer update runnable

        touchCountTextView.setText("Touch Count: 0") ;

        isStart = true;
    }

    private Runnable stopDetection = new Runnable() {
        @Override
        public void run()
        {
            isStart = false;
            touchStartBtn.setText("시작");
            touchStartBtn.setVisibility(View.VISIBLE);
            touchText.setText("제한시간 내에 최대한 많이 화면을 누르세요!");
            touchText.setTextColor(Color.parseColor("#000000"));
        }
    };

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            if (isStart) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long remainingTime = 10000 - elapsedTime;

                //터치 카운트 증가
                touchAreaView.setOnTouchListener((v, event) -> {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (isStart) {
                                // 화면 터치 시 횟수 증가
                                touchCount++;
                                // TextView 업데이트
                                touchCountTextView.setText("Touch Count: " + touchCount);
                                break;
                            }

                    }
                    return true;
                });

                if (remainingTime > 0)
                {
                    touchText.setText("부셔!!");
                    touchText.setTextColor(Color.parseColor("#ff0000"));
                    timerHandler.postDelayed(this, 100);
                }
            }
        }
    };




}
