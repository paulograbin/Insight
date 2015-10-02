package com.paulograbin.insight.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.paulograbin.insight.Activity.Lists.ListBeacons;
import com.paulograbin.insight.Activity.Lists.ListPaths;
import com.paulograbin.insight.Activity.Lists.ListPlaceBeacons;
import com.paulograbin.insight.Activity.Lists.ListPlacesAll;
import com.paulograbin.insight.DB.DatabaseHelper;
import com.paulograbin.insight.Output.Speaker;
import com.paulograbin.insight.Output.Vibrate;
import com.paulograbin.insight.R;


public class AdminPanelActivity extends ActionBarActivity {

    private Vibrate vibrate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        vibrate = new Vibrate(this);

        Button btnShowBeaconList = (Button) findViewById(R.id.btnShowBeacons);
        btnShowBeaconList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListBeacons.class);
                startActivity(intent);
            }
        });

        Button btnShowPlaceList = (Button) findViewById(R.id.btnPlaces);
        btnShowPlaceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListPlacesAll.class);
                startActivity(intent);
            }
        });

        Button btnShowPlaceBeaconList = (Button) findViewById(R.id.btnShowPlaceBeacons);
        btnShowPlaceBeaconList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListPlaceBeacons.class);
                startActivity(intent);
            }
        });

        Button btnVibrate = (Button) findViewById(R.id.btnVibrate);
        btnVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                vibrate.onVibrate();
//                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.music_marimba_chord);
//                mp.start();

//                Speaker.getInstance(getApplicationContext()).

                Speaker.getInstance(getApplicationContext()).playWithAlert("teste do paulo");
            }
        });

        Button btnPaths = (Button) findViewById(R.id.btnPaths);
        btnPaths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListPaths.class);
                startActivity(intent);
            }
        });

        Button btnDropTables = (Button) findViewById(R.id.btnDropTables);
        btnDropTables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper.getInstance(getApplicationContext()).dropTables();
            }
        });
    }
}
