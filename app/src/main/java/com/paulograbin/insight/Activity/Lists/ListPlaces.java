package com.paulograbin.insight.Activity.Lists;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Place;

import java.util.ArrayList;
import java.util.List;

public class ListPlaces extends ActionBarActivity {

    ListView listView;
    ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = new ListView(this);
        mArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());

        listView.setAdapter(mArrayAdapter);

        setContentView(listView);
        refreshList();
    }

    private void refreshList() {
        mArrayAdapter.clear();

        PlaceProvider pp = new PlaceProvider(getBaseContext());
        List<Place> places = pp.getAll();

        for (int i = 0; i < places.size(); i++) {
            mArrayAdapter.add(places.get(i).toString());
        }
    }
}
