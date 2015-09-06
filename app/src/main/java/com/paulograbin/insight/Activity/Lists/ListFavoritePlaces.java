package com.paulograbin.insight.Activity.Lists;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.paulograbin.insight.Adapter.PlaceSelectionAdapter;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListFavoritePlaces extends AppCompatActivity implements TextToSpeech.OnInitListener {

    ListView mList;
    List<Place> mFavorites;
    PlaceSelectionAdapter mAdapter;

    Place mCurrentPlace;
    Location mCurrentLocation;
    private TextToSpeech tts;

    @Override
    protected void onResume() {
        super.onResume();

        tts = new TextToSpeech(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        if (intent.hasExtra("place")) {
            mCurrentPlace = (Place) intent.getSerializableExtra("place");
        }

        mCurrentLocation = new Location("teste");
        mCurrentLocation.setLatitude(mCurrentPlace.getLatitude());
        mCurrentLocation.setLongitude(mCurrentPlace.getLongitude());

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

//        say(mFavorites.size() + " locais favoritos encontrados");

        mAdapter.addAll(mFavorites);
    }

    @Override
    protected void onPause() {
        super.onPause();

        tts.stop();
        tts.shutdown();
        tts = null;
    }

    private void say(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(this, "TTS bombando", Toast.LENGTH_SHORT).show();
            tts.setLanguage(Locale.getDefault());
        } else {
            Toast.makeText(this, "pau no TTS", Toast.LENGTH_SHORT).show();
        }
    }
}
