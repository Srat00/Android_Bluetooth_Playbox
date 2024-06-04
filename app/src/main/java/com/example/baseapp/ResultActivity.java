package com.example.baseapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity { //자이로볼의 결과화면을 보여주는 액티비티 

    BluetoothManager bluetoothManager = BluetoothManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long totalTime = getIntent().getLongExtra("TOTAL_TIME", 0);

        TextView resultTextView = findViewById(R.id.resultTextView);
        TextView resultTextView_con = findViewById(R.id.resultTextView_con);

        resultTextView.setText("Your Total Time: " + totalTime + " ms");

        Handler handler = new Handler(msg -> {
            if (msg.what == MainActivity.BT_MESSAGE_READ) {
                byte[] readBuf = (byte[]) msg.obj;
                String readMessage = new String(readBuf, 0, msg.arg1);
                long conTime = Long.valueOf(readMessage);
                resultTextView_con.setText("Friend's Total Time: " + conTime + " ms");
            }
            return true;
        });

        bluetoothManager.setBluetoothHandler(handler);


        setContentView(R.layout.activity_result);
    }
}
