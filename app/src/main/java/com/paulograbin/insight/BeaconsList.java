package com.paulograbin.insight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.paulograbin.insight.DB.DatabaseHelper;
import com.paulograbin.insight.Model.Beacon;

import java.util.ArrayList;
import java.util.List;


public class BeaconsList extends ActionBarActivity {

    ArrayAdapter<String> mArrayAdapter;

    ListView beaconsList;
    Button btnAdd;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacons_list);

        mArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());

        beaconsList = (ListView) findViewById(R.id.beaconsList);
        beaconsList.setAdapter(mArrayAdapter);
        beaconsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), mArrayAdapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBeacon();

                refreshList();
            }
        });

        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllBeacons();

                refreshList();
            }
        });

        refreshList();
    }

    public void addBeacon() {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.addDummyBeacon();
    }

    public void deleteAllBeacons() {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.dropTableBeacon();
    }

    public void refreshList() {
        mArrayAdapter.clear();
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        List<Beacon> beacons = db.getAllBeacons();

        for(int i = 0; i < beacons.size(); i++) {
            mArrayAdapter.add(beacons.get(i).toString());
        }
    }
}
