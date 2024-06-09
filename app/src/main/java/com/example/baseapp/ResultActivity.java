package com.example.baseapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity { //자이로볼의 결과화면을 보여주는 액티비티 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long totalTime = getIntent().getLongExtra("TOTAL_TIME", 0);
        TextView resultTextView = findViewById(R.id.resultTextView);
        resultTextView.setText("걸린 시간 : " + (totalTime / 1000) + " 초");

        setContentView(R.layout.activity_result);
    }
}
