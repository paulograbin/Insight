package com.paulograbin.insight;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.paulograbin.insight.Bluetooth.BleWrapper;
import com.paulograbin.insight.Bluetooth.BleWrapperUiCallbacks;

import java.util.logging.Handler;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    String LOG_TAG = "Spiga";

    Button btnBluetooth;
    Button btnShowBeaconList;
    Button btnShowMessageList;

    BleWrapper mBLEWapper;
    BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        btnShowMessageList = (Button) rootView.findViewById(R.id.btnShowMessages);
        btnShowMessageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callShowMessagesActivity();
            }
        });

        btnShowBeaconList = (Button) rootView.findViewById(R.id.btnShowBeacons);
        btnShowBeaconList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callShowBeaconsActivity();
            }
        });

        btnBluetooth = (Button) rootView.findViewById(R.id.btnBluetooth);
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBluetooth();
            }
        });


        mBLEWapper = new BleWrapper(getActivity(), new BleWrapperUiCallbacks.Null() {
        });

        if (!mBLEWapper.checkBleHardwareAvailable()) {
            Toast.makeText(getActivity(), "Hardware incompativel.", Toast.LENGTH_SHORT).show();

            this.getActivity().finish();
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // check for Bluetooth enabled on each resume
        if (!mBLEWapper.isBtEnabled()) {
            // Bluetooth is not enabled. Request to user to turn it on
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
        }

        // init BLE wrapper
        mBLEWapper.initialize();
    }

    public void callShowMessagesActivity() {
        Intent intent = new Intent(getActivity().getApplicationContext(), MessageList.class);
        startActivity(intent);
    }

    public void callShowBeaconsActivity() {
        Intent intent = new Intent(getActivity().getApplicationContext(), BeaconsList.class);
        startActivity(intent);
    }

    public void checkBluetooth() {
        checkSupportForRegularBluetooth();

        checkSupportForBLuetoothLowEnergy();

        enableBluetooth();
    }

    public Boolean checkSupportForRegularBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(LOG_TAG, "Dispositivo não suporta Bluetooth...");
//            Toast.makeText(this, "Dispositivo não possui Bluetooth...", Toast.LENGTH_LONG).show();
            return false;
        }

        Log.e(LOG_TAG, "Dispositivo tem suporte a Bluetooth...");
//        Toast.makeText(this, "Bluetooth suportado!11!11", Toast.LENGTH_LONG).show();
        return true;
    }

    public void enableBluetooth() {

        if(!mBluetoothAdapter.isEnabled()) {
            int REQUEST_ENABLE_BT = 0;

            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
            Log.i(LOG_TAG, "Usuário respondeu:" + REQUEST_ENABLE_BT);
        }
    }

    public Boolean checkSupportForBLuetoothLowEnergy() {
        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.

        Boolean bluetooth = getActivity().getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);

        if (!bluetooth) {
            Log.i(LOG_TAG, "Bluetooth Low Energy não é suportado");
            return false;
        }

        return true;
    }
}
