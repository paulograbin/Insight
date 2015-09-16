package com.paulograbin.insight.Activity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.paulograbin.insight.Adapter.PlaceSelectionAdapter;
import com.paulograbin.insight.DB.DatabaseHelper;
import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Exceptions.NoWayException;
import com.paulograbin.insight.Exceptions.RecordNotFoundException;
import com.paulograbin.insight.LocationEngine.Navigation;
import com.paulograbin.insight.LocationEngine.RouteFinder;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.Model.PlaceBeacon;
import com.paulograbin.insight.R;

import org.altbeacon.beacon.Beacon;

public class FirstScreenActivity extends ServiceActivity implements SensorEventListener {

    //TODO: usar o objeto navigation
    //TODO: direção do sensor

    //TODO: navigation, initicializa já no primeiro lugar do camihno

    private static final String TAG = "Spiga";
    private SensorManager mSensorManager;
    private Sensor mCompass;
    private boolean debugActivityExecution = true;

    private Beacon mLastSeenBeacon;
    private Place mCurrentPlace;

    private Navigation mNavigation;
    private PlaceSelectionAdapter mPlaceSelectionAdapter;

    private PlaceProvider mPlaceProvider;

    private PlaceBeaconProvider mPlaceBeaconProvider;

    private ListView mPathPlacesList;
    private TextView txtCurrentPlace;
    private Button btnAdminPanel;
    private Button btnChooseDestiny;
    private Button btnCallHelp;

    private Button btnFavorites;
    private int pathRequest = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstscreen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mCompass = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        mPathPlacesList = (ListView) findViewById(R.id.list);

        txtCurrentPlace = (TextView) findViewById(R.id.txtCurrentPlace);
        txtCurrentPlace.setText("Nenhum beacon foi detectado ainda... :(");

        btnCallHelp = (Button) findViewById(R.id.btnCallHelp);
        btnCallHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                say("Ajuda solicitada, um segurança está a caminho.");
            }
        });

        btnAdminPanel = (Button) findViewById(R.id.btnAdminPanel);
        btnAdminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminPanelActivity.class);
                startActivity(intent);
            }
        });

        btnFavorites = (Button) findViewById(R.id.btnFavorites);
        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPlace == null) {
                    Toast.makeText(getBaseContext(), R.string.initial_place_not_identified, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mPlaceProvider.getAllFavoritePlaces().size() == 0) {
                    say("Você não possui nenhum local marcado como favorito");
                    return;
                }

                Intent intent = new Intent(getBaseContext(), ListFavoritePlaces.class);
                intent.putExtra("place", mCurrentPlace);
                startActivityForResult(intent, pathRequest);
            }
        });

        btnChooseDestiny = (Button) findViewById(R.id.btnChooseDestination);
        btnChooseDestiny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPlace == null) {
                    Toast.makeText(getBaseContext(), R.string.initial_place_not_identified, Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getBaseContext(), DestinySelectionActivity.class);
                intent.putExtra("place", mCurrentPlace);
                startActivityForResult(intent, pathRequest);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(checkBluetoothIsSupported()) {
            if (checkIsBLEisSupported()) {
                askUserToEnableBluetooth();
            } else {

            }
        } else {

        }

        mPlaceProvider = new PlaceProvider(this);
        mPlaceBeaconProvider = new PlaceBeaconProvider(this);

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);

        try {
            mCurrentPlace = mPlaceProvider.getByName("Ponto Inicial"); //TODO: remove
        } catch (RecordNotFoundException e) {
            Toast.makeText(this, "Não pegou o place inicial fake", Toast.LENGTH_SHORT).show();
        }

        DatabaseHelper.getInstance(this).checkDatabase();
    }

    protected void onBeaconReceived(Beacon lastSeenBeacon) {
        try {
            mLastSeenBeacon = lastSeenBeacon;

            PlaceBeacon pb = mPlaceBeaconProvider.getByUUID(mLastSeenBeacon.getId1().toString());
            mCurrentPlace = mPlaceProvider.getByID(pb.getIdPlace());

            if (!mCurrentPlace.isEqualTo(mNavigation.getTargetPlace())) {
                // Not final place
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message = "Você está em " + mCurrentPlace.getName();

                        txtCurrentPlace.setText(message);
                        say(message + ", " + mCurrentPlace.getMessage());
                    }
                });
            } else {
                // Final place
                say("Você chegou ao seu destino.");

            }

        } catch (Exception e) {
            printToLog("Beacon com uuid " + lastSeenBeacon.getId1().toString() + " não cadastrado, ignorando...");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            if (requestCode == pathRequest) {
                Place destinationPlace = (Place) intent.getParcelableExtra("chosenPlace");
                onUserSelectedADestinationPlace(destinationPlace);
            }
        }
    }

    private void onUserSelectedADestinationPlace(Place destinationPlaceSelectedByUser) {
        try {
            RouteFinder routeFinder = new RouteFinder(this, mCurrentPlace, destinationPlaceSelectedByUser);

            mNavigation = new Navigation(routeFinder.getPath(), mCurrentPlace, destinationPlaceSelectedByUser);

            say("Calculando rota até " + destinationPlaceSelectedByUser.getName());

            mPlaceSelectionAdapter = new PlaceSelectionAdapter(this, routeFinder.getPath(), mCurrentPlace.getLocation());
            mPathPlacesList.setAdapter(mPlaceSelectionAdapter);

            say("Rota para " + destinationPlaceSelectedByUser.getName() + " encontrada. Vamos passar por " + (mPlaceSelectionAdapter.getCount() - 1) + " locais para chegar ao destino");
        } catch (NoWayException e) {
            say("Não há um caminho cadastrado para o local selecionado");
            if (mPlaceSelectionAdapter != null) {
                mPlaceSelectionAdapter.clear();
            }
        }
    }

    private void printToLog(String message) {
        if (debugActivityExecution)
            Log.i(TAG, message);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        float azimuth = Math.round(event.values[0]);
        // The other values provided are:
        //  float pitch = event.values[1];
        //  float roll = event.values[2];
//        Log.i("sensor", "azimuth: " + Float.toString(azimuth));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}