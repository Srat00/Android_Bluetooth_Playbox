package com.example.baseapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity { //자이로볼의 결과화면을 보여주는 액티비티 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button retBut=findViewById(R.id.return_to_start);

        long totalTime = getIntent().getLongExtra("TOTAL_TIME", 0);
        long enemyTime = getIntent().getLongExtra("ENEMY_TOTAL_TIME", 0);
        TextView resultOfRace = findViewById(R.id.result_of_race);
        TextView resultTextView = findViewById(R.id.resultTextView);
        TextView enemyResultTextView = findViewById(R.id.enemyTextView);
        if(totalTime<enemyTime){
            resultOfRace.setText("WINNER IS YOU" );
        }else{
            resultOfRace.setText("WINNER IS ENEMY" );
        }
        resultTextView.setText("나의 시간 : " + totalTime + " 밀리초");
        enemyResultTextView.setText("상대의 시간 : " + enemyTime + " 밀리초");

        setContentView(R.layout.activity_result);
    }
}
