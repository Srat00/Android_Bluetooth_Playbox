package com.example.baseapp;

import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class VibrateActivity extends AppCompatActivity {
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrate);

        button1 = findViewById(R.id.me);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vibrator 객체 가져오기
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                // 진동 실행
                if (vibrator != null) {
                    vibrator.vibrate(500); // 0.5초간 진동
                    Log.d("Vibration", "Vibration started");
                }
            }
        });
    }
}
