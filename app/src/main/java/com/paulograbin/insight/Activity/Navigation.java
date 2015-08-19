package com.paulograbin.insight.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.paulograbin.insight.Bluetooth.BeaconLayout;
import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.LocationEngine.RouteFinder;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.Model.PlaceBeacon;
import com.paulograbin.insight.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

public class Navigation extends Activity implements BeaconConsumer {

    //    private static final String UNIQUE_RANGING_ID = "FarolBeaconUniqueId";
    public static final String TAG = "Spiga";
    Place currentPlace;
    TextView txtCurrentPlace;
    Button btnAdminPanel;
    Button btnChooseDestiny;
    String texto;

    BeaconManager mBeaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        btnAdminPanel = (Button) findViewById(R.id.btnAdminPanel);
        btnAdminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        btnChooseDestiny = (Button) findViewById(R.id.btnChooseDestination);
        btnChooseDestiny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteFinder rf = new RouteFinder(getApplicationContext(), currentPlace.getId(), 105l);
                rf.findWayToPlace();
            }
        });

        txtCurrentPlace = (TextView) findViewById(R.id.txtCurrentPlace);
        txtCurrentPlace.setText("Nenhum beacon foi detectado ainda...");


        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconLayout.IBEACON.layout()));
        mBeaconManager.bind(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Tells you if the passed beacon consumer is bound to the service
        if (mBeaconManager.isBound(this))

            mBeaconManager.unbind(this);
        // This method notifies the beacon service that the application is either moving to background mode or foreground mode.
//            mBeaconManager.setBackgroundMode(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Tells you if the passed beacon consumer is bound to the service
        if (!mBeaconManager.isBound(this))
            mBeaconManager.bind(this);

        if (mBeaconManager.isBound(this))
            mBeaconManager.setBackgroundMode(false);
    }

    // Called when the beacon service is running and ready to accept your commands through the BeaconManager
    @Override
    public void onBeaconServiceConnect() {
        //Specifies a class that should be called each time the BeaconService gets ranging data,
        // which is nominally once per second when beacons are detected.
        mBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.i(TAG, "Foram encontrados " + beacons.size() + " beacons.");

                    ArrayList<Beacon> meusBeacons = new ArrayList<Beacon>();
                    meusBeacons.addAll(beacons);


                    for (int i = 0; i < beacons.size(); i++) {
                        Beacon b = meusBeacons.get(i);

                        try {
                            PlaceBeaconProvider pbp = new PlaceBeaconProvider(getApplicationContext());
                            PlaceBeacon pb = pbp.getByUUID(b.getId1().toString());
                            Log.i(TAG, pb.toString());

                            PlaceProvider pp = new PlaceProvider(getApplicationContext());
                            Place currentPlace = pp.getByID(pb.getIdPlace());

                            Log.i(TAG, "Você está em " + currentPlace.getName());

                            texto = "Você está em " + currentPlace.getName();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtCurrentPlace.setText(texto);
                                }
                            });
                        } catch (SQLiteException e) {
                            Log.i(TAG, "Beacon com uuid " + b.getId1().toString() + " não cadastrado, ignorando...");
                        }
                    }
                }
            }
        });

//        try {
//            mBeaconManager.startRangingBeaconsInRegion(new Region(UNIQUE_RANGING_ID, null, null, null));
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    private void updateCurrentPlaceText(Place p) {
        txtCurrentPlace.setText("Você está em " + p.getName());
    }

    private void getDetailsFromBeacon(Beacon b) {
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
    }
}