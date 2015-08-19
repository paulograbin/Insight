package com.paulograbin.insight.Bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by paulograbin on 01/08/15.
 */
public class BluetoothService extends Service {

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

//    private void verifyBluetooth() {
//
//        try {
//            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
//                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Bluetooth not enabled");
//                builder.setMessage("Please enable bluetooth in settings and restart this application.");
//                builder.setPositiveButton(android.R.string.ok, null);
//                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        finish();
//                        System.exit(0);
//                    }
//                });
//                builder.show();
//            }
//        }
//        catch (RuntimeException e) {
//            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Bluetooth LE not available");
//            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
//            builder.setPositiveButton(android.R.string.ok, null);
//            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    finish();
//                    System.exit(0);
//                }
//
//            });
//            builder.show();
//
//        }
//
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
