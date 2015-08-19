package com.paulograbin.insight.Output;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by paulograbin on 01/07/15.
 */
public class Speaker implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;

    public Speaker(Context context) {
        tts = new TextToSpeech(context, this);
    }

    public void say(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.ERROR) {
            tts.setLanguage(Locale.UK);
        }
    }
}
