package com.paulograbin.insight;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.paulograbin.insight.DB.DatabaseHelper;
import com.paulograbin.insight.Model.Beacon;
import com.paulograbin.insight.Model.Message;

import java.util.ArrayList;
import java.util.List;


public class MessageList extends ActionBarActivity {

    ArrayAdapter<String> mArrayAdapter;
    ListView messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        mArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());

        messageList = (ListView) findViewById(R.id.messageList);
        messageList.setAdapter(mArrayAdapter);

        refreshList();
    }

    public void refreshList() {
        mArrayAdapter.clear();
//        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
//
//        List<Message> messages = db.getAllMessages();
//
//        for(int i = 0; i < messages.size(); i++) {
//            mArrayAdapter.add(messages.get(i).toString());
//        }

        mArrayAdapter.add("Mensagem 1");
        mArrayAdapter.add("Mensagem 2");
    }
}
