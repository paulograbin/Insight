package com.paulograbin.insight.Activity.Lists;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.paulograbin.insight.DB.Provider.MessageProvider;
import com.paulograbin.insight.Model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ListMessages extends ActionBarActivity {

    TextToSpeech tts;
    ListView listView;
    ArrayAdapter<String> mArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.getDefault());
                }
            }
        });

        mArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());

        listView = new ListView(this);
        listView.setAdapter(mArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Spiga", mArrayAdapter.getItem(position).toString());
                tts.speak(mArrayAdapter.getItem(position), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        setContentView(listView);
        refreshList();
    }

    public void refreshList() {
        mArrayAdapter.clear();

        MessageProvider mp = new MessageProvider(this);
        List<Message> messages = mp.getAll();

        for (int i = 0; i < messages.size(); i++) {
            mArrayAdapter.add(messages.get(i).toString());
        }
    }
}
