package com.example.baseapp;

import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;

public class RhythmActivity extends AppCompatActivity {
    private TextView lastTime;
    private long startTime;
    private long[] meTouchArray;
    private long[] youTouchArray;
    private int meTouchCount;
    private int youTouchCount;
    private boolean timerFinished = false;
    private boolean timerStarted = false;
    private boolean youMode = false;
    private MediaPlayer sound1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rhythm);

        lastTime = findViewById(R.id.lastTime);
        Button startTimer = findViewById(R.id.startTimer);
        RelativeLayout rootView = findViewById(R.id.rootView);

        sound1 = MediaPlayer.create(this, R.raw.sound1);

        meTouchArray = new long[10];
        youTouchArray = new long[10];

        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerStarted) {
                    return;
                }

                new CountDownTimer(10000, 100) {
                    public void onTick(long millisUntilFinished) {
                        lastTime.setText("남은 시간: " + millisUntilFinished / 1000 + "." + (millisUntilFinished / 100) % 10 + " 초");
                    }

                    public void onFinish() {
                        lastTime.setText("타이머 종료");
                        timerFinished = true;
                        timerStarted = false;
                        if (youMode) {
                            checkSuccess();
                        } else {
                            youMode = true;
                            lastTime.setText("You 모드: 터치하여 시간을 기록하세요.");
                        }
                    }
                }.start();

                startTime = System.currentTimeMillis();
                timerStarted = true;
                timerFinished = false;

                if (!youMode) {
                    meTouchCount = 0;
                } else {
                    youTouchCount = 0;
                }
            }
        });

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!timerStarted) {
                    lastTime.setText("타이머가 시작되지 않았습니다.");
                    return false;
                }

                if (timerFinished) {
                    lastTime.setText("타이머가 종료되어 더 이상 저장할 수 없습니다.");
                    return false;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sound1.setVolume(0.0f, 0.0f); // 볼륨을 0으로 설정하여 음소거
                        break;
                    case MotionEvent.ACTION_UP:
                        long currentTime = System.currentTimeMillis();
                        long touchDuration = (currentTime - startTime) / 100;

                        if (youMode) {
                            if (youTouchCount < youTouchArray.length) {
                                youTouchArray[youTouchCount] = touchDuration;
                                youTouchCount++;
                            } else {
                                lastTime.setText("더 이상 저장할 수 없습니다.");
                            }
                        } else {
                            if (meTouchCount < meTouchArray.length) {
                                meTouchArray[meTouchCount] = touchDuration;
                                meTouchCount++;
                            } else {
                                lastTime.setText("더 이상 저장할 수 없습니다.");
                            }
                        }
                        sound1.setVolume(1.0f, 1.0f); // 볼륨을 원래대로 설정
                        sound1.start(); // 터치할 때마다 sound1 재생
                        shakeBackground();
                        break;
                }
                return true;
            }
        });
    }

    private void checkSuccess() {
        int successCount = 0;
        for (int i = 0; i < Math.min(meTouchCount, youTouchCount); i++) {
            if (Math.abs(meTouchArray[i] - youTouchArray[i]) <= 1) { // ±0.1초 오차
                successCount++;
            }
        }

        if (successCount >= 8) {
            lastTime.setText("성공!");
        } else {
            lastTime.setText("실패!");
        }
    }

    private void shakeBackground() {
        View rootView = findViewById(R.id.rootView);
        ObjectAnimator shakeAnimatorX = ObjectAnimator.ofFloat(rootView, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
        ObjectAnimator shakeAnimatorY = ObjectAnimator.ofFloat(rootView, "translationY", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(shakeAnimatorX, shakeAnimatorY);
        animatorSet.setDuration(500);
        animatorSet.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sound1 != null) {
            sound1.release();
            sound1 = null;
        }
    }
}
