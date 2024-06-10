package com.example.baseapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity { // 자이로볼의 결과화면을 보여주는 액티비티

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView resultTextView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result); // 레이아웃을 먼저 설정합니다.

        long totalTime = getIntent().getLongExtra("TOTAL_TIME", 0);
        resultTextView = findViewById(R.id.resultTextView);
        resultTextView.setText("걸린 시간 : " + (totalTime / 1000) + " 초");
    }
}
