package com.paulograbin.insight.Activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.paulograbin.insight.Activity.Details.DetailsPlace;
import com.paulograbin.insight.Adapter.PlaceSelectionAdapter;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DestinySelectionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;

    ListView listView;
    List<Place> places;
    PlaceSelectionAdapter mAdapter;

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
        mAdapter = new PlaceSelectionAdapter(this, places, currentLocation);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place chosenPlace = (Place) parent.getItemAtPosition(position);
                say("teste");
                Log.i("Spiga", "Usuário escolheu destino: " + chosenPlace.toString());

                Intent intent = new Intent();
                intent.putExtra("chosenPlace", chosenPlace);

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Place p = mAdapter.getItem(position);
                Log.i("Spiga", "Clicado na posição " + position + " id " + id);

                Intent intent = new Intent(getBaseContext(), DetailsPlace.class);
                intent.putExtra("place", p);
                startActivity(intent);
                return true;
            }
        });

        setContentView(listView);
        refreshList();

        tts = new TextToSpeech(this, this);


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

    private void say(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.ERROR) {
            tts.setLanguage(Locale.getDefault());
        } else {
            Log.i("Spiga", status + "");
        }
    }
}
