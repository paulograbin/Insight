package com.paulograbin.insight.Activity.Lists;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.paulograbin.insight.Activity.Details.BeaconDetails;
import com.paulograbin.insight.DB.Provider.BeaconProvider;
import com.paulograbin.insight.Model.Beacon;

import java.util.ArrayList;
import java.util.List;


public class ListBeacons extends ActionBarActivity {

    ListView listView;
    ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());

        listView = new ListView(this);
        listView.setAdapter(mArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), mArrayAdapter.getItem(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getBaseContext(), BeaconDetails.class)
                        .putExtra(Intent.EXTRA_TEXT, mArrayAdapter.getItem(position));
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        setContentView(listView);
        refreshList();
    }

    public void refreshList() {
        mArrayAdapter.clear();

        BeaconProvider bp = new BeaconProvider(this);

        List<Beacon> beacons = bp.getAll();

        for (int i = 0; i < beacons.size(); i++) {
            mArrayAdapter.add(beacons.get(i).toString());
        }
    }
}
