package com.ziasy.haanbaba.intellishopping.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ChatService {
    private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID MY_UUID_SECURE = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final String NAME_INSECURE = "BluetoothChatInsecure";
    private static final String NAME_SECURE = "BluetoothChatSecure";
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_NONE = 0;
    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    private final Handler handler;
    private AcceptThread insecureAcceptThread;
    private AcceptThread secureAcceptThread;
    private int state = 0;

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket serverSocket;
        private String socketType;

        public AcceptThread(boolean secure) {
            BluetoothServerSocket tmp = null;
            this.socketType = secure ? "Secure" : "Insecure";
            if (secure) {
                try {
                    tmp = ChatService.this.bluetoothAdapter.listenUsingRfcommWithServiceRecord(ChatService.NAME_SECURE, ChatService.MY_UUID_SECURE);
                } catch (IOException e) {
                }
            } else {
                try {
                    tmp = ChatService.this.bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(ChatService.NAME_INSECURE, ChatService.MY_UUID_INSECURE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.serverSocket = tmp;
        }

        public void run() {
            setName("AcceptThread" + this.socketType);
            while (ChatService.this.state != 3) {
                try {
                    BluetoothSocket socket = this.serverSocket.accept();
                    if (socket != null) {
                        synchronized (ChatService.this) {
                            switch (ChatService.this.state) {
                                case 0:
                                case 3:
                                    try {
                                        socket.close();
                                        break;
                                    } catch (IOException e) {
                                        break;
                                    }
                                case 1:
                                case 2:
                                    ChatService.this.connected(socket, socket.getRemoteDevice(), this.socketType);
                                    break;
                            }
                        }
                    }
                } catch (IOException e2) {
                    return;
                }
            }
            return;
        }

        public void cancel() {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothDevice device;
        private final BluetoothSocket socket;
        private String socketType;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            this.device = device;
            BluetoothSocket tmp = null;
            this.socketType = secure ? "Secure" : "Insecure";
            if (secure) {
                try {
                    tmp = device.createRfcommSocketToServiceRecord(ChatService.MY_UUID_SECURE);
                } catch (IOException e) {
                }
            } else {
                try {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(ChatService.MY_UUID_INSECURE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.socket = tmp;
        }

        private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
            if (VERSION.SDK_INT >= 10) {
                try {
                    return (BluetoothSocket) device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class}).invoke(device, new Object[]{ChatService.MY_UUID_INSECURE});
                } catch (Exception e) {
                    Log.e("ContentValues", "Could not create Insecure RFComm Connection", e);
                }
            }
            return device.createRfcommSocketToServiceRecord(ChatService.MY_UUID_INSECURE);
        }

        public void run() {
            setName("ConnectThread" + this.socketType);
            ChatService.this.bluetoothAdapter.cancelDiscovery();
            try {
                this.socket.connect();
                synchronized (ChatService.this) {
                    ChatService.this.connectThread = null;
                }
                ChatService.this.connected(this.socket, this.device, this.socketType);
            } catch (IOException e) {
                try {
                    this.socket.close();
                } catch (IOException e2) {
                }
                ChatService.this.connectionFailed();
            }
        }

        public void cancel() {
            try {
                this.socket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public ConnectedThread(BluetoothSocket socket, String socketType) {
            this.bluetoothSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            this.inputStream = tmpIn;
            this.outputStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            while (true) {
                try {
                    ChatService.this.handler.obtainMessage(2, this.inputStream.read(buffer), -1, buffer).sendToTarget();
                } catch (IOException e) {
                    ChatService.this.connectionLost();
                    ChatService.this.start();
                    return;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                this.outputStream.write(buffer);
                ChatService.this.handler.obtainMessage(3, -1, -1, buffer).sendToTarget();
            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                this.bluetoothSocket.close();
            } catch (IOException e) {
            }
        }
    }

    public ChatService(Context context, Handler handler) {
        this.handler = handler;
    }

    private synchronized void setState(int state) {
        this.state = state;
        this.handler.obtainMessage(1, state, -1).sendToTarget();
    }

    public synchronized int getState() {
        return this.state;
    }

    public synchronized void start() {
        if (this.connectThread != null) {
            this.connectThread.cancel();
            this.connectThread = null;
        }
        if (this.connectedThread != null) {
            this.connectedThread.cancel();
            this.connectedThread = null;
        }
        setState(1);
        if (this.secureAcceptThread == null) {
            this.secureAcceptThread = new AcceptThread(true);
            this.secureAcceptThread.start();
        }
        if (this.insecureAcceptThread == null) {
            this.insecureAcceptThread = new AcceptThread(false);
            this.insecureAcceptThread.start();
        }
    }

    public synchronized void connect(BluetoothDevice device, boolean secure) {
        if (this.state == 2 && this.connectThread != null) {
            this.connectThread.cancel();
            this.connectThread = null;
        }
        if (this.connectedThread != null) {
            this.connectedThread.cancel();
            this.connectedThread = null;
        }
        this.connectThread = new ConnectThread(device, secure);
        this.connectThread.start();
        setState(2);
    }

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device, String socketType) {
        if (this.connectThread != null) {
            this.connectThread.cancel();
            this.connectThread = null;
        }
        if (this.connectedThread != null) {
            this.connectedThread.cancel();
            this.connectedThread = null;
        }
        if (this.secureAcceptThread != null) {
            this.secureAcceptThread.cancel();
            this.secureAcceptThread = null;
        }
        if (this.insecureAcceptThread != null) {
            this.insecureAcceptThread.cancel();
            this.insecureAcceptThread = null;
        }
        this.connectedThread = new ConnectedThread(socket, socketType);
        this.connectedThread.start();
        Message msg = this.handler.obtainMessage(4);
        Bundle bundle = new Bundle();
        bundle.putString(BlueToothMainActivity.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        this.handler.sendMessage(msg);
        setState(3);
    }

    public synchronized void stop() {
        if (this.connectThread != null) {
            this.connectThread.cancel();
            this.connectThread = null;
        }
        if (this.connectedThread != null) {
            this.connectedThread.cancel();
            this.connectedThread = null;
        }
        if (this.secureAcceptThread != null) {
            this.secureAcceptThread.cancel();
            this.secureAcceptThread = null;
        }
        if (this.insecureAcceptThread != null) {
            this.insecureAcceptThread.cancel();
            this.insecureAcceptThread = null;
        }
        setState(0);
    }

    public void write(byte[] out) {
        synchronized (this) {
            if (this.state != 3) {
                return;
            }
            ConnectedThread r = this.connectedThread;
            r.write(out);
        }
    }

    private void connectionFailed() {
        Message msg = this.handler.obtainMessage(5);
        Bundle bundle = new Bundle();
        bundle.putString(BlueToothMainActivity.TOAST, "Unable to connect device");
        msg.setData(bundle);
        this.handler.sendMessage(msg);
        start();
    }

    private void connectionLost() {
        Message msg = this.handler.obtainMessage(5);
        Bundle bundle = new Bundle();
        bundle.putString(BlueToothMainActivity.TOAST, "Device connection was lost");
        msg.setData(bundle);
        this.handler.sendMessage(msg);
        start();
    }
}
