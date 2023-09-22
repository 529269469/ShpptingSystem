package com.fhxh.shpptingsystem.sdk;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {


    public static String getTime() {


        Date date = new Date();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss: SSS");

        String reStr = dateFormat.format(date);

        return reStr;


    }

    public static String getDateTime() {


        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String reStr = dateFormat.format(date);

        return reStr;


    }

    /**
     * 16进制字符串转换成为string类型字符串
     *
     * @param s 十六进制字符串
     * @return string
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     *  获取当前程序版本
     * @param context context
     * @return string
     */
    public static String getAppVersionName(Context context) {
        String versionName = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }



    /**
     * 格式化字符串，不足位数前补0
     * @param str 需要格式化的字符串
     * @param strLength 长度
     * @return 字符串
     */
    public static String addZeroForStr(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sbuf = new StringBuffer();
                sbuf.append("0").append(str);// 补0
                str = sbuf.toString();
                strLen = str.length();
            }
        }

        return str;
    }


}
