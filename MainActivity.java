package com.example.baseapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ShakeDetector.OnShakeListener {
    Button button1;
    private ShakeDetector shakeDetector;
    private TextView shakeCountTextView;
    private int shakeCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        shakeCountTextView = findViewById(R.id.shakeCountTextView);

        // ShakeDetector 인스턴스 생성
        shakeDetector = new ShakeDetector(this);
        // 리스너 설정
        shakeDetector.setOnShakeListener(this);


    }

    // 흔들림 이벤트 발생시 호출됨
    @Override
    public void onShake(int count) {
        shakeCount++;
        // 화면에 흔들린 횟수 업데이트
        updateShakeCountUI();
    }
    @Override
    protected void onResume() {
        super.onResume();
        shakeDetector.register(); // 등록
    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeDetector.unregister(); // 해제
    }

    // 화면에 흔들린 횟수를 업데이트하는 메서드
    private void updateShakeCountUI() {
        shakeCountTextView.setText("Shake Count: " + shakeCount);
    }
}