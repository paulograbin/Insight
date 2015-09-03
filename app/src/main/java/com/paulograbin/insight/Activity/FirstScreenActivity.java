package com.paulograbin.insight.Activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.paulograbin.insight.Activity.Lists.ListPlacesForSelection;
import com.paulograbin.insight.Adapter.PlaceAdapter;
import com.paulograbin.insight.Adapter.PlaceSelectionAdapter;
import com.paulograbin.insight.Bluetooth.BluetoothService;
import com.paulograbin.insight.DB.Provider.BeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.LocationEngine.RouteFinder;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.Model.PlaceBeacon;
import com.paulograbin.insight.R;

import org.altbeacon.beacon.Beacon;

import java.util.LinkedList;
import java.util.Locale;

public class FirstScreenActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    public static final String TAG = "Spiga";

    private boolean debugActivityExecution = true;
    private TextToSpeech tts;
    private BluetoothService mBluetoothService;

    Beacon mLastSeenBeacon;
    Place mCurrentPlace;
    Place mTargetPlace;

    LinkedList<Place> path;
    PlaceSelectionAdapter mPlaceSelectionAdapter;
    PlaceAdapter mPlaceAdapter;

    PlaceProvider mPlaceProvider;
    PlaceBeaconProvider mPlaceBeaconProvider;

    ListView list;
    TextView txtCurrentPlace;
    Button btnAdminPanel;
    Button btnChooseDestiny;

    int selectedOption = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstscreen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        tts = new TextToSpeech(this, this);

        list = (ListView) findViewById(R.id.list);

        mPlaceProvider = new PlaceProvider(this);
        mPlaceBeaconProvider = new PlaceBeaconProvider(this);

        txtCurrentPlace = (TextView) findViewById(R.id.txtCurrentPlace);
        txtCurrentPlace.setText("Nenhum beacon foi detectado ainda... :(");

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
                if (mCurrentPlace == null) {
                    Toast.makeText(getBaseContext(), R.string.initial_place_not_identified, Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getBaseContext(), ListPlacesForSelection.class);
                intent.putExtra("place", mCurrentPlace);
                startActivityForResult(intent, selectedOption);
            }
        });


        try {
            Log.i("Spiga", "Chamando service");
            Intent intent = new Intent(this, BluetoothService.class);
            startService(intent);

            if(isMyServiceRunning(BluetoothService.class)) {
                Log.i("Spiga", "rodando");
            } else {
                Log.i("Spiga", "não ta rodando");
            }

            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(BluetoothService.FOUND_NEW_BEACON_EVENT));
        } catch (Exception e) {
            Log.i("Spiga", "Deu merda... " + e.getClass());
            e.printStackTrace();
        }
    }

    private void say(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
            Beacon receivedBeacon = (Beacon) intent.getParcelableExtra(BluetoothService.BEACON_KEY);
            printToLog(this.getClass().getName() + " recebeu um beacon, " + receivedBeacon.getId1().toString());

            onBeaconReceived(receivedBeacon);
        }
    };

    private void onBeaconReceived(Beacon lastSeenBeacon) {
        try {
            mLastSeenBeacon = lastSeenBeacon;

            PlaceBeacon pb = mPlaceBeaconProvider.getByUUID(mLastSeenBeacon.getId1().toString());
            mCurrentPlace = mPlaceProvider.getByID(pb.getIdPlace());

            BeaconProvider beaconProvider = new BeaconProvider(this);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = "Você está em " + mCurrentPlace.getName();

                    txtCurrentPlace.setText(message);
                    say(message + ", " + mCurrentPlace.getMessage());
                }
            });
        } catch (Exception e) {
            printToLog("Beacon com uuid " + lastSeenBeacon.getId1().toString() + " não cadastrado, ignorando...");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.i(TAG, "SelectedOption: " + selectedOption + "; requestCode: " + requestCode);

            Place placeSelectedByUser = (Place) data.getSerializableExtra("chosenPlace");
            Log.i(TAG, "Place selected by the user " + placeSelectedByUser);

            RouteFinder routeFinder = new RouteFinder(this, mCurrentPlace, placeSelectedByUser);
            path = routeFinder.getPathToTargetPlace();

            if(path != null) {
                Location currentLocation = new Location("mCurrentLocation");
                currentLocation.setLatitude(mCurrentPlace.getLatitude());
                currentLocation.setLongitude(mCurrentPlace.getLongitude());

                mPlaceSelectionAdapter = new PlaceSelectionAdapter(this, path, currentLocation);
                list.setAdapter(mPlaceSelectionAdapter);
            } else {
                say("Caminho não encontrado");
                mPlaceSelectionAdapter.clear();
            }
        }
    }

    private void updateCurrentPlaceText(Place p) {
        txtCurrentPlace.setText("Você está em " + p.getName());
    }

    private void printToLog(String message) {
        if (debugActivityExecution)
            Log.i(TAG, message);
    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.ERROR) {
            tts.setLanguage(Locale.getDefault());
        }
    }
}