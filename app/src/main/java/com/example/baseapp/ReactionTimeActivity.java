package com.example.baseapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReactionTimeActivity extends AppCompatActivity {

    private ReactionTimeTester reactionTimeTester;
    private Button reactionButton;
    private TextView win_or_defeat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_time);

        reactionButton = findViewById(R.id.reaction_button); // 레이아웃에 정의된 버튼 ID
        win_or_defeat=findViewById(R.id.win_or_defeat);
        reactionTimeTester = new ReactionTimeTester(this, reactionButton,win_or_defeat);

        // 사용자가 버튼을 누르면 반응 시간 측정
        reactionButton.setOnClickListener(v -> reactionTimeTester.measureReactionTime());
    }

    public void startReactionTest(View view) {
        reactionTimeTester.startReactionTest();
    }

    public void measureReactionTime(View view) {
        reactionTimeTester.measureReactionTime();
    }
}
