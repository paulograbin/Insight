package com.paulograbin.insight.Activity.Details;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.R;

import java.util.Locale;

public class DetailsPlace extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextView txtPlaceName;
    private TextView txtPlaceDescription;
    private TextView txtDistance;
    private Button btnGo;
    private Button btnFavorite;

    private Place mPlace;
    private TextToSpeech tts;

    @Override
    protected void onPause() {
        super.onPause();

        tts.stop();
        tts.shutdown();
        tts = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra("place")) {
            mPlace = (Place) intent.getSerializableExtra("place");

            txtPlaceName.setText(mPlace.getName());
            txtPlaceDescription.setText(mPlace.getDescription());
//            txtDistance.setText("Distance até o place"); //TODO

            if(mPlace.getFavorite() == 0) {
                btnFavorite.setText("Marcar como favorito");
            } else {
                btnFavorite.setText("Desmarcar como favorito");
            }
        }

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlace.getFavorite() == 0) {
                    mPlace.setFavorite(1);
                    say(mPlace.getName() + "  salvo como favorito.");
                } else {
                    mPlace.setFavorite(0);
                    say(mPlace.getName() + "  desmarcado como favorito.");
                }

                new PlaceProvider(getApplicationContext()).update(mPlace);
                onResume();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        txtPlaceName = (TextView) findViewById(R.id.txtPlaceName);
        txtPlaceDescription = (TextView) findViewById(R.id.txtPlaceDescription);
        txtDistance = (TextView) findViewById(R.id.txtPlaceDistance);
        btnGo = (Button) findViewById(R.id.btnGo);
        btnFavorite = (Button) findViewById(R.id.btnFavorite);

        tts = new TextToSpeech(this, this);
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

    private void say(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
