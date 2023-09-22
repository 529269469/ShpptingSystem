package com.fhxh.shpptingsystem;

import android.app.Application;
import android.os.Environment;

import com.fhxh.shpptingsystem.utils.FileUtils;
import com.hjq.toast.ToastUtils;
import com.tencent.mmkv.MMKV;

import org.opencv.android.OpenCVLoader;

import java.io.File;


public class MyApp extends Application {

    private static MyApp myApplication = null;

    public static MyApp getApplication() {
        return myApplication;
    }

    private String TAG="MyApp";
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        // 设置初始化的根目录
        String dir = getFilesDir().getAbsolutePath() + "/mmkv_2";
        String rootDir = MMKV.initialize(dir);
//        Log.e(TAG, "mmkv root: " + rootDir);
        // 初始化 Toast 框架
        ToastUtils.init(this);

        boolean b = OpenCVLoader.initDebug();





    }
}
