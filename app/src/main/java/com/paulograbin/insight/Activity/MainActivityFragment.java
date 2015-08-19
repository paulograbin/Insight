package com.paulograbin.insight.Activity;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

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

    TextView txtBluetooth;
    TextView txtBLE;

    BluetoothAdapter mBluetoothAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        txtBluetooth = (TextView) rootView.findViewById(R.id.txtBluetooth);
        txtBLE = (TextView) rootView.findViewById(R.id.txtBLE);

//        btnSpeak = (Button) rootView.findViewById(R.id.btnSpeak);
//        btnSpeak.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Speaker s = new Speaker(getActivity().getBaseContext());
//                s.say("Teste do Paulo");
//            }
//        });

        btnShowBeaconList = (Button) rootView.findViewById(R.id.btnShowBeacons);
        btnShowBeaconList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ListBeacons.class);
                startActivity(intent);
            }
        });

        btnShowMessageList = (Button) rootView.findViewById(R.id.btnShowMessages);
        btnShowMessageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ListMessages.class);
                startActivity(intent);
            }
        });

        btnShowPlaceList = (Button) rootView.findViewById(R.id.btnPlaces);
        btnShowPlaceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ListPlaces.class);
                startActivity(intent);
            }
        });

        btnShowPlaceBeaconList = (Button) rootView.findViewById(R.id.btnShowPlaceBeacons);
        btnShowPlaceBeaconList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ListPlaceBeacons.class);
                startActivity(intent);
            }
        });

        btnShowNearBeacons = (Button) rootView.findViewById(R.id.btnShowNearBeacons);
        btnShowNearBeacons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ListNearBeacons.class);
                startActivity(intent);
            }
        });

        btnBluetooth = (Button) rootView.findViewById(R.id.btnBluetooth);
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askUserToEnableBluetooth();
            }
        });

        btnPaths = (Button) rootView.findViewById(R.id.btnPaths);
        btnPaths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ListPaths.class);
                startActivity(intent);
            }
        });

        checkDatabase();
        checkBluetoothIsSupported();
        checkIsBLEisSupported();

        return rootView;
    }

    private void checkDatabase() {

        Context context = getActivity().getApplicationContext();

        BeaconProvider bp = new BeaconProvider(context);
        MessageProvider mp = new MessageProvider(context);
        PlaceProvider pp = new PlaceProvider(context);
        PlaceBeaconProvider pbp = new PlaceBeaconProvider(context);

        if (bp.getCount() == 0 && mp.getCount() == 0 & pp.getCount() == 0 && pbp.getCount() == 0) {
            DatabaseHelper.insertStandardRecords();
        }
    }

    public Boolean checkBluetoothIsSupported() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            txtBluetooth.setText("Bluetooth: NOT supported.");
            return false;
        }

        txtBluetooth.setText("Blueooth: supported.");
        return true;
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

        if (!getActivity().getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            txtBLE.setText("BLE: NOT supported");
            return false;
        }

        txtBLE.setText("BLE: Supported");
        return true;
    }
}