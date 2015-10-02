package com.paulograbin.insight.Activity.Lists;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.paulograbin.insight.Adapter.PlaceAdapter;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Place;

import java.util.ArrayList;
import java.util.List;

public class ListPlacesAll extends ActionBarActivity {

    private ListView mListView;
    private List<Place> places;
    private PlaceAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListView = new ListView(this);
        mListView.setId(android.R.id.list);

        places = new ArrayList<>();
        mAdapter = new PlaceAdapter(this, places);
        mListView.setAdapter(mAdapter);

        setContentView(mListView);
        refreshList();
    }

    private void refreshList() {
        mAdapter.clear();

        PlaceProvider pp = new PlaceProvider(this);
        List<Place> places = pp.getAll();

        mAdapter.addAll(places);
    }
}
