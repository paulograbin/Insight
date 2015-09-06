package com.paulograbin.insight.Bluetooth;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by paulograbin on 01/08/15.
 */
public class BluetoothService extends IntentService implements BeaconConsumer {

    public static final String FOUND_NEW_BEACON_EVENT = "foundNewBeacon";
    public static final String BEACON_KEY = "beacon";
    public static final String LOG_TAG = "bluetoothService";

    private final IBinder mBinder = new LocalBinder();
    private BeaconManager mBeaconManager;
    private Beacon mLastSeenBeacon;
    private int mBindCount;
    private boolean debugServiceExecution = true;


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
        mBindCount = 0;
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
                if (beacons.size() == 0)
                    return;

                Beacon closerBeacon = getCloserBeacon(beacons);

                onCloserBeaconFound(closerBeacon);
            }
        });

        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region("teste", null, null, null));
        } catch (RemoteException e) {
            printToLog(e.getMessage() + e.toString());
        }
    }

    @Nullable
    private Beacon getCloserBeacon(Collection<Beacon> beacons) {
//        printToLog("getCloserBeacon foi chamado com " + beacons.size() + " beacons.");

        if (beacons.size() == 1) {
            return beacons.iterator().next();
        }

        Iterator<Beacon> i = beacons.iterator();

        Beacon closerBeacon = i.next();
        printToLog("Assumindo beacon mais proximo: " + closerBeacon.getId1().toString());

        while (i.hasNext()) {
            printToLog("Verificando se existe beacon ainda mais proximo...");
            Beacon auxBeacon = i.next();

            if (closerBeacon.getDistance() > auxBeacon.getDistance()) {
                closerBeacon = auxBeacon;
                printToLog("Assumindo novo beacon mais proximo: " + closerBeacon.getId1().toString());
            }
        }

        return closerBeacon;
    }

    private void onCloserBeaconFound(Beacon beacon) {
        if (!beacon.equals(mLastSeenBeacon)) {
            printToLog("Ultimo beacon encontrado é " + beacon.getId1().toString());
            mLastSeenBeacon = beacon;

            printToLog("Enviando broadcast");
            Intent intent = new Intent(FOUND_NEW_BEACON_EVENT);
            intent.putExtra(BEACON_KEY, mLastSeenBeacon);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        printToLog("On handle intent...");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        printToLog("Desbindaram");
        mBindCount--;
        return super.onUnbind(intent);
    }

    private void printToLog(String msg) {
        if (debugServiceExecution)
            Log.i(LOG_TAG, msg);
    }

    @Override
    public IBinder onBind(Intent intent) {
        printToLog("Bindaram");
        mBindCount++;
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public BluetoothService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BluetoothService.this;
        }
    }
}
