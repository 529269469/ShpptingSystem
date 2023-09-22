package com.fhxh.shpptingsystem.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileUtils {
    public static void CopyAssets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(newPath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    CopyAssets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                    // buffer字节
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap stringToBitmap(String string) {
        String value = string;
        if (string.contains(",")) {
            value = string.split(",")[1];
        }
        byte[] bitmapArray = Base64.decode(value, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    }


    public static Bitmap getImage(String Url) throws Exception {

        try {

            URL url = new URL(Url);

            String responseCode = url.openConnection().getHeaderField(0);

            if (responseCode.indexOf("200") < 0)

                throw new Exception("图片文件不存在或路径错误，错误代码：" + responseCode);

            return BitmapFactory.decodeStream(url.openStream());

        } catch (IOException e) {

            // TODO Auto-generated catch block

            throw new Exception(e.getMessage());

        }

    }

  
    public static  Bitmap returnBitMap(final String url){
        Bitmap bitmap = null;
            URL imageurl = null;

            try {
                imageurl = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection conn = (HttpURLConnection)imageurl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return bitmap;
    }

}
