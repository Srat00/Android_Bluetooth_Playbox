package com.example.baseapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GyroStart extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gyro_start);

        BluetoothManager bluetoothManager = BluetoothManager.getInstance();

        Button startButton = findViewById(R.id.start_button);

        Handler handler = new Handler(msg -> {
            if (msg.what == MainActivity.BT_MESSAGE_READ) {
                byte[] readBuf = (byte[]) msg.obj;
                String readMessage = new String(readBuf, 0, msg.arg1);
                if (readMessage.equals("Gyro"))
                {
                    Intent intent = new Intent(GyroStart.this, GyroScope.class);
                    startActivity(intent);
                }
            }
            return true;
        });

        bluetoothManager.setBluetoothHandler(handler);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GyroStart.this, GyroScope.class);
                startActivity(intent);
                bluetoothManager.sendBluetoothMessage("Gyro");
            }
        });
    }
}
