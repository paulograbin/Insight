package com.paulograbin.insight.Activity.Details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.Output.Speaker;
import com.paulograbin.insight.R;

public class DetailsPlace extends AppCompatActivity {

    private TextView txtPlaceName;
    private TextView txtPlaceDescription;
    private TextView txtDistance;
    private Button btnGo;
    private Button btnFavorite;

    private Place mPlace;
    private Speaker mSpeaker;

    //TODO: Implement button Go and place description

    @Override
    protected void onResume() {
        super.onResume();
        mSpeaker = Speaker.getInstance(this);

        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra("place")) {
            mPlace = intent.getParcelableExtra("place");

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
                    mSpeaker.playWithAlert(mPlace.getName() + "  salvo como favorito.");
                } else {
                    mPlace.setFavorite(0);
                    mSpeaker.playWithAlert(mPlace.getName() + "  desmarcado como favorito.");
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
    }
}
