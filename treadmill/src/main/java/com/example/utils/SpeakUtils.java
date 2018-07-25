package com.example.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

/**
 * Created by wr-app1 on 2018/3/23.
 */

public class SpeakUtils {
    public static void initialize(Context context, String msg){
        TextToSpeech tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    Log.i("语音初始化成功","");
                }else {
                    Log.i("语音初始化失败","");
                }
            }
        });
        tts.setPitch(1.0f);
        tts.setSpeechRate(0.3f);
        if (msg.length() >= 1){
            tts.speak(msg,TextToSpeech.QUEUE_ADD,null);
        }
    }
}
