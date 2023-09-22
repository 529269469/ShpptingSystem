package com.fhxh.shpptingsystem.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;


import com.fhxh.shpptingsystem.base_app.Constant;
import com.tencent.mmkv.MMKV;

import java.util.Locale;

/**
 * Created by  on 2021/7/30 10:56.
 */
public class SpeechUtils {
    private Context context;

    private static boolean isInit = false;
    private static final String TAG = "SpeechUtils";
    private static SpeechUtils singleton;

    private static TextToSpeech textToSpeech; // TTS对象

    public static SpeechUtils getInstance(Context context) {
        if (singleton == null) {
            synchronized (SpeechUtils.class) {
                if (singleton == null) {
                    singleton = new SpeechUtils(context);
                }
            }
        }
        return singleton;
    }

    public SpeechUtils(Context context) {
        this.context = context;
        textToSpeech = new TextToSpeech(context, i -> {
            if (i == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
                int languageAvailable = textToSpeech.isLanguageAvailable(Locale.SIMPLIFIED_CHINESE);
                textToSpeech.setPitch(1.0f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
                textToSpeech.setSpeechRate(1.0f);
                isInit = true;
            } else {
                Log.e(TAG, "初始化失败");
            }
        });
    }

    public static void SpeakVoice(String st){
        Log.e(TAG, "SpeakVoice: "+st );
        textToSpeech.speak(st,
                TextToSpeech.QUEUE_ADD, null,"1");
    }

    public static void SpeakVoice2(String st){
        Log.e(TAG, "SpeakVoice: "+st );
        MMKV defaultMMKV = MMKV.defaultMMKV();
        boolean setting_play = defaultMMKV.decodeBool("dialog_setting_play");
        if (setting_play){
            textToSpeech.speak(st,
                    TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public static boolean IsInit(){
        return isInit;
    }

    public static void stop() {
        isInit = false;
        if (textToSpeech != null) {
            textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
            textToSpeech.shutdown(); // 关闭，释放资源
            singleton = null;
        }
    }
}
