package com.example.baseapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
{
    Button mBtnBluetoothOn;
    Button mBtnBluetoothOff;
    Button mBtnConnect;

    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;

    Handler mBluetoothHandler;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;

    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final String[] permissions = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT
    };

    private final ActivityResultLauncher<String[]> permissionsRequestActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    permissions -> {
                        boolean granted = true;
                        for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
                            if (!entry.getValue()) {
                                granted = false;
                                break;
                            }
                        }
                        if (granted) {
                            // 권한 승인 처리
                        } else {
                            // 권한 거부 처리
                        }
                    });

    private boolean hasPermissions(List<String> permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!hasPermissions(Arrays.asList(permissions))) {
                permissionsRequestActivityLauncher.launch(permissions);
            } else {
                // 이미 권한을 승인받음
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        Button menuButton = findViewById(R.id.button_menu);
        mBtnBluetoothOn = (Button) findViewById(R.id.btnBluetoothOn);
        mBtnBluetoothOff = (Button) findViewById(R.id.btnBluetoothOff);
        mBtnConnect = (Button) findViewById(R.id.btnBluetoothConnect);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 메뉴 화면으로 이동
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        mBtnBluetoothOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothOn();
            }
        });
        mBtnBluetoothOff.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothOff();
            }
        });
        mBtnConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPairedDevices();
            }
        });
    }

    void bluetoothOn() {
        if (mBluetoothAdapter == null)
        {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
        }
        else
        {
            if (mBluetoothAdapter.isEnabled())
            {
                Toast.makeText(getApplicationContext(), "블루투스가 이미 활성화 되어 있습니다.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "블루투스가 활성화 되어 있지 않습니다.", Toast.LENGTH_LONG).show();
                Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getApplicationContext(), "블루투스 권한이 적용되지 않았습니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);
            }
        }
    }

    void bluetoothOff() {
        if (mBluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "블루투스 권한이 적용되지 않았습니다.", Toast.LENGTH_LONG).show();
                return;
            }
            mBluetoothAdapter.disable();
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되었습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "블루투스가 이미 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BT_REQUEST_ENABLE:
                if (resultCode == RESULT_OK) {
                    // 블루투스 활성화를 확인을 클릭하였다면
                    Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) {
                    // 블루투스 활성화를 취소를 클릭하였다면
                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void listPairedDevices() {
        if (mBluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "블루투스 권한이 적용되지 않았습니다.", Toast.LENGTH_LONG).show();
                return;
            }
            mPairedDevices = mBluetoothAdapter.getBondedDevices();

            if (mPairedDevices.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("장치 선택");

                mListPairedDevices = new ArrayList<String>();
                for (BluetoothDevice device : mPairedDevices) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "블루투스 권한이 적용되지 않았습니다.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mListPairedDevices.add(device.getName());
                }
                final CharSequence[] items = mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);
                mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        connectSelectedDevice(items[item].toString());
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    void connectSelectedDevice(String selectedDeviceName) {
        for (BluetoothDevice tempDevice : mPairedDevices) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "블루투스 권한이 적용되지 않았습니다.", Toast.LENGTH_LONG).show();
                return;
            }
            if (selectedDeviceName.equals(tempDevice.getName())) {
                mBluetoothDevice = tempDevice;
                break;
            }
        }
        try
        {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket.connect();
            mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
            mThreadConnectedBluetooth.start();
            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
        }
        catch (IOException e)
        {
            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
        }

    }


    private class ConnectedBluetoothThread extends Thread
    {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket)
        {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try
            {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e)
            {
                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() //GET
        {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true)
            {
                try
                {
                    bytes = mmInStream.available();
                    if (bytes != 0)
                    {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                }
                catch (IOException e)
                {
                    break;
                }
            }
        }
        public void write(String str) //SET
        {
            byte[] bytes = str.getBytes();
            try
            {
                mmOutStream.write(bytes);
            }
            catch (IOException e)
            {
                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
        public void cancel()
        {
            try
            {
                mmSocket.close();
            }
            catch (IOException e)
            {
                Toast.makeText(getApplicationContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}











