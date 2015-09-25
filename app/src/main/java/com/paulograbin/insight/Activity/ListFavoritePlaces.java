package com.paulograbin.insight.Activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.paulograbin.insight.Adapter.PlaceSelectionAdapter;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.Output.Speaker;

import java.util.ArrayList;
import java.util.List;

public class ListFavoritePlaces extends AppCompatActivity {

    private ListView mList;
    private List<Place> mFavorites;
    private PlaceSelectionAdapter mAdapter;

    private Place mCurrentPlace;
    private Location mCurrentLocation;
    private Speaker mSpeaker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpeaker = Speaker.getInstance(this);

        Intent intent = this.getIntent();
        if (intent.hasExtra("place")) {
            mCurrentPlace = intent.getParcelableExtra("place");
        }

        mCurrentLocation = new Location("teste");
        mCurrentLocation.setLatitude(mCurrentPlace.getLocation().getLatitude());
        mCurrentLocation.setLongitude(mCurrentPlace.getLocation().getLongitude());

        mList = new ListView(this);
        mList.setId(android.R.id.list);

        mFavorites = new ArrayList<>();
        mAdapter = new PlaceSelectionAdapter(this, mFavorites, mCurrentLocation);
        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place chosenPlace = (Place) parent.getItemAtPosition(position);
                Log.i("Spiga", "Usuário escolheu destino: " + chosenPlace.toString());

                Intent intent = new Intent();
                intent.putExtra("chosenPlace", chosenPlace);

                setResult(RESULT_OK, intent);
                finish();
            }
        });

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
