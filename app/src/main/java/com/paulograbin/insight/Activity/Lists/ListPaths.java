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

    private ListView mListView;
    private List<Path> paths;
    private PathListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListView = new ListView(this);
        mListView.setId(android.R.id.list);

        paths = new ArrayList<>();
        mAdapter = new PathListAdapter(this, paths);
        mListView.setAdapter(mAdapter);

        setContentView(mListView);
        refreshList();
    }

    private void refreshList() {
        mAdapter.clear();

        PathProvider pp = new PathProvider(this);
        paths = pp.getAll();

        mAdapter.addAll(paths);
    }
}
