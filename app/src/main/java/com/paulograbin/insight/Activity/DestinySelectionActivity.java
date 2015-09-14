package com.paulograbin.insight.Activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.paulograbin.insight.Activity.Details.DetailsPlace;
import com.paulograbin.insight.Adapter.PlaceSelectionAdapter;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.Output.Speaker;

import java.util.ArrayList;
import java.util.List;

public class DestinySelectionActivity extends AppCompatActivity {

    ListView mList;
    List<Place> mPossibleDestinies;
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
            mCurrentPlace = (Place) intent.getSerializableExtra("place");
        }

        mCurrentLocation = new Location("teste");
        mCurrentLocation.setLatitude(mCurrentPlace.getLatitude());
        mCurrentLocation.setLongitude(mCurrentPlace.getLongitude());

        Log.i("Spiga", "CurrentPlace:" + mCurrentPlace.getName() + ", " + mCurrentPlace.getLatitude() + "/" + mCurrentPlace.getLongitude());
        Log.i("Spiga", "CurrentLocation:" + mCurrentLocation.getLatitude() + " - " + mCurrentLocation.getLongitude());

        mList = new ListView(this);
        mList.setId(android.R.id.list);

        mPossibleDestinies = new ArrayList<>();
        mAdapter = new PlaceSelectionAdapter(this, mPossibleDestinies, mCurrentLocation);
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

        if (mPossibleDestinies.size() == 1) {
            mSpeaker.say("Um possível destino encontrado");
        } else {
            mSpeaker.say(mPossibleDestinies.size() + " possíveis destinos encontrado");
        }
    }

    //TODO: todo android vem com o sintetizador de voz
    //TODO: inspiração veio quando minha namorada comentou que viu no trem...
}
