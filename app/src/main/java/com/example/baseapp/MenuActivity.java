package com.example.baseapp;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // 메뉴 아이템에 따라 기능을 수행
        if (id == R.id.action_touch_count) {
            // 터치를 카운트하는 화면으로 전환
            startActivity(new Intent(this, TouchCountActivity.class));
            return true;
        } else if (id == R.id.action_shake) {
            startActivity(new Intent(this, ShakeDetector.class));
            return true;
        }else if (id == R.id.action_compass) {
            startActivity(new Intent(this, Compass.class));
            return true;
        }else if (id == R.id.action_of_time_respond) {
            startActivity(new Intent(this, ReactionTimeActivity.class));
            return true;
        }else if (id == R.id.action_of_gyro) {
            startActivity(new Intent(this, GyroScope.class));
            return true;
        }else if (id == R.id.action_rhythm) {
            startActivity(new Intent(this, RhythmActivity.class));
            return true;
        }else if (id == R.id.action_vibrate) {
            startActivity(new Intent(this, VibrateActivity.class));
            return true;
        }else if (id == R.id.action_touch2) {
            startActivity(new Intent(this,TouchActivity_dongwon.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
