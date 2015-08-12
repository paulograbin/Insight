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

    EditText PlaceBeaconId;
    EditText PlaceID;
    EditText PlaceName;
    EditText BeaconID;
    EditText BeaconUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_placebeacon);

        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            long id = intent.getLongExtra(Intent.EXTRA_TEXT, 1);

            PlaceBeaconProvider pbp = new PlaceBeaconProvider(this);
            pb = pbp.getByID(id);

            PlaceProvider pp = new PlaceProvider(this);
            p = pp.getByID(pb.getIdPlace());

            BeaconProvider bp = new BeaconProvider(this);
            b = bp.getByID(pb.getIdBeacon());
        }

        PlaceBeaconId = (EditText) findViewById(R.id.textIDPlaceBeacon);
        PlaceID = (EditText) findViewById(R.id.textIDPlace);
        PlaceName = (EditText) findViewById(R.id.textPlaceName);
        BeaconID = (EditText) findViewById(R.id.textBeaconID);
        BeaconUUID = (EditText) findViewById(R.id.textBeaconUUID);

        PlaceBeaconId.setText("" + pb.getId());
        PlaceID.setText(p.getId() + "");
        PlaceName.setText(p.getName());
        BeaconID.setText(b.getId() + "");
        BeaconUUID.setText(b.getUUID() + "");
    }
}
