package com.paulograbin.insight.Activity.Details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.paulograbin.insight.DB.Provider.BeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Beacon;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.Model.PlaceBeacon;
import com.paulograbin.insight.R;

public class PlaceBeaconDetails extends AppCompatActivity {

    PlaceBeacon pb;
    Place p;
    Beacon b;

    EditText editPlaceBeaconId, editPlaceID, editPlaceName, editBeaconID, editBeaconUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_placebeacon);

        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra("placeBeaconId")) {
            long id = intent.getLongExtra("placeBeaconId", 1);

            PlaceBeaconProvider pbp = new PlaceBeaconProvider(this);
            pb = pbp.getByID(id);

            PlaceProvider pp = new PlaceProvider(this);
            p = pp.getByID(pb.getIdPlace());

            BeaconProvider bp = new BeaconProvider(this);
            b = bp.getByID(pb.getIdBeacon());
        }

        editPlaceBeaconId = (EditText) findViewById(R.id.textIDPlaceBeacon);
        editPlaceID = (EditText) findViewById(R.id.textIDPlace);
        editPlaceName = (EditText) findViewById(R.id.textPlaceName);
        editBeaconID = (EditText) findViewById(R.id.textBeaconID);
        editBeaconUUID = (EditText) findViewById(R.id.textBeaconUUID);

        editPlaceBeaconId.setText("" + pb.getId());
        editPlaceID.setText(p.getId() + "");
        editPlaceName.setText(p.getName());
        editBeaconID.setText(b.getId() + "");
        editBeaconUUID.setText(b.getUUID() + "");
    }
}
