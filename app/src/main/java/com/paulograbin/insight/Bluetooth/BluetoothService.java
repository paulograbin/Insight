package com.paulograbin.insight.Bluetooth;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

/**
 * Created by paulograbin on 01/08/15.
 */
public class BluetoothService extends IntentService implements BeaconConsumer {

    private final IBinder mBinder = new LocalBinder();
    private BeaconManager mBeaconManager;
    private Beacon mLastSeenBeacon;
    private int mBindCount = 0;
    private boolean debug = true;


    public BluetoothService() {
        super("myBluetoothService");
    }

    public int getBindCount() {
        return mBindCount;
    }

    public Beacon getLastSeenBeacon() {
        return mLastSeenBeacon;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        printToLog("Serviço criado...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        printToLog("Serviço started with command");

        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        mBeaconManager.bind(this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        printToLog("Serviço destruído...");

        mBeaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        printToLog("onBeaconServiceConnected");

        mBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() == 1) {
//                    printToLog(beacons.size() + " beacon encontrado.");

                    Beacon beacon = beacons.iterator().next();
                    if (!beacon.equals(mLastSeenBeacon)) {
                        printToLog("Novo beacon detectado, " + beacon.getId1().toString());
                        mLastSeenBeacon = beacon;
                    } else {
//                        printToLog("Mesmo beacon...");
                    }


                } else if (beacons.size() > 1) {
                    printToLog(beacons.size() + " beacons encontrado.");
                }
            }
        });

        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region("teste", null, null, null));
        } catch (RemoteException e) {
            printToLog(e.getMessage() + e.toString());
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        printToLog("On handle intent...");
    }

    @Override
    public IBinder onBind(Intent intent) {
        printToLog("Bindaram");
        mBindCount++;
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        printToLog("Desbindaram");
        mBindCount--;
        return super.onUnbind(intent);
    }

    private void printToLog(String msg) {
        if (debug)
            Log.i("Spiga", msg);
    }

    public class LocalBinder extends Binder {
        public BluetoothService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BluetoothService.this;
        }
    }
}
