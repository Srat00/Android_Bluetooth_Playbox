package com.example.opensourceproject;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.media.MediaPlayer;

public class RhythmActivity extends AppCompatActivity {
    private TextView lastTime;
    private TextView touchTime;
    private long startTime;
    private long[] meTouchArray;
    private long[] youTouchArray;
    private int meTouchCount;
    private int youTouchCount;
    private boolean timerFinished = false;
    private boolean timerStarted = false; // 타이머가 시작되었는지 여부를 확인하는 변수
    private boolean youMode = false; // You 모드인지 여부를 확인하는 변수
    private MediaPlayer correctSound;
    private MediaPlayer wrongSound; //wrongsound mp3 못찾음

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rhythm);

        lastTime = findViewById(R.id.lastTime);
        touchTime = findViewById(R.id.touchTime);
        Button me = findViewById(R.id.me);
        Button you = findViewById(R.id.you);
        Button startTimer = findViewById(R.id.startTimer);
        correctSound = MediaPlayer.create(this, R.raw.correct_sound);
        //wrongSound = MediaPlayer.create(this, R.raw.wrong_sound);
        // 배열 초기화

        meTouchArray = new long[10]; // 예시로 10개의 요소를 가진 배열
        youTouchArray = new long[10]; // 예시로 10개의 요소를 가진 배열

        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerStarted) {
                    return; // 타이머가 이미 시작된 경우 무시
                }

                new CountDownTimer(10000, 100) { // 10초 동안 0.1초 간격으로 실행
                    public void onTick(long millisUntilFinished) {
                        // 타이머가 줄어들 때마다 실행되는 코드
                        lastTime.setText("남은 시간: " + millisUntilFinished / 1000 + "." + (millisUntilFinished / 100) % 10 + " 초");
                    }

                    public void onFinish() {
                        // 타이머가 끝났을 때 실행되는 코드
                        lastTime.setText("타이머 종료");
                        timerFinished = true;
                        timerStarted = false; // 타이머 종료 상태로 설정
                        youMode = !youMode; // You 모드로 전환
                        if (youMode) {
                            lastTime.setText("You 모드: Me 버튼과 시간 비교하세요.");
                        }
                    }
                }.start();

                // 타이머 시작 시간 기록
                startTime = System.currentTimeMillis();
                timerStarted = true; // 타이머가 시작되었음을 표시
                timerFinished = false; // 타이머가 종료되지 않았음을 표시

                if (!youMode) {
                    meTouchCount = 0; // 터치 카운트를 초기화
                } else {
                    youTouchCount = 0; // 터치 카운트를 초기화
                }
                touchTime.setText(""); // 기록된 시간 초기화
            }
        });

        // me 버튼 클릭 이벤트 처리
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timerStarted) {
                    touchTime.setText("타이머가 시작되지 않았습니다.");
                    return;
                }

                if (timerFinished) {
                    touchTime.setText("타이머가 종료되어 더 이상 저장할 수 없습니다.");
                    return;
                }

                // 버튼을 클릭했을 때 현재 시간을 배열에 저장
                if (meTouchCount < meTouchArray.length) {
                    meTouchArray[meTouchCount] = (System.currentTimeMillis() - startTime) / 100;
                    meTouchCount++;
                    correctSound.start();

                    // 배열 내용을 텍스트뷰에 출력
                    String touchText = "";
                    for (int i = 0; i < meTouchCount; i++) {
                        touchText += "Me 터치 시간 " + (i + 1) + ": " + meTouchArray[i] / 10 + "." + (meTouchArray[i] % 10) + "초\n";
                    }
                    touchTime.setText(touchText);
                } else {
                    touchTime.setText("더 이상 저장할 수 없습니다.");
                }
            }
        });

        // you 버튼 클릭 이벤트 처리
        you.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!youMode) {
                    touchTime.setText("You 모드가 아닙니다. Start 버튼을 눌러 타이머를 시작하세요.");
                    return;
                }

                if (!timerStarted) {
                    touchTime.setText("타이머가 시작되지 않았습니다.");
                    return;
                }

                if (timerFinished) {
                    touchTime.setText("타이머가 종료되어 더 이상 저장할 수 없습니다.");
                    return;
                }

                // 현재 시간을 기록
                long currentTime = (System.currentTimeMillis() - startTime) / 100;

                // 버튼을 클릭했을 때 현재 시간을 배열에 저장
                if (youTouchCount < youTouchArray.length) {
                    youTouchArray[youTouchCount] = currentTime;
                    youTouchCount++;

                    // 배열 내용을 텍스트뷰에 출력
                    String touchText = "";
                    for (int i = 0; i < youTouchCount; i++) {
                        touchText += "You 터치 시간 " + (i + 1) + ": " + youTouchArray[i] / 10 + "." + (youTouchArray[i] % 10) + "초\n";
                    }
                    touchTime.setText(touchText);
                } else {
                    touchTime.setText("더 이상 저장할 수 없습니다.");
                }

                // 기록된 시간과 ±0.5초 오차를 가지고 비교
                boolean matchFound = false;
                for (int i = 0; i < meTouchCount; i++) {
                    if (Math.abs(currentTime - meTouchArray[i]) <= 5) {
                        matchFound = true;
                        break;
                    }
                }

                if (matchFound) {
                    touchTime.setText("맞았습니다!");
                } else {
                    touchTime.setText("틀렸습니다.");
                }
            }
        });
    }
}
