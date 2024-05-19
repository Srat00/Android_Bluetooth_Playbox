package com.example.baseapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TouchActivity_dongwon extends AppCompatActivity {
    TextView timerTextView;
    TextView countTextView;
    TextView resultTextView;
    CountDownTimer countDownTimer;
    int touchCountMe = 0;
    int touchCountYou = 0;
    boolean isMeTurn = false;
    boolean isYouTurn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch2);

        timerTextView = findViewById(R.id.timerTextView);
        countTextView = findViewById(R.id.countTextView);
        resultTextView = findViewById(R.id.resultTextView);
        View purpleArea = findViewById(R.id.purpleArea);
        Button meButton = findViewById(R.id.meButton);
        Button youButton = findViewById(R.id.youButton);
        Button resultButton = findViewById(R.id.resultButton);

        // Me 버튼 클릭 시
        meButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMeTurn = true;
                isYouTurn = false;
                touchCountMe = 0;  // 타이머 시작 전에 카운트 초기화
                startTimer();
            }
        });

        // You 버튼 클릭 시
        youButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMeTurn = false;
                isYouTurn = true;
                touchCountYou = 0;  // 타이머 시작 전에 카운트 초기화
                startTimer();
            }
        });

        // 결과 버튼 클릭 시
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMeTurn || isYouTurn) {
                    resultTextView.setText("타이머가 끝난 후 결과를 확인해 주세요.");
                } else {
                    showResult();
                }
            }
        });

        // 보라색 구역 터치 시
        purpleArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isMeTurn) {
                        touchCountMe++;
                    } else if (isYouTurn) {
                        touchCountYou++;
                    }
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
                timerTextView.setText("남은 시간: 0");
                isMeTurn = false;
                isYouTurn = false;
                updateTouchCount();
            }
        }.start();
    }

    // 터치 횟수 업데이트
    private void updateTouchCount() {
        if (isMeTurn) {
            countTextView.setText("Me 터치 횟수: " + touchCountMe);
        } else if (isYouTurn) {
            countTextView.setText("You 터치 횟수: " + touchCountYou);
        }
    }

    // 결과 표시
    private void showResult() {
        String result;
        if (touchCountMe > touchCountYou) {
            result = "Me가 승리했습니다!";
        } else if (touchCountYou > touchCountMe) {
            result = "You가 승리했습니다!";
        } else {
            result = "동점입니다!";
        }
        resultTextView.setText(result);
    }
}

