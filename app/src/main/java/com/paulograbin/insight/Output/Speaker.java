package com.paulograbin.insight.Output;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * Created by paulograbin on 01/07/15.
 */
public class Speaker implements TextToSpeech.OnInitListener {

    private static Speaker sSpeaker;
    private TextToSpeech tts;
    private boolean ready;

    public boolean isReady() {
        return ready;
    }

    public static Speaker getInstance(Context context) {
        if(sSpeaker == null)
            sSpeaker = new Speaker(context);

        return sSpeaker;
    }

    private Speaker(Context context) {
        ready = false;
        tts = new TextToSpeech(context, this);
    }

    public void say(String text) {
        if(ready)
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.getDefault());
            ready = true;
            Log.i("Speaker", "Speaker bombando");
        } else {
            Log.i("Speaker", "Speaker fail");
        }
    }
}
