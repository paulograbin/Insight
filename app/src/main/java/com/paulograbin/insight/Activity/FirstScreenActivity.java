package com.paulograbin.insight.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
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

public class FirstScreenActivity extends ServiceActivity {

    private Beacon mLastSeenBeacon;
    private Place mCurrentPlace;
    private Place mPreviousPlace;

    private Navigation mNavigation;
    private PlaceSelectionAdapter mPlaceSelectionAdapter;
    private PlaceProvider mPlaceProvider;
    private PlaceBeaconProvider mPlaceBeaconProvider;

    private ListView mPathPlacesList;
    private TextView txtCurrentPlace;
    private TextView txtPath;
    private Button btnChooseDestiny;
    private Button btnCallHelp;
    private Button btnFavorites;

    private final int pathRequest = 33;
    private final int bluetoothTurnedOn = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstscreen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mPathPlacesList = (ListView) findViewById(R.id.list);
        mPathPlacesList.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);

        txtCurrentPlace = (TextView) findViewById(R.id.txtCurrentPlace);
        txtCurrentPlace.setText("Ainda não foi possível determinar sua localização");

        txtPath = (TextView) findViewById(R.id.txtPath);
        txtPath.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);

        btnCallHelp = (Button) findViewById(R.id.btnCallHelp);
        btnCallHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                say("Ajuda solicitada, um segurança está a caminho.");
                DatabaseHelper.getInstance(getApplicationContext()).insertStandardRecords();
            }
        });

        btnFavorites = (Button) findViewById(R.id.btnFavorites);
        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPlace == null) {
                    sayImmediately(getString(R.string.initial_place_not_identified));
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
                DatabaseHelper.getInstance(getApplicationContext()).checkDatabase();

                if (mCurrentPlace == null) {
                    sayImmediately(getString(R.string.initial_place_not_identified));
                    return;
                }

                Intent intent = new Intent(getBaseContext(), DestinySelectionActivity.class);
                intent.putExtra("place", mCurrentPlace);
                startActivityForResult(intent, pathRequest);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AdminPanelActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(checkBluetoothIsSupported()) {
            if (checkIsBLEisSupported()) {
                if(!isBluetoothOn()) {
                    sayImmediately("Por favor, ative sua comunicação bluetooth");
                    askUserToEnableBluetooth();
                }
            } else {
                sayImmediately("Seu aplicativo não suport a versão que o aplicativo exige para funcionar.");
            }
        } else {
            sayImmediately("Seu dispositivo não tem suporte a tecnologia Bluetooth, por isso o aplicativo não pode funcionar.");
        }

        mPlaceProvider = new PlaceProvider(this);
        mPlaceBeaconProvider = new PlaceBeaconProvider(this);

//        if(mCurrentPlace != null && !isNavigationStarted())
//            sayPlaceName(txtCurrentPlace.getText().toString());
    }

    protected void onBeaconReceived(Beacon lastSeenBeacon) {
        try {
            mLastSeenBeacon = lastSeenBeacon;

            mPreviousPlace = mCurrentPlace;

            PlaceBeacon pb = mPlaceBeaconProvider.getByUUID(mLastSeenBeacon.getId1().toString());
            mCurrentPlace = mPlaceProvider.getByID(pb.getIdPlace());

            onUserHasArrivedInNewPlace();
        } catch (Exception e) {
            printToLog("Beacon não cadastrado " + lastSeenBeacon.getId1().toString());

            Toast t = Toast.makeText(this, "Beacon não cadastrado", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    private void onUserHasArrivedInNewPlace() {
        printToLog("onUserHasArrivedInNewPlace " + isNavigationStarted());

        updateScreenWithPlaceName();
        if(isNavigationStarted()) {
            // Destination selected

            if(!mCurrentPlace.isEqualTo(mNavigation.getTargetPlace())) {
                sayWithAlert("Você está em " + mCurrentPlace.getName());

                // Chegou ao proximo place
                if(mCurrentPlace.isEqualTo(mNavigation.checkNextPlace())) {

//                    removePlaceFromScreeList();
                    mNavigation.getNextPlace();
                    say(mCurrentPlace.getMessage());

                    getRouteToTargetPlace(mNavigation.getTargetPlace());

                    float bearing = mCurrentPlace.getLocation().bearingTo(mNavigation.checkNextPlace().getLocation());
                    printToLog(bearing + " is the bearing from " + mCurrentPlace.getName() + " to " + mNavigation.checkNextPlace().getName());
                }
            } else {
                // Chegou ao destino
                removePlaceFromScreeList();
                sayWithAlert("Você chegou em " + mCurrentPlace.getName());
            }
        } else { // Destination not selected yet
            sayPlaceName(txtCurrentPlace.getText().toString());
        }
    }

    private void removePlaceFromScreeList() {
        mPlaceSelectionAdapter.remove(mPlaceSelectionAdapter.getItem(0));
    }

    private void sayPlaceName(String message) {
        sayWithAlert(message);
    }

    private void updateScreenWithPlaceName() {
        txtCurrentPlace.setText("Você está em " + mCurrentPlace.getName());
    }

    private boolean isNavigationStarted() {
        boolean navigating = false;

        if(mNavigation != null)
            navigating = true;

        return navigating;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i("result", "RequestCode:" + requestCode + " - ResultCode " + resultCode);

        if (resultCode == RESULT_OK) {
            if (requestCode == pathRequest) {
                Place destinationPlace = intent.getParcelableExtra("chosenPlace");
                onUserSelectedADestinationPlace(destinationPlace);
            } else if(requestCode == 1)
            {
                Log.i("result", "bluetooth request accepter");
            }
        } else if (resultCode == RESULT_CANCELED){
            if(requestCode != pathRequest) {
                Log.i("result", "usuário cancelou request bluetooth - requestCode " + requestCode);
                say("O aplicativo necessita que a comunicação Bluetooth de seu telefone esteja ativa.");
            }
        }
    }

    private void onUserSelectedADestinationPlace(Place destinationPlaceSelectedByUser) {
        try {
            getRouteToTargetPlace(destinationPlaceSelectedByUser);

            txtPath.setText("Caminho a ser percorrido:");
            txtPath.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);

            if(mPlaceSelectionAdapter.getCount() == 2) {
                say("Passaremos por " + (mPlaceSelectionAdapter.getCount() - 1) + " local até o destino");
            } else {
                say("Passaremos por " + (mPlaceSelectionAdapter.getCount() - 1) + " locais até o destino");
            }

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

    private void getRouteToTargetPlace(Place destinationPlaceSelectedByUser) {
        RouteFinder routeFinder = new RouteFinder(this, mCurrentPlace, destinationPlaceSelectedByUser);

        mNavigation = new Navigation(routeFinder.getPath(), mCurrentPlace, destinationPlaceSelectedByUser);

        mPlaceSelectionAdapter = new PlaceSelectionAdapter(this, routeFinder.getPath(), mCurrentPlace.getLocation());
        mPathPlacesList.setAdapter(mPlaceSelectionAdapter);
        mPathPlacesList.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
    }

    private void printToLog(String message) {
        String TAG = "Spiga";
        boolean debugActivityExecution = false;

        if (debugActivityExecution)
            Log.i(TAG, message);
    }
}