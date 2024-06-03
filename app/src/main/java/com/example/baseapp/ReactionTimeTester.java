package com.example.baseapp;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReactionTimeTester {

    private Context context;
    private Button changeButton;
    private Button startBtn;
    private TextView text_of_result;
    private long startTime;
    private boolean isReadyToMeasure = false;
    private boolean hasMeasured = false;
    long my_score = 0, enemy_score = 0;
    private Handler handler;
    private Runnable timeoutRunnable;

    public long getScore() {
        return my_score;
    }

    public void setScore(long enemy_score) {
        this.enemy_score = enemy_score;
    }

    public void startAlarm() {
        // Alarm logic here
    }

    public ReactionTimeTester(Context context, Button changeButton, TextView win,Button start) {
        this.text_of_result = win;
        this.context = context;
        this.changeButton = changeButton;
        this.changeButton.setOnClickListener(v -> measureReactionTime());
        this.handler = new Handler();
        this.startBtn=start;
    }

    public void startReactionTest() {
        startAlarm();
        text_of_result.setText("버튼을 누를수 있게 되면 터치하세요!!");
        changeButton.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray)); // 초기 색
        changeButton.setText("초록색이 나오면 이 버튼을 터치하세요!");
        changeButton.setEnabled(false); // 시작 버튼 비활성화
        startBtn.setEnabled(false);
        // 랜덤한 지연 후 초록색으로 변경
        long delay = (long) (Math.random() * 5000 + 1000); // 1초에서 6초 사이의 지연
        handler.postDelayed(() -> {
            changeButton.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
            changeButton.setText("Press now!");
            changeButton.setEnabled(true);
            startTime = System.currentTimeMillis(); // 타이머 시작
            isReadyToMeasure = true;
            hasMeasured = false; // 측정 여부 초기화
            startTimeout(); // 7초 타이머 시작
        }, delay);
    }

    private void startTimeout() {
        timeoutRunnable = () -> {
            if (isReadyToMeasure) {
                if (!hasMeasured) {
                    my_score = 7000; // 7초로 설정
                }
                showResult();
            }
        };
        handler.postDelayed(timeoutRunnable, 7000); // 7초 후 실행
    }

    public void measureReactionTime() {
        if (isReadyToMeasure && !hasMeasured) {
            handler.removeCallbacks(timeoutRunnable); // 타임아웃 콜백 제거
            long reactionTime = System.currentTimeMillis() - startTime;
            my_score = reactionTime;
            hasMeasured = true;

            changeButton.setEnabled(false);
            isReadyToMeasure = false;

            long remainingTime = 7000 - reactionTime;
            handler.postDelayed(this::showResult, remainingTime); // 남은 시간 후 결과 표시
        }
    }

    private void showResult() {
        startBtn.setEnabled(true);
        changeButton.setText("Reaction Time: " + my_score + " ms");
        if (my_score < enemy_score) {
            text_of_result.setText("당신의 승리입니다!");
        } else {
            text_of_result.setText("당신의 패배입니다!");
        }
    }
}
