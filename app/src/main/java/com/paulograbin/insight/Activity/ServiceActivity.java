package com.paulograbin.insight.Activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.paulograbin.insight.Bluetooth.BluetoothService;

import org.altbeacon.beacon.Beacon;

import java.util.Locale;

/**
 * Created by paulograbin on 06/09/15.
 */
public abstract class ServiceActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private BluetoothService mBluetoothService;
    private static final String TAG = "ServiceActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerAsReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();

        tts = new TextToSpeech(this, this);
    }

    private void registerAsReceiver() {
        try {
            Log.i("Spiga", "Chamando service");
            Intent intent = new Intent(this, BluetoothService.class);
            startService(intent);

            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(BluetoothService.FOUND_NEW_BEACON_EVENT));
        } catch (Exception e) {
            Log.i("Spiga", "Deu merda... " + e.getClass());
            e.printStackTrace();
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) service;
            mBluetoothService = binder.getService();
            Log.i("Spiga", "Service binded");
        }

        public void onServiceDisconnected(ComponentName className) {
            printToLog("onServiceDisconnected");
            mBluetoothService = null;
        }
    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Beacon receivedBeacon = intent.getParcelableExtra(BluetoothService.BEACON_KEY);
            printToLog(this.getClass().getName() + " recebeu um beacon, " + receivedBeacon.getId1().toString());

            onBeaconReceived(receivedBeacon);
        }
    };

    protected abstract void onBeaconReceived(Beacon lastSeenBeacon); //{
//        try {
//            mLastSeenBeacon = lastSeenBeacon;
//
//            PlaceBeacon pb = mPlaceBeaconProvider.getByUUID(mLastSeenBeacon.getId1().toString());
//            mCurrentPlace = mPlaceProvider.getByID(pb.getIdPlace());
//
//            BeaconProvider beaconProvider = new BeaconProvider(this);
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String message = "Você está em " + mCurrentPlace.getName();
//
//                    txtCurrentPlace.setText(message);
//                    say(message + ", " + mCurrentPlace.getMessage());
//                }
//            });
//        } catch (Exception e) {
//            printToLog("Beacon com uuid " + lastSeenBeacon.getId1().toString() + " não cadastrado, ignorando...");
//        }
//    }

    protected void say(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.getDefault());
        } else {
            Toast.makeText(this, "pau no TTS", Toast.LENGTH_SHORT).show();
        }
    }

    private void printToLog(String message) {
        boolean debugServiceActivity = true;

        if (debugServiceActivity)
            Log.i(TAG, message);
    }
}
