package com.example.baseapp;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TouchCountActivity extends AppCompatActivity {

    private int touchCount = 0;
    private TextView touchCountTextView;
    private View touchAreaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_count);

        touchCountTextView = findViewById(R.id.touchCountTextView);
        touchAreaView = findViewById(R.id.touchAreaView);
        
        //터치 카운트 증가
        touchAreaView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 화면 터치 시 횟수 증가
                    touchCount++;
                    // TextView 업데이트
                    touchCountTextView.setText("Touch Count: " + touchCount);
                    break;
            }
            return true;
        });
    }
}
