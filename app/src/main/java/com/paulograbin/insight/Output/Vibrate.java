package com.paulograbin.insight.Output;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by paulograbin on 13/09/15.
 */
public class Vibrate {

    private Vibrator vibrator;
    private long[] pattern = new long[4];

    public Vibrate(Context context){
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        for(int i = 0; i < 4; i++){
            if(i % 2 == 0)
                pattern[i] = 100;
            else
                pattern[i] = 400;
        }
    }

    public void onVibrate(){
        vibrator.vibrate(pattern, -1);
    }

    public void onStop(){
        vibrator.cancel();
    }
}
