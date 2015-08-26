package com.paulograbin.insight.Activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.paulograbin.insight.Activity.Lists.ListBeacons;
import com.paulograbin.insight.Activity.Lists.ListMessages;
import com.paulograbin.insight.Activity.Lists.ListNearBeacons;
import com.paulograbin.insight.Activity.Lists.ListPaths;
import com.paulograbin.insight.Activity.Lists.ListPlaceBeacons;
import com.paulograbin.insight.Activity.Lists.ListPlaces;
import com.paulograbin.insight.DB.DatabaseHelper;
import com.paulograbin.insight.DB.Provider.BeaconProvider;
import com.paulograbin.insight.DB.Provider.MessageProvider;
import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.R;


public class AdminPanelActivity extends ActionBarActivity {

    //    TODO: No primeiro uso, pergunta se o usuário quer que o aplicativo monitore sua região e
    //    se auto execute quando chega na área

    String LOG_TAG = "Spiga";

    Button btnBluetooth;
    Button btnShowNearBeacons;
    Button btnShowBeaconList;
    Button btnShowMessageList;
    Button btnShowPlaceList;
    Button btnShowPlaceBeaconList;
    Button btnPaths;

    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        btnSpeak = (Button) findViewById(R.id.btnSpeak);
//        btnSpeak.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Speaker s = new Speaker(this.getBaseContext());
//                s.say("Teste do Paulo");
//            }
//        });

        btnShowBeaconList = (Button) findViewById(R.id.btnShowBeacons);
        btnShowBeaconList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListBeacons.class);
                startActivity(intent);
            }
        });

        btnShowMessageList = (Button) findViewById(R.id.btnShowMessages);
        btnShowMessageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListMessages.class);
                startActivity(intent);
            }
        });

        btnShowPlaceList = (Button) findViewById(R.id.btnPlaces);
        btnShowPlaceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListPlaces.class);
                startActivity(intent);
            }
        });

        btnShowPlaceBeaconList = (Button) findViewById(R.id.btnShowPlaceBeacons);
        btnShowPlaceBeaconList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListPlaceBeacons.class);
                startActivity(intent);
            }
        });

        btnShowNearBeacons = (Button) findViewById(R.id.btnShowNearBeacons);
        btnShowNearBeacons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListNearBeacons.class);
                startActivity(intent);
            }
        });

        btnBluetooth = (Button) findViewById(R.id.btnBluetooth);
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askUserToEnableBluetooth();
            }
        });

        btnPaths = (Button) findViewById(R.id.btnPaths);
        btnPaths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListPaths.class);
                startActivity(intent);
            }
        });

        checkDatabase();
        checkBluetoothIsSupported();
        checkIsBLEisSupported();
    }

    private void checkDatabase() {

        BeaconProvider bp = new BeaconProvider(this);
        MessageProvider mp = new MessageProvider(this);
        PlaceProvider pp = new PlaceProvider(this);
        PlaceBeaconProvider pbp = new PlaceBeaconProvider(this);

        if (bp.getCount() == 0 && mp.getCount() == 0 & pp.getCount() == 0 && pbp.getCount() == 0) {
            DatabaseHelper.insertStandardRecords();
        }
    }

    public Boolean checkBluetoothIsSupported() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null;

    }

    public void askUserToEnableBluetooth() {

        if (!mBluetoothAdapter.isEnabled()) {
            int REQUEST_ENABLE_BT = 0;

            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
            Log.i(LOG_TAG, "Usuário respondeu:" + REQUEST_ENABLE_BT);
        }
    }

    public Boolean checkIsBLEisSupported() {
        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);

    }
}
