package com.example.baseapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ReactionTimeActivity extends AppCompatActivity {

    private ReactionTimeTester reactionTimeTester;
    private Button reactionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_time);

        reactionButton = findViewById(R.id.reaction_button); // 레이아웃에 정의된 버튼 ID
        reactionTimeTester = new ReactionTimeTester(this, reactionButton);

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
