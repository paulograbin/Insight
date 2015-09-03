package com.paulograbin.insight.Activity.Lists;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.paulograbin.insight.DB.Provider.BeaconProvider;
import com.paulograbin.insight.Model.Beacon;

import java.util.ArrayList;
import java.util.List;

public class ListBeacons extends ActionBarActivity {

    ListView mListView;
    List<Beacon> beacons;
    ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListView = new ListView(this);
        beacons = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());

        mListView.setAdapter(mAdapter);

        setContentView(mListView);
        refreshList();
    }

    public void refreshList() {
        mAdapter.clear();

        BeaconProvider bp = new BeaconProvider(this);
        beacons = bp.getAll();

        for (int i = 0; i < beacons.size(); i++) {
            mAdapter.add(beacons.get(i).toString());
        }
    }
}
