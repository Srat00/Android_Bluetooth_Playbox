package com.example.baseapp;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.SystemClock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothManager
{
    private static BluetoothManager instance;
    private ConnectedBluetoothThread connectedThread;
    private Handler mBluetoothHandler;

    private BluetoothManager() { }

    public static synchronized BluetoothManager getInstance() {
        if (instance == null) {
            instance = new BluetoothManager();
        }
        return instance;
    }

    public void setBluetoothHandler(Handler handler) {
        mBluetoothHandler = handler;
    }

    public void startConnectedThread(BluetoothSocket socket) {
        if (connectedThread != null) {
            connectedThread.cancel();
        }
        connectedThread = new ConnectedBluetoothThread(socket);
        connectedThread.start();
    }

    public ConnectedBluetoothThread getConnectedThread() {
        return connectedThread;
    }

    public void sendBluetoothMessage(String message)
    {
        if (connectedThread != null)
        {
            connectedThread.write(message);
        }
        else
        {
            // Handle the case where the thread is not yet initialized
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

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                // Toast를 사용하기 위해 context를 전달받아야 하지만 싱글톤에서 context를 사용할 수 없으므로 생략하거나 다른 방법을 사용해야 합니다.
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() { // GET
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        if (mBluetoothHandler != null) {
                            mBluetoothHandler.obtainMessage(MainActivity.BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                        }
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(String str) { // SET
            byte[] bytes = str.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                // Toast를 사용하기 위해 context를 전달받아야 하지만 싱글톤에서 context를 사용할 수 없으므로 생략하거나 다른 방법을 사용해야 합니다.
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                // Toast를 사용하기 위해 context를 전달받아야 하지만 싱글톤에서 context를 사용할 수 없으므로 생략하거나 다른 방법을 사용해야 합니다.
            }
        }
    }
}
