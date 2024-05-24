package com.example.baseapp;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class ReactionTimeTester {

    private Context context;
    private Button changeButton;
    private long startTime;
    private boolean isReadyToMeasure = false;

    public ReactionTimeTester(Context context, Button changeButton) {
        this.context = context;
        this.changeButton = changeButton;
        this.changeButton.setOnClickListener(v -> startReactionTest());
    }

    public void startReactionTest() {
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
            changeButton.setEnabled(false);
            isReadyToMeasure = false;
        }
    }
}
