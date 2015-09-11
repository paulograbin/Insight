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
import android.widget.Toast;

import com.paulograbin.insight.Activity.Details.DetailsPlace;
import com.paulograbin.insight.Adapter.PlaceSelectionAdapter;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DestinySelectionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;

    ListView mList;
    List<Place> mPlaces;
    PlaceSelectionAdapter mAdapter;

    Place mCurrentPlace;
    Location mCurrentLocation;


    @Override
    protected void onPause() {
        super.onPause();

        tts.stop();
        tts.shutdown();
        tts = null;
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

        Log.i("Spiga", "CurrentPlace:" + mCurrentPlace.getName() + ", " + mCurrentPlace.getLatitude() + "/" + mCurrentPlace.getLongitude());
        Log.i("Spiga", "CurrentLocation:" + mCurrentLocation.getLatitude() + " - " + mCurrentLocation.getLongitude());

        mList = new ListView(this);
        mList.setId(android.R.id.list);

        mPlaces = new ArrayList<>();
        mAdapter = new PlaceSelectionAdapter(this, mPlaces, mCurrentLocation);
        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

        setContentView(mList);
        refreshList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        tts = new TextToSpeech(this, this);
    }

    private void refreshList() {
        mAdapter.clear();

        PlaceProvider pp = new PlaceProvider(this);
        List<Place> places = pp.getAll(); // TODO: selecionar apenas destinos

        if(mCurrentPlace != null)
            for (Place p: places) {
                if (!p.isEqualTo(mCurrentPlace)) {
                    mAdapter.add(p);
                }
            }
    }

    private void say(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        //TODO: todo android vem com o sintetizador de voz
        //TODO: inspiração veio quando minha namorada comentou que viu no trem...

        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.getDefault());

            if(mPlaces.size() == 1)
                say("Um possível destino encontrado.");
            else if(mPlaces.size() > 1) {
                say(mPlaces.size() + " possíveis destinos encontrados");
            }
        } else {
            Toast.makeText(this, "pau no TTS", Toast.LENGTH_SHORT).show();
        }
    }
}
