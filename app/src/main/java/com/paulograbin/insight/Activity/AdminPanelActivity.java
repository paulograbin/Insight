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
import com.paulograbin.insight.Activity.Lists.ListPaths;
import com.paulograbin.insight.Activity.Lists.ListPlaceBeacons;
import com.paulograbin.insight.Activity.Lists.ListPlacesAll;
import com.paulograbin.insight.DB.DatabaseHelper;
import com.paulograbin.insight.R;


public class AdminPanelActivity extends ActionBarActivity {

    //    TODO: No primeiro uso, pergunta se o usuário quer que o aplicativo monitore sua região e
    //    se auto execute quando chega na área

    private String LOG_TAG = "Spiga";

    private Button btnBluetooth;
    private Button btnShowBeaconList;
    private Button btnShowMessageList;
    private Button btnShowPlaceList;
    private Button btnShowPlaceBeaconList;
    private Button btnPaths;
    private Button btnDropTables;

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
                Intent intent = new Intent(getApplicationContext(), ListPlacesAll.class);
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

        btnDropTables = (Button) findViewById(R.id.btnDropTables);
        btnDropTables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper.getInstance(getApplicationContext()).dropTables();
            }
        });

        checkBluetoothIsSupported();
        checkIsBLEisSupported();
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
