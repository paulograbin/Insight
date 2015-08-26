package com.paulograbin.insight.Activity.Lists;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.paulograbin.insight.Adapter.PlaceAdapter;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Place;

import java.util.ArrayList;
import java.util.List;

public class ListPlaces extends ActionBarActivity {

    ListView listView;
    List<Place> places;
    PlaceAdapter mAdapter;

    Place currentPlace;
    Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        if (intent.hasExtra("place")) {
            currentPlace = (Place) intent.getSerializableExtra("place");
        }

        currentLocation = new Location("teste");
        currentLocation.setLatitude(currentPlace.getLatitude());
        currentLocation.setLongitude(currentPlace.getLongitude());

        Log.i("Spiga", "CurrentPlace:" + currentPlace.getName() + ", " + currentPlace.getLatitude() + "/" + currentPlace.getLongitude());
        Log.i("Spiga", "CurrentLocation:" + currentLocation.getLatitude() + " - " + currentLocation.getLongitude());

        listView = new ListView(this);
        listView.setId(android.R.id.list);

        places = new ArrayList<>();
        mAdapter = new PlaceAdapter(this, places, currentLocation);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place chosenPlace = (Place) parent.getItemAtPosition(position);
                Log.i("Spiga", "Teste... " + chosenPlace.getName() + " - " + chosenPlace.toString());

                Intent intent = new Intent();
                intent.putExtra("chosenPlace", chosenPlace);

                setResult((int) chosenPlace.getId(), intent);
                finish();
            }
        });

        setContentView(listView);
        refreshList();
    }

    private void refreshList() {
        mAdapter.clear();

        PlaceProvider pp = new PlaceProvider(this);
        List<Place> places = pp.getAll();

        if(currentPlace != null)
            for (Place p: places) {
                if (!p.isEqualTo(currentPlace)) {
                    mAdapter.add(p);
                }
            }
    }
}
