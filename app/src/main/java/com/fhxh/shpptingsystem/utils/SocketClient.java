package com.fhxh.shpptingsystem.utils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by  on 2022/2/17.
 */

public class SocketClient {
    /**
     * single instance TcpClient
     */
    private static SocketClient mSocketClient = null;

    private SocketClient() {
    }

    public static SocketClient getInstance() {
        if (mSocketClient == null) {
            synchronized (SocketClient.class) {
                mSocketClient = new SocketClient();
            }
        }
        return mSocketClient;
    }


    String TAG_log = "Socket";
    private Socket mSocket;

    private OutputStream mOutputStream;
    private BufferedWriter writer;
    private InputStream mInputStream;

    private SocketThread mSocketThread;
    private boolean isStop = false;//thread flag



    // 线程池
    // 为了方便展示,此处直接采用线程池进行线程管理,而没有一个个开线程
    private ExecutorService mThreadPool;
    /**
     * - 数据按照最长接收，一次性
     */
    private class SocketThread extends Thread {

        private String ip;
        private int port;

        public SocketThread(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        @SuppressLint("StaticFieldLeak")
        @Override
        public void run() {
            Log.e(TAG_log, "开始连接总控");
            Log.e(TAG_log, ip+ port);
            super.run();
            // 初始化线程池
            mThreadPool = Executors.newCachedThreadPool();
            try {
                if (mSocket != null) {
                    mSocket.close();
                    mSocket = null;
                }
                try {
                    mSocket = new Socket(ip, port);
                    mSocket.setKeepAlive(true);
                    //阻塞停止，表示连接成功
                    Log.e(TAG_log, "连接成功");
                } catch (Exception e) {
                    Log.e(TAG_log, "连接服务器时异常");
                    e.printStackTrace();
                    return;
                }


                if (isConnect()) {
                    Log.e(TAG_log, "SocketThread connect 连接成功");
                    mOutputStream = mSocket.getOutputStream();
                    mInputStream = mSocket.getInputStream();
                    writer = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                    isStop = false;
                    uiHandler.sendEmptyMessage(1);


                    // 利用线程池直接开启一个线程 & 执行该线程
                    mThreadPool.execute(() -> {
                        try {
                            // 步骤1：创建输入流对象InputStream
                            InputStream   is = mSocket.getInputStream();
                            // 步骤2：创建输入流读取器对象 并传入输入流对象
                            // 该对象作用：获取服务器返回的数据
                            InputStreamReader  isr = new InputStreamReader(is);
                            BufferedReader  br = new BufferedReader(isr);

                            // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据
                            String  response = br.readLine();

                            // 步骤4:通知主线程,将接收的消息显示到界面
                            Message msg = Message.obtain();
                            msg.what = 1001;
                            msg.obj = response;
                            uiHandler.sendMessage(msg);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });


                }
                /* 此处这样做没什么意义不大，真正的socket未连接还是靠心跳发送，等待服务端回应比较好，一段时间内未回应，则socket未连接成功 */
                else {
                    uiHandler.sendEmptyMessage(-1);
                    Log.e(TAG_log, "SocketThread connect fail");
                    return;
                }

            } catch (IOException e) {
                uiHandler.sendEmptyMessage(-1);
                Log.e(TAG_log, "SocketThread connect io exception = " + e.getMessage());
                e.printStackTrace();
                return;
            }
            Log.e(TAG_log, "SocketThread connect over ");

        }
    }


    //==============================socket connect============================


    /**
     * 发送消息
     */
    public void sendStrSocket(final String senddata) {
        new Thread(() -> {
            try {
//                Log.e(TAG_log, "sendStrSocket:" + senddata);
                writer.write(senddata);//"utf-8"
                writer.flush();
            } catch (Exception e) {
//                Log.e(TAG_log, "sendStrSocket:" + e.getMessage());
            }
        }).start();

    }


    /**
     * connect socket in thread
     * Exception : android.os.NetworkOnMainThreadException
     */
    public void connect(String ip, int port) {
        mSocketThread = new SocketThread(ip, port);
        mSocketThread.start();
    }

    /**
     * socket is connect
     */
    public boolean isConnect() {
        boolean flag = false;
        if (mSocket != null) {
            flag = mSocket.isConnected();
        }
        return flag;
    }

    /**
     * socket disconnect
     */
    public void disconnect() {
        isStop = true;
        try {
            if (mOutputStream != null) {
                mOutputStream.close();
            }

            if (mInputStream != null) {
                mInputStream.close();
            }

            if (mSocket != null) {
                mSocket.close();
                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mSocketThread != null) {
            mSocketThread.interrupt();//not intime destory thread,so need a flag
        }
    }


    /**
     * send byte[] cmd
     * Exception : android.os.NetworkOnMainThreadException
     */
    public void sendByteCmd(final byte[] mBuffer, int requestCode) {
        this.requestCode = requestCode;

        new Thread(() -> {
            try {
                if (mOutputStream != null) {
                    mOutputStream.write(mBuffer);
                    mOutputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }


    /**
     * send string cmd to serial
     */
    public void sendStrCmds(String cmd, int requestCode) {
        byte[] mBuffer = cmd.getBytes();
        sendByteCmd(mBuffer, requestCode);
    }


    /**
     * send prt content cmd to serial
     */
    public void sendChsPrtCmds(String content, int requestCode) {
        try {
            byte[] mBuffer = content.getBytes("GB2312");
            sendByteCmd(mBuffer, requestCode);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
    }


    Handler uiHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                //connect error
                case -1:
                    if (null != onDataReceiveListener) {
                        onDataReceiveListener.onConnectFail();
                        disconnect();
                    }
                    break;

                //connect success
                case 1:
                    if (null != onDataReceiveListener) {
                        onDataReceiveListener.onConnectSuccess();
                    }
                    break;

                //receive data
                case 100:
                    Bundle bundle = msg.getData();
                    byte[] buffer = bundle.getByteArray("data");
                    int size = bundle.getInt("size");
                    int mequestCode = bundle.getInt("requestCode");
                    if (null != onDataReceiveListener) {
                        onDataReceiveListener.onDataReceive(buffer, size, mequestCode);
                    }
                    break;
                case 1001:
                    if (null != onDataReceiveListener) {
                        if (msg.obj!=null){
                            onDataReceiveListener.onSocketCOntent(msg.obj.toString());
                        }
                    }
                    break;
            }
            return false;
        }
    });


    /**
     * socket response data listener
     */
    private OnDataReceiveListener onDataReceiveListener = null;
    private int requestCode = -1;

    public interface OnDataReceiveListener {
        public void onConnectSuccess();

        public void onConnectFail();

        public void onSocketCOntent(String msg);

        public void onDataReceive(byte[] buffer, int size, int requestCode);
    }

    public void setOnDataReceiveListener(
            OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }



}
