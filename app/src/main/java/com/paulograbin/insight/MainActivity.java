package com.paulograbin.insight;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.paulograbin.insight.Bluetooth.BleWrapper;
import com.paulograbin.insight.Bluetooth.BleWrapperUiCallbacks;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
