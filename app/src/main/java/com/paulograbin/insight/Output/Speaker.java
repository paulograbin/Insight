package com.paulograbin.insight.Output;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.paulograbin.insight.R;

import java.util.Locale;

/**
 * Created by paulograbin on 01/07/15.
 */
public class Speaker implements TextToSpeech.OnInitListener {

    private static Speaker sSpeaker;
    private TextToSpeech tts;
    private boolean ready;


    private Speaker(Context context) {
        ready = false;
        tts = new TextToSpeech(context, this);
    }

    public static Speaker getInstance(Context context) {
        if(sSpeaker == null)
            sSpeaker = new Speaker(context);

        return sSpeaker;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.getDefault());
            tts.addEarcon("beep", "com.paulograbin.insight", R.raw.music_marimba_confirm);

            ready = true;

            Log.i("Speaker", "Speaker bombando");
        } else {
            Log.i("Speaker", "Speaker fail");
        }
    }

    public void playWithoutAlert(String text) {
        Log.i("speaker", "Saying: " + text);

        if (ready)
            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
        else
            Log.i("Speaker", "Speaker not ready...");
    }

    public void playWithAlert(String text) {
        Log.i("speaker", "Saying with beep: " + text);

        if (ready) {
            tts.playEarcon("beep", TextToSpeech.QUEUE_ADD, null);
            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
        else
            Log.i("Speaker", "Speaker not ready...");
    }

    public void sayImmediately(String text) {
        Log.i("speaker", "Saying immediately: " + text);

        if (ready) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            tts.playEarcon("beep", TextToSpeech.QUEUE_FLUSH, null);
        }
        else {
            Log.i("Speaker", "Speaker not ready...");
        }
    }
}
