package com.example.gyrosensor;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.button1);
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