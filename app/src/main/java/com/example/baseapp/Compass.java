package com.example.baseapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class Compass extends AppCompatActivity implements CompassSensor.CompassListener {
    private CompassSensor compassSensor;
    private ImageView compassBackground;
    private ImageView compassNeedle;
    private ImageView miniCompass;
    private ImageView compassNeedle2; //시작 전 돌아가는 니들을 위한 애니메이션
    private TextView currentAzimuthText;
    private TextView targetAzimuthView;
    private TextView timerView;
    private Button startButton;

    private int targetAzimuth;
    private long startTime;
    private boolean isTargetSet = false;
    private boolean isWithinRange = false;
    private Handler handler = new Handler();
    private Handler rangeHandler = new Handler();
    private Runnable rangeCheckRunnable;
    private int currentAzimuth = 0;

    private static final int RANGE_DURATION_MS = 1000; // 1 second
    private static final int RANGE_THRESHOLD_DEGREES = 5; // 5 degrees

    long enemy_score = 0;
    long my_score = 0;

    public long getScore() {
        return my_score;
    }

    public void setScore(long enemy_score) {
        this.enemy_score = enemy_score;
    }

    public void startAlarm() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        miniCompass = findViewById(R.id.mini_compass);
        compassBackground = findViewById(R.id.compass_background);
        compassNeedle = findViewById(R.id.compass_needle);
        currentAzimuthText = findViewById(R.id.current_azimuth_text);
        targetAzimuthView = findViewById(R.id.target_azimuth_text);
        timerView = findViewById(R.id.timer_text);
        startButton = findViewById(R.id.start_button);

        compassSensor = new CompassSensor(this, this);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
        startNeedleAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compassSensor.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compassSensor.stop();
        handler.removeCallbacks(timerRunnable);
        rangeHandler.removeCallbacks(rangeCheckRunnable);
    }

    @Override
    public void onNewAzimuth(int azimuth) {
        if (isTargetSet){
            rotateCompass(azimuth);
        }

        currentAzimuthText.setText(azimuth + "°");
        checkIfWithinRange(azimuth);
    }

    private void rotateCompass(int azimuth) {
        RotateAnimation rotateAnimation = new RotateAnimation(
                currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(500);
        rotateAnimation.setFillAfter(true);
        compassBackground.startAnimation(rotateAnimation);
        currentAzimuth = -azimuth;
    }

    private void checkIfWithinRange(int azimuth) {
        if (isTargetSet) {
            int difference = Math.abs(targetAzimuth - azimuth);
            if (difference <= RANGE_THRESHOLD_DEGREES || difference >= (360 - RANGE_THRESHOLD_DEGREES)) {
                if (!isWithinRange) {
                    isWithinRange = true;
                    rangeHandler.postDelayed(rangeCheckRunnable, RANGE_DURATION_MS);
                }
            } else {
                isWithinRange = false;
                rangeHandler.removeCallbacks(rangeCheckRunnable);
            }
        }
    }
    private void startNeedleAnimation() {
        compassNeedle2 = findViewById(R.id.compass_needle2); // 시작 전 돌아가는 나침반.
        RotateAnimation rotateAnimation = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000); // 2 seconds for one full rotation
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        compassNeedle2.startAnimation(rotateAnimation);
    }

    private void startGame() {
        startAlarm();
        setNewTargetAzimuth();
        startTime = System.currentTimeMillis();
        updateTimer();
        startRangeCheck(); // 범위 확인 시작

        compassBackground.setVisibility(View.VISIBLE);
        compassNeedle.setVisibility(View.VISIBLE);
        currentAzimuthText.setVisibility(View.VISIBLE);
        targetAzimuthView.setVisibility(View.VISIBLE);
        timerView.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.GONE); // Hide the start button
        compassNeedle2.clearAnimation(); // Stop the initial rotation animation
        compassNeedle2.setVisibility(View.GONE);
        miniCompass.setVisibility(View.GONE);
        findViewById(R.id.textView2).setVisibility(View.GONE);
        findViewById(R.id.explain_text).setVisibility(View.GONE);
        findViewById(R.id.winner_text).setVisibility(View.GONE);
    }

    private void setNewTargetAzimuth() {
        Random random = new Random();
        targetAzimuth = random.nextInt(360);
        targetAzimuthView.setText("Target: " + targetAzimuth + " degrees");
        isTargetSet = true;
        isWithinRange = false;
    }

    private void updateTimer() {
        handler.post(timerRunnable);
    }

    private void startRangeCheck() {
        rangeCheckRunnable = new Runnable() {
            @Override
            public void run() {
                if (isWithinRange) {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    timerView.setText("Time: " + elapsedTime / 1000.0 + " seconds");
                    isTargetSet = false;

                    compassBackground.clearAnimation();
                    compassBackground.setVisibility(View.GONE); // 나침반 배경 사라짐
                    compassNeedle.setVisibility(View.GONE); // 나침반 바늘 사라짐
                    compassNeedle2.setVisibility(View.VISIBLE); // 시작화면 나침반 바늘 뜨게함 
                    miniCompass.setVisibility(View.VISIBLE); // 미니 나침반이 뜨게함 [시작
                    findViewById(R.id.explain_text).setVisibility(View.VISIBLE); // 설명 텍스트가 뜨게함
                    currentAzimuthText.setVisibility(View.GONE); // 현재 각도를 없앰(게임중
                    targetAzimuthView.setVisibility(View.GONE); // 타겟 각도를 없앰(게임중
                    timerView.setVisibility(View.VISIBLE); // 걸린 시간을 보여줌 (게임끝)
                    startButton.setVisibility(View.VISIBLE);  // 시작버튼을 보이게함 [ 게임 시작중
                    findViewById(R.id.textView2).setVisibility(View.VISIBLE); //제목을 띄움

                    startNeedleAnimation();

                    TextView winnerText = findViewById(R.id.winner_text);
                    //enemy scroe 설정하기. 추가구현 필요.
                    if (my_score < enemy_score) {
                        winnerText.setText("Winner: You");
                    } else if (my_score > enemy_score) {
                        winnerText.setText("Winner: Enemy");
                    } else {
                        winnerText.setText("It's a tie!");
                    }
                    winnerText.setVisibility(View.VISIBLE);
                }
            }
        };
        rangeHandler.postDelayed(rangeCheckRunnable, RANGE_DURATION_MS);
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isTargetSet) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                timerView.setText("걸린시간: " + elapsedTime / 1000.0 + " 초");
                my_score = (long) (elapsedTime / 1000.0);
                handler.postDelayed(this, 100);
            }
        }
    };
}
