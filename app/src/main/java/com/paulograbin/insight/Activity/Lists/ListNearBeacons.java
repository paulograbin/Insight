package com.paulograbin.insight.Activity.Lists;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.WindowManager;

import com.paulograbin.insight.Adapter.NearBeaconListAdapter;
import com.paulograbin.insight.Bluetooth.BeaconLayout;
import com.paulograbin.insight.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;


public class ListNearBeacons extends ListActivity implements BeaconConsumer {
    public static final String TAG = "Spiga";
    private static final String UNIQUE_RANGING_ID = "FarolBeaconUniqueId";
    BeaconManager mBeaconManager;
    ArrayList<Beacon> mBeacons;
    NearBeaconListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_nearbeacons);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mBeacons = new ArrayList<>();
        mAdapter = new NearBeaconListAdapter(this, mBeacons);
        setListAdapter(mAdapter);

        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconLayout.IBEACON.layout()));
        mBeaconManager.bind(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Tells you if the passed beacon consumer is bound to the service
        if (mBeaconManager.isBound(this))

            // This method notifies the beacon service that the application is either moving to background mode or foreground mode.
            mBeaconManager.setBackgroundMode(true);

        mBeaconManager.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Tells you if the passed beacon consumer is bound to the service
        if (!mBeaconManager.isBound(this))
            mBeaconManager.bind(this);

        if (mBeaconManager.isBound(this))
            mBeaconManager.setBackgroundMode(false);

        ((NearBeaconListAdapter) getListAdapter()).notifyDataSetChanged();
    }

    // Called when the beacon service is running and ready to accept your commands through the BeaconManager
    @Override
    public void onBeaconServiceConnect() {
        //Specifies a class that should be called each time the BeaconService gets ranging data,
        // which is nominally once per second when beacons are detected.
        mBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                mBeacons.clear();
                if (beacons.size() > 0) {
                    Log.i(TAG, "Foram encontrados " + beacons.size() + " beacons.");

                    ArrayList<Beacon> meusBeacons = new ArrayList<Beacon>();
                    meusBeacons.addAll(beacons);

                    for (int i = 0; i < beacons.size(); i++) {
                        Beacon b = meusBeacons.get(i);

                        Log.i(TAG, "BluetoothAddress " + b.getBluetoothAddress());
                        Log.i(TAG, "BluetoothName " + b.getBluetoothName());
                        Log.i(TAG, "ToString " + b.toString());
                        Log.i(TAG, "DISTANCE MOTHER " + b.getDistance());
                        Log.i(TAG, "ExtraDataFields " + b.getExtraDataFields());
                        Log.i(TAG, "BeaconTypeCode " + b.getBeaconTypeCode());
                        Log.i(TAG, "Id1 " + b.getId1());
                        Log.i(TAG, "Id2 " + b.getId2());
                        Log.i(TAG, "Id3 " + b.getId3());
                        Log.i(TAG, "Identifiers " + b.getIdentifiers().toString());
                        Log.i(TAG, "Manufacturer " + b.getManufacturer());
                        Log.i(TAG, "Rssi " + b.getRssi());
                        Log.i(TAG, "ServiceUUID " + b.getServiceUuid());
                        Log.i(TAG, "TxPower " + b.getTxPower());
                        Log.i(TAG, "Hashcode " + b.hashCode());

                        mBeacons.add(b);
                    }
                }

                mAdapter.notifyDataSetChanged();

                if (this != null) runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region(UNIQUE_RANGING_ID, null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}