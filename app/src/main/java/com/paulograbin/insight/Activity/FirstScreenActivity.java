package com.paulograbin.insight.Activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.paulograbin.insight.Activity.Lists.ListFavoritePlaces;
import com.paulograbin.insight.Adapter.PlaceAdapter;
import com.paulograbin.insight.Adapter.PlaceSelectionAdapter;
import com.paulograbin.insight.DB.DatabaseHelper;
import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Exceptions.NoWayException;
import com.paulograbin.insight.LocationEngine.RouteFinder;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.Model.PlaceBeacon;
import com.paulograbin.insight.R;

import org.altbeacon.beacon.Beacon;

import java.util.LinkedList;

public class FirstScreenActivity extends ServiceActivity {

    private static final String TAG = "Spiga";

    private boolean debugActivityExecution = true;

    private Beacon mLastSeenBeacon;
    private Place mCurrentPlace;
    private Place mTargetPlace;

    private LinkedList<Place> path;
    private PlaceSelectionAdapter mPlaceSelectionAdapter;
    PlaceAdapter mPlaceAdapter;

    private PlaceProvider mPlaceProvider;
    private PlaceBeaconProvider mPlaceBeaconProvider;

    private ListView list;
    private TextView txtCurrentPlace;
    private Button btnAdminPanel;
    private Button btnChooseDestiny;
    private Button btnCallHelp;
    private Button btnFavorites;

    int selectedOption = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstscreen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        list = (ListView) findViewById(R.id.list);

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
                startActivityForResult(intent, selectedOption);
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
                startActivityForResult(intent, selectedOption);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPlaceProvider = new PlaceProvider(this);
        mPlaceBeaconProvider = new PlaceBeaconProvider(this);

        mCurrentPlace = mPlaceProvider.getByName("Ponto Inicial"); //TODO: remove

        DatabaseHelper.getInstance(this).checkDatabase();
    }

    protected void onBeaconReceived(Beacon lastSeenBeacon) {
        try {
            mLastSeenBeacon = lastSeenBeacon;

            PlaceBeacon pb = mPlaceBeaconProvider.getByUUID(mLastSeenBeacon.getId1().toString());
            mCurrentPlace = mPlaceProvider.getByID(pb.getIdPlace());

            if(!mCurrentPlace.isEqualTo(mTargetPlace)) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.i(TAG, "SelectedOption: " + selectedOption + "; requestCode: " + requestCode);

            Place placeSelectedByUser = (Place) data.getSerializableExtra("chosenPlace");
            Log.i(TAG, "Place selected by the user " + placeSelectedByUser);
            mTargetPlace = placeSelectedByUser;

            try {
                RouteFinder routeFinder = new RouteFinder(this, mCurrentPlace, placeSelectedByUser);
                path = routeFinder.getPathToTargetPlace();

                say("Calculando rota até " + placeSelectedByUser.getName());

                Location currentLocation = new Location("mCurrentLocation");
                currentLocation.setLatitude(mCurrentPlace.getLatitude());
                currentLocation.setLongitude(mCurrentPlace.getLongitude());

                mPlaceSelectionAdapter = new PlaceSelectionAdapter(this, path, currentLocation);
                list.setAdapter(mPlaceSelectionAdapter);

                say("Rota para " + mTargetPlace.getName() + " encontrada. Vamos passar por " + (mPlaceSelectionAdapter.getCount()-1) + " locais para chegar ao destino");
            } catch (NoWayException e) {
                say("Não há um caminho cadastrado para o local selecionado");
//                mPlaceSelectionAdapter.clear();
            }
        }
    }

    private void printToLog(String message) {
        if (debugActivityExecution)
            Log.i(TAG, message);
    }
}