package com.example.opensourceproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button Touch = findViewById(R.id.touch_button);
        Button Rhythm = findViewById(R.id.rhythm_button);
        Button Vibrate = findViewById(R.id.vibrate_button);

        //Touch 페이지 이동
        Touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 첫 번째 버튼 클릭 시 실행할 내용
                Intent intent = new Intent(MainActivity.this, TouchActivity.class);
                startActivity(intent);
            }
        });

        //Rhythm 페이지 이동
        Rhythm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 두 번째 버튼 클릭 시 실행할 내용
                Intent intent = new Intent(MainActivity.this, RhythmActivity.class);
                startActivity(intent);
            }
        });

        //Vibrate 페이지 이동
        Vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 두 번째 버튼 클릭 시 실행할 내용
                Intent intent = new Intent(MainActivity.this, VibrateActivity.class);
                startActivity(intent);
            }
        });

    }
}