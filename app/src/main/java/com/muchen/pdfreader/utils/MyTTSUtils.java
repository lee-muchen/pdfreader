package com.muchen.pdfreader.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.speech.tts.TextToSpeech;

public class MyTTSUtils {
    private static String text;
    private static float Pitch;//语调
    private static float SpeechRate;//语速
    public MyTTSUtils(){
        this.Pitch=1.0f;
        SpeechRate=1.0f;
    }
    public void play(String str,TextToSpeech tts){
        if (!TextUtils.isEmpty(str)){
            //播放语音
            tts.setPitch(Pitch);
            tts.setSpeechRate(SpeechRate);
            tts.speak(str, TextToSpeech.QUEUE_ADD, null,null);
        }
    }
}
