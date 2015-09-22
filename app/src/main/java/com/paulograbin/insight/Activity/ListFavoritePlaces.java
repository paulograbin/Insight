package com.paulograbin.insight.Activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.paulograbin.insight.Adapter.PlaceSelectionAdapter;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.Output.Speaker;

import java.util.ArrayList;
import java.util.List;

public class ListFavoritePlaces extends AppCompatActivity {

    ListView mList;
    List<Place> mFavorites;
    PlaceSelectionAdapter mAdapter;

    Place mCurrentPlace;
    Location mCurrentLocation;
    private Speaker mSpeaker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpeaker = Speaker.getInstance(this);

        Intent intent = this.getIntent();
        if (intent.hasExtra("place")) {
            mCurrentPlace = (Place) intent.getParcelableExtra("place");
        }

        mCurrentLocation = new Location("teste");
        mCurrentLocation.setLatitude(mCurrentPlace.getLocation().getLatitude());
        mCurrentLocation.setLongitude(mCurrentPlace.getLocation().getLongitude());

        mList = new ListView(this);
        mList.setId(android.R.id.list);

        mFavorites = new ArrayList<>();
        mAdapter = new PlaceSelectionAdapter(this, mFavorites, mCurrentLocation);
        mList.setAdapter(mAdapter);

        setContentView(mList);
        refreshList();
    }

    private void refreshList() {
        mAdapter.clear();

        PlaceProvider pp = new PlaceProvider(this);
        mFavorites = pp.getAllFavoritePlaces();

        mAdapter.addAll(mFavorites);

        if(mFavorites.size() == 1)
            mSpeaker.playWithoutAlert("Um local favorito encontrado");
        else if(mFavorites.size() > 1) {
            mSpeaker.playWithoutAlert(mFavorites.size() + " locais favoritos encontrados");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSpeaker = null;
        finish();
    }
}
