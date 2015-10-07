package com.paulograbin.insight.Activity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.paulograbin.insight.Bluetooth.BluetoothService;
import com.paulograbin.insight.Output.Speaker;

import org.altbeacon.beacon.Beacon;

/**
 * Created by paulograbin on 06/09/15.
 */
public abstract class ServiceActivity extends AppCompatActivity {

    private static final String TAG = "ServiceActivity";
    private BluetoothAdapter mBluetoothAdapter;
    protected BluetoothService mBluetoothService;
    private Speaker mSpeaker;
    private int BLUETOOTH_REQUEST = 1;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSpeaker = Speaker.getInstance(this);
        registerAsReceiver();
    }

    protected abstract void onBeaconReceived(Beacon lastSeenBeacon);

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

    protected void say(String text) {
        mSpeaker.playWithoutAlert(text);
    }

    protected void sayImmediately(String text) {
        mSpeaker.sayImmediately(text);
    }

    protected void sayWithAlert(String text) {
        mSpeaker.playWithAlert(text);
    }

    private void printToLog(String message) {
        boolean debugServiceActivity = true;

        if (debugServiceActivity)
            Log.i(TAG, message);
    }

    public Boolean checkBluetoothIsSupported() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null;
    }

    public void askUserToEnableBluetooth() {

        if (!isBluetoothOn()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, BLUETOOTH_REQUEST);
            Log.i(TAG, "Usuário respondeu:" + BLUETOOTH_REQUEST);
        }
    }

    protected boolean isBluetoothOn() {
        return mBluetoothAdapter.isEnabled();
    }

    public Boolean checkIsBLEisSupported() {
        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }
}
