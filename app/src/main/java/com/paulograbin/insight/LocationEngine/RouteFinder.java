package com.paulograbin.insight.LocationEngine;

import android.content.Context;
import android.util.Log;

import com.paulograbin.insight.DB.Provider.PathProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Path;
import com.paulograbin.insight.Model.Place;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by paulograbin on 16/08/15.
 */
public class RouteFinder {

    private static final String TAG = "RouteFinder";

    PlaceProvider mPlaceProvider;
    PathProvider mPathProvider;

    List<Vertex> nodes;
    List<Edge> edges;
    Graph graph;
    DijkstraAlgorithm da;

    LinkedList<Vertex> path;

    public RouteFinder(Context context, Place sourcePlace) {
        mPlaceProvider = new PlaceProvider(context);
        mPathProvider = new PathProvider(context);

        List<Place> places = mPlaceProvider.getAll();
        List<Path> paths = mPathProvider.getAll();

        nodes = new ArrayList<>();
        edges = new ArrayList<>();

        for (Place p : places) {
            nodes.add(getVertexFromPlace(p));
        }

        for (Path p : paths) {
            edges.add(getEdgeFromPath(p));
        }

        graph = new Graph(nodes, edges);

        da = new DijkstraAlgorithm(graph);
        da.execute(getVertexFromPlace(sourcePlace));

        path = da.getPath(nodes.get(2));

        for (Vertex vertex : path) {
            Log.i(TAG, vertex.toString());
        }
    }

//    public List<Place> getPath() {
//        ArrayList<Place> path = new
//    }

    private Edge getEdgeFromPath(Path p) {
        Place sourcePlace = mPlaceProvider.getByID(p.getPlace());
        Place destinationPlace = mPlaceProvider.getByID(p.getConnectedTo());
        int weight = p.getWeight();

        Vertex source = getVertexFromPlace(sourcePlace);
        Vertex destination = getVertexFromPlace(destinationPlace);

        return new Edge(source, destination, weight);
    }

    private Vertex getVertexFromPlace(Place p) {
        return new Vertex(String.valueOf(p.getId()), p.getName());
    }
}
