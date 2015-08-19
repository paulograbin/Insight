package com.paulograbin.insight.Activity.Lists;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.paulograbin.insight.Adapter.PathListAdapter;
import com.paulograbin.insight.DB.Provider.PathProvider;
import com.paulograbin.insight.Model.Path;

import java.util.ArrayList;
import java.util.List;

public class ListPaths extends ListActivity {

    ListView listView;
    List<Path> paths;
    PathListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = new ListView(this);
        listView.setId(android.R.id.list);

        paths = new ArrayList<>();
        mAdapter = new PathListAdapter(this, paths);
        listView.setAdapter(mAdapter);


        setContentView(listView);
        refreshList();
    }

    private void refreshList() {
        mAdapter.clear();

        PathProvider pp = new PathProvider(this);
        List<Path> paths = pp.getAll();

        mAdapter.addAll(paths);
    }
}
