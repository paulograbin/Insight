package com.paulograbin.insight.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.paulograbin.insight.Activity.Lists.ListPlaces;
import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
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

    public static final String TAG = "Spiga";
    private static final String UNIQUE_RANGING_ID = "FarolBeaconUniqueId";
    PlaceProvider pp = new PlaceProvider(this);

    Place currentPlace;
    TextView txtCurrentPlace;
    Button btnAdminPanel;
    Button btnChooseDestiny;

    BeaconManager mBeaconManager;

    int selectedOption = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        pp = new PlaceProvider(getApplicationContext());

//        currentPlace = pp.getByID(1L);

        btnAdminPanel = (Button) findViewById(R.id.btnAdminPanel);
        btnAdminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminPanelActivity.class);
                startActivity(intent);
            }
        });
        btnChooseDestiny = (Button) findViewById(R.id.btnChooseDestination);
        btnChooseDestiny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlace == null) {
                    Toast.makeText(getBaseContext(), "Não foi possível determinar o local inicial", Toast.LENGTH_SHORT).show();
                    return;
                }


                Intent intent = new Intent(getBaseContext(), ListPlaces.class);
                intent.putExtra("place", currentPlace);
                startActivityForResult(intent, selectedOption);
            }
        });

        txtCurrentPlace = (TextView) findViewById(R.id.txtCurrentPlace);
        txtCurrentPlace.setText("Nenhum beacon foi detectado ainda... :(");

        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        mBeaconManager.bind(this);
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     * <p>This method is never invoked if your activity sets
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "SelectedOption: " + selectedOption + "; requestCode: " + requestCode);
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

                            currentPlace = pp.getByID(pb.getIdPlace());

                            Log.i(TAG, "Você está em " + currentPlace.getName());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtCurrentPlace.setText("Você está em " + currentPlace.getName());
                                }
                            });
                        } catch (SQLiteException e) {
                            Log.i(TAG, "Beacon com uuid " + b.getId1().toString() + " não cadastrado, ignorando...");
                        }
                    }
                }
            }
        });

        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region(UNIQUE_RANGING_ID, null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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