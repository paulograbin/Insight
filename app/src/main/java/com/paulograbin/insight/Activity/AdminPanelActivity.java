package com.paulograbin.insight.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
    }
}
