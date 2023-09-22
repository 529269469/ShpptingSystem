package com.fhxh.shpptingsystem.utils;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.fhxh.shpptingsystem.base_app.Constant;
import com.fhxh.shpptingsystem.ui.bean.DeleteEvent;
import com.fhxh.shpptingsystem.utils.serial.SerialManage;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerService extends Service {
    private boolean isServiceDestroyed = false;
    private static final String TAG = "SocketServerService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new TcpServer()).start();
    }

    private class TcpServer implements Runnable {
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            while (!isServiceDestroyed) {
                try {
                    if (serverSocket == null) {
                        return;
                    }
                    Log.e(TAG, "ServerSocket loop listen ClientSocket");
                    //接收客户端的请求，并且阻塞直到接收到消息
                    final Socket client = serverSocket.accept();
                    new Thread(() -> {
                        try {
                            responseClient(client);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }).start();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

        }
    }

    private String number = "0";

    private void responseClient(Socket client) throws IOException {
        // 用于接收客户端的消息
        InputStream inputStream = client.getInputStream();
        InputStreamReader isr = new InputStreamReader(client.getInputStream(), "UTF-8");
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            String text = new String(buffer, 0, len);
            Log.e(TAG, "收到的数据为：" + text);


            if (text.contains("自主训练")) {
                SerialManage.getInstance().send("90AA55005501E4");
            }
            if (text.contains("统一训练")) {
                SerialManage.getInstance().send("90AA5580550264");
            }
            if (text.contains("结束训练")) {
                SerialManage.getInstance().send("90AA5580550264");
            }
            if (text.contains("num")) {
                number= text.substring(4);
                Log.e(TAG, "responseClient: " + number);
            }
            if (text.contains("name")) {
                switch (number){
                    case "5":
                        SerialManage.getInstance().send("90AA55C55502A9");
                        break;
                    case "10":
                        SerialManage.getInstance().send("90AA55CA5502AE");
                        break;
                    case "15":
                        SerialManage.getInstance().send("90AA55CF5502B3");
                        break;
                    case "20":
                        SerialManage.getInstance().send("90AA55D45502B8");
                        break;
                    case "25":
                        SerialManage.getInstance().send("90AA55D95502BD");
                        break;
                    case "30":
                        SerialManage.getInstance().send("90AA55DE5502C2");
                        break;
                }
                EventBus.getDefault().post(new DeleteEvent(text));
            }

            // 必须先关闭输入流才能获取下面的输出流
            client.shutdownInput();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceDestroyed = true;
    }

    private SocketClient socketClient;

    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
    }

    public interface SocketClient {
        Bitmap getBitmap(Bitmap bitmap);
    }
}
