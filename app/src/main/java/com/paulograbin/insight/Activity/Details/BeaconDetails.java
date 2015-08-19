package com.paulograbin.insight.Activity.Details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.paulograbin.insight.DB.Provider.BeaconProvider;
import com.paulograbin.insight.Model.Beacon;
import com.paulograbin.insight.R;

public class BeaconDetails extends ActionBarActivity {

    EditText editUUID, editID, editNetworking, editMajor, editMinor, editChannel, editLatitude,
            editLongitude, editLocation, editMessage, editCreatedTime, editCreatedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_beacon);

        initFields();

        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String uuid = intent.getStringExtra(Intent.EXTRA_TEXT);
            Toast.makeText(this, uuid, Toast.LENGTH_SHORT).show();

            BeaconProvider bp = new BeaconProvider(this);
            Beacon b = bp.getByUUID(uuid);

            setFieldsFromBeacon(b);
        }
    }

    private void setFieldsFromBeacon(Beacon b) {
        editUUID.setText(b.getUUID());
        editID.setText("" + b.getId());
        editNetworking.setText("" + b.getNetworktype());
        editMajor.setText("" + b.getMajor());
        editMinor.setText("" + b.getMinor());
        editChannel.setText("" + b.getChannel());
        editLatitude.setText("" + b.getLatitude());
        editLongitude.setText("" + b.getLongitude());
        editLocation.setText(b.getLocation());
        editMessage.setText(b.getMessage());
        editCreatedDate.setText(b.getCreatedDate());
        editCreatedTime.setText(b.getCreatedTime());
    }

    private void initFields() {
        editUUID = (EditText) findViewById(R.id.editUUID);
        editID = (EditText) findViewById(R.id.editID);
        editNetworking = (EditText) findViewById(R.id.editNetworking);
        editMajor = (EditText) findViewById(R.id.editMajor);
        editMinor = (EditText) findViewById(R.id.editMinor);
        editChannel = (EditText) findViewById(R.id.editChannel);
        editLatitude = (EditText) findViewById(R.id.editLatitude);
        editLongitude = (EditText) findViewById(R.id.editLongitude);
        editLocation = (EditText) findViewById(R.id.editLocation);
        editMessage = (EditText) findViewById(R.id.editMessage);
        editCreatedTime = (EditText) findViewById(R.id.editCreatedTime);
        editCreatedDate = (EditText) findViewById(R.id.editCreatedDate);
    }
}
