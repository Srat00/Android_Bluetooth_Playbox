package com.example.baseapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TouchActivity_dongwon extends AppCompatActivity {
    TextView timerTextView;
    TextView countTextView;
    TextView resultTextView;
    CountDownTimer countDownTimer;
    int currentNumber = 1;
    long startTime;
    long endTime;
    List<Button> buttons;
    String[] colors = {"#80FF0000", "#80FFA500", "#80FFFF00", "#80008000", "#800000FF", "#804B0082", "#80800080", "#80FFFFFF", "black"}; // 빨강, 주황, 노랑, 초록, 파랑, 남색, 보라, 흰색, 검정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch2); // 레이아웃 파일을 설정

        timerTextView = findViewById(R.id.timerTextView);
        countTextView = findViewById(R.id.countTextView);
        resultTextView = findViewById(R.id.resultTextView);
        GridLayout gridLayout = findViewById(R.id.gridLayout); // gridLayout ID 수정
        Button meButton = findViewById(R.id.meButton);
        Button youButton = findViewById(R.id.youButton);
        Button resultButton = findViewById(R.id.resultButton);

        // GridLayout 안의 버튼들 가져오기
        buttons = new ArrayList<>();
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof Button) {
                buttons.add((Button) child);
            }
        }

        // Me 버튼 클릭 시
        meButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
                startTimer();
            }
        });

        // You 버튼 클릭 시
        youButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
                startTimer();
            }
        });

        // 결과 버튼 클릭 시
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endTime > 0) {
                    long duration = (endTime - startTime) / 1000;
                    resultTextView.setText("걸린 시간: " + duration + " 초");
                } else {
                    resultTextView.setText("게임이 아직 완료되지 않았습니다.");
                }
            }
        });

        // 각 버튼에 클릭 리스너 설정
        for (final Button button : buttons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (button.getText().toString().equals(String.valueOf(currentNumber))) {
                        button.setEnabled(false);
                        currentNumber++;
                        if (currentNumber > 9) {
                            endTime = System.currentTimeMillis();
                            countDownTimer.cancel();
                            timerTextView.setText("게임 종료");
                        }
                    }
                }
            });
        }
    }

    // 타이머 시작
    private void startTimer() {
        startTime = System.currentTimeMillis();
        endTime = 0;
        currentNumber = 1;
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("남은 시간: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                timerTextView.setText("남은 시간: 0");
                resultTextView.setText("시간 초과!");
            }
        }.start();
    }

    // 게임 리셋
    private void resetGame() {
        Collections.shuffle(buttons);
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setText(String.valueOf(i + 1));
            buttons.get(i).setEnabled(true);
            buttons.get(i).setBackgroundColor(android.graphics.Color.parseColor(colors[i]));
        }
        resultTextView.setText("");
        timerTextView.setText("남은 시간: 10");
        countTextView.setText("터치 횟수: 0");
    }
}
