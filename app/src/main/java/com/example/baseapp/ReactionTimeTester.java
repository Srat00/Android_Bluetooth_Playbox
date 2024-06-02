package com.example.baseapp;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ReactionTimeTester {

    private Context context;
    private Button changeButton;
    private TextView text_of_result;
    private long startTime;
    private boolean isReadyToMeasure = false;
    long my_score=0,enemy_score=0;

    public long getScore() {
        return my_score;
    }

    public void setScore(long enemy_score) {
        this.enemy_score = enemy_score;
    }
    public void startAlarm(){

    };
    public ReactionTimeTester(Context context, Button changeButton,TextView win) {
        this.text_of_result=win;
        this.context = context;
        this.changeButton = changeButton;
        this.changeButton.setOnClickListener(v -> startReactionTest());

    }

    public void startReactionTest() {
        startAlarm();
        changeButton.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray)); // 초기 색
        changeButton.setText("초록색이 나오면 이 버튼을 터치하세요!");
        changeButton.setEnabled(false); // 시작 버튼 비활성화

        // 랜덤한 지연 후 초록색으로 변경
        long delay = (long) (Math.random() * 5000 + 1000); // 1초에서 6초 사이의 지연
        new Handler().postDelayed(() -> {
            changeButton.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
            changeButton.setText("Press now!");
            changeButton.setEnabled(true);
            startTime = System.currentTimeMillis(); // 타이머 시작
            isReadyToMeasure = true;
        }, delay);
    }

    public void measureReactionTime() {
        if (isReadyToMeasure) {
            long reactionTime = System.currentTimeMillis() - startTime;
            changeButton.setText("Reaction Time: " + reactionTime + " ms");
            my_score=reactionTime;

            changeButton.setEnabled(false);
            isReadyToMeasure = false;

            if(my_score<enemy_score){text_of_result.setText("당신의 승리입니다!");}
            else{text_of_result.setText("당신의 패배입니다!");}
        }
    }
}
