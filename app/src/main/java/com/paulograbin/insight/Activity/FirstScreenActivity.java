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
import com.paulograbin.insight.LocationEngine.Navigation;
import com.paulograbin.insight.LocationEngine.RouteFinder;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.Model.PlaceBeacon;
import com.paulograbin.insight.R;

import org.altbeacon.beacon.Beacon;

public class FirstScreenActivity extends ServiceActivity implements SensorEventListener {

    //bug nas distancias
    //prioridade das falas
    //beep

    //TODO: direção do sensor
    private SensorManager mSensorManager;
    private Sensor mCompass;

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


//        try {
//            mCurrentPlace = mPlaceProvider.getByName("Ponto Inicial"); //TODO: remove
//        } catch (RecordNotFoundException e) {
//            Toast.makeText(this, "Não pegou o place inicial fake", Toast.LENGTH_SHORT).show();
//        }

        DatabaseHelper.getInstance(this).checkDatabase();
    }

    protected void onBeaconReceived(Beacon lastSeenBeacon) {
        try {
            mLastSeenBeacon = lastSeenBeacon;

            PlaceBeacon pb = mPlaceBeaconProvider.getByUUID(mLastSeenBeacon.getId1().toString().toUpperCase());
            mCurrentPlace = mPlaceProvider.getByID(pb.getIdPlace());

            handleUserMovement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // todo: pegar direção até proximo place e apontar/vibrar pra ele
    private void handleUserMovement() {
        printToLog("handleUserMovement " + userHasStartedNavigating());

        updateScreenWithPlaceName();
        if(userHasStartedNavigating()) {
            // Destination selected

            if(!mCurrentPlace.isEqualTo(mNavigation.getTargetPlace())) {
                sayWithAlert("Você está em " + mCurrentPlace.getName()); // + ". Agora " + mCurrentPlace.getMessage());

                // Chegou ao proximo place
                if(mCurrentPlace.isEqualTo(mNavigation.checkNextPlace())) {
                    mNavigation.getNextPlace();
                    say(mCurrentPlace.getMessage());

                    float bearing = mCurrentPlace.getLocation().bearingTo(mNavigation.checkNextPlace().getLocation());
                    printToLog(bearing + " is the bearing from " + mCurrentPlace.getName() + " to " + mNavigation.checkNextPlace().getName());
                }
            } else {
                // Chegou ao destino
                sayWithAlert("Você chegou em " + mCurrentPlace.getName());
            }
        } else { // Destination not selected yet
            sayPlaceName(txtCurrentPlace.getText().toString());
        }
    }

    private void sayPlaceName(String message) {
        sayWithAlert(message);
    }

    private void updateScreenWithPlaceName() {
        txtCurrentPlace.setText("Você está em " + mCurrentPlace.getName());
    }

    private boolean userHasStartedNavigating() {
        boolean navigating = false;

        if(mNavigation != null)
            navigating = true;

        return navigating;
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

            mPlaceSelectionAdapter = new PlaceSelectionAdapter(this, routeFinder.getPath(), mCurrentPlace.getLocation());
            mPathPlacesList.setAdapter(mPlaceSelectionAdapter);

            say("Vamos passar por " + (mPlaceSelectionAdapter.getCount() - 1) + " locais para chegar ao destino");

            float bearing = mCurrentPlace.getLocation().bearingTo(mNavigation.checkNextPlace().getLocation());
            printToLog(bearing + " is the bearing from current place to " + mNavigation.checkNextPlace().getName());


            say(mNavigation.getCurrentPlace().getMessage());
        } catch (NoWayException e) {
            say("Não há um caminho cadastrado para o local selecionado");
            if (mPlaceSelectionAdapter != null) {
                mPlaceSelectionAdapter.clear();
            }
        }
    }

    private void printToLog(String message) {
        String TAG = "Spiga";
        boolean debugActivityExecution = true;

        if (debugActivityExecution)
            Log.i(TAG, message);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float azimuth = Math.round(event.values[0]);
        // The other values provided are:
        //  float pitch = event.values[1];
        //  float roll = event.values[2];
//        Log.i("Spiga", "azimuth: " + Float.toString(azimuth));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}