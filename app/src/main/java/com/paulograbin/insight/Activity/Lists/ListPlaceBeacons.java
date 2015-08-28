package com.paulograbin.insight.Activity.Lists;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.paulograbin.insight.Activity.Details.PlaceBeaconDetails;
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), mAdapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), PlaceBeaconDetails.class)
                        .putExtra("placeBeaconId", mAdapter.getItem(position).getId());
                startActivity(intent);

                return true;
            }
        });

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
