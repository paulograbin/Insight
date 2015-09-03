package com.paulograbin.insight.Activity.Lists;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.paulograbin.insight.Adapter.PlaceBeaconAdapter;
import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.Model.PlaceBeacon;

import java.util.ArrayList;
import java.util.List;

public class ListPlaceBeacons extends ListActivity {

    ListView mListView;
    List<PlaceBeacon> placeBeacons;
    PlaceBeaconAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListView = new ListView(this);
        mListView.setId(android.R.id.list);

        placeBeacons = new ArrayList<>();
        mAdapter = new PlaceBeaconAdapter(this, placeBeacons);
        mListView.setAdapter(mAdapter);

        setContentView(mListView);
        refreshList();
    }

    private void refreshList() {
        mAdapter.clear();

        PlaceBeaconProvider pp = new PlaceBeaconProvider(this);
        placeBeacons = pp.getAll();

        mAdapter.addAll(placeBeacons);
    }
}
