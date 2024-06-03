package com.example.baseapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Game> gameList = new ArrayList<>();
        gameList.add(new Game("터치 횟수", "시간안에 최대한 많이 터치터치~", R.drawable.ic_touch_count, TouchCountActivity.class));
        gameList.add(new Game("흔들기 횟수", "최대한 많이 핸드폰을 흔들어라!", R.drawable.ic_shake, ShakeDetector.class));
        gameList.add(new Game("나침반", "최대한 빨리 각도를 맞추어라!", R.drawable.ic_compass, Compass.class));
        gameList.add(new Game("반응 속도", "최대한 빨리 터치하세요~", R.drawable.ic_time_respond, ReactionTimeActivity.class));
        gameList.add(new Game("공 굴리기", "핸드폰을 기울여라!", R.drawable.ic_gyro, GyroStart.class));
        gameList.add(new Game("리듬", "리듬미컬한 테스트~", R.drawable.ic_rhythm, RhythmActivity.class));
        gameList.add(new Game("진동", "진동 테스트", R.drawable.ic_vibrate, VibrateActivity.class));
        gameList.add(new Game("터치2", "다른 터치 게임", R.drawable.ic_touch_count, TouchActivity_dongwon.class));

        GameAdapter adapter = new GameAdapter(this, gameList);
        recyclerView.setAdapter(adapter);
    }
}
