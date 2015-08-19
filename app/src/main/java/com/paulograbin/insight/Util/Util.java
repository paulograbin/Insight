package com.paulograbin.insight.Util;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by paulograbin on 30/06/15.
 */
public class Util {

    final static String LOG_TAG = "Spiga";

    public static void playSound(Context context) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copiaArquivos(File source, File destination) throws IOException {
        if (!destination.exists())
            destination.createNewFile();

        InputStream in = new FileInputStream(source);
        OutputStream out = new FileOutputStream(destination);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();

        Log.i(LOG_TAG, "Copiando arquivos...");
        Log.i(LOG_TAG, "Source: " + source.getPath());
        Log.i(LOG_TAG, "Destination: " + destination.getPath());
    }


    public static void copiaOutroJeito(File sourceFile, File destinationFile) throws IOException {
        if (!destinationFile.exists()) {
            destinationFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destinationFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }

        Log.i(LOG_TAG, "Copiando arquivos...");
        Log.i(LOG_TAG, "Source: " + sourceFile.getPath());
        Log.i(LOG_TAG, "Destination: " + destinationFile.getPath());
    }
}
