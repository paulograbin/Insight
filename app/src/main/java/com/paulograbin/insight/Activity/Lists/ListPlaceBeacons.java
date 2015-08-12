package com.paulograbin.insight.Activity.Lists;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.paulograbin.insight.Activity.Details.PlaceBeaconDetails;
import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.Model.PlaceBeacon;
import com.paulograbin.insight.R;

import java.util.ArrayList;
import java.util.List;

public class ListPlaceBeacons extends AppCompatActivity {

    ArrayAdapter<String> mArrayAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_placebeacon);

        mArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());

        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), mArrayAdapter.getItem(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getBaseContext(), PlaceBeaconDetails.class)
                        .putExtra(Intent.EXTRA_TEXT, mArrayAdapter.getItem(position).substring(0, 2).trim());
                Log.i("Spiga", mArrayAdapter.getItem(position).substring(0, 2).trim());
                startActivity(intent);
            }
        });

        refreshList();
    }

    private void refreshList() {
        mArrayAdapter.clear();

        PlaceBeaconProvider pp = new PlaceBeaconProvider(getBaseContext());
        List<PlaceBeacon> placeBeacons = pp.getAll();

        for (int i = 0; i < placeBeacons.size(); i++) {
            mArrayAdapter.add(placeBeacons.get(i).toString());
        }

    }
}
