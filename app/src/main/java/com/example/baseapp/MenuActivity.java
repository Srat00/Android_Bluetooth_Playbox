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
        gameList.add(new Game("쉐킷쉐킷", "[가속도 센서] 최대한 많이 핸드폰을 흔들어라!", R.drawable.ic_shake, ShakeDetector.class));
        gameList.add(new Game("나침반 맞추기", "[지자기 센서] 최대한 빨리 각도를 맞추어라!", R.drawable.ic_compass, Compass.class));
        gameList.add(new Game("공을 굴려라", "[자이로 센서] 핸드폰을 기울여서 공을 굴려라!", R.drawable.ic_gyro, GyroStart.class));
        gameList.add(new Game("순간판단력 측정", "[터치 센서] 얼마나 빠르게 판단할 수 있나요?", R.drawable.ic_touch_count, TouchActivity_dongwon.class));
        gameList.add(new Game("박자감각 측정", "[일반] 리드미컬한 박자 감각 테스트!", R.drawable.ic_rhythm, RhythmActivity.class));
        gameList.add(new Game("반응속도 측정", "[일반] 당신의 반응 속도는 어느 정도인가요?", R.drawable.ic_time_respond, ReactionTimeActivity.class));
        gameList.add(new Game("화면 격파", "[일반] 화면 격파! 시간 안에 최대한 많이 터치터치~", R.drawable.ic_touch_count, TouchCountActivity.class));

        GameAdapter adapter = new GameAdapter(this, gameList);
        recyclerView.setAdapter(adapter);
    }
}
