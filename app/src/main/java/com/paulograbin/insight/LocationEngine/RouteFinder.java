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

    private boolean debugExecution = true;

    PlaceProvider mPlaceProvider;
    PathProvider mPathProvider;

    Place sourcePlace;
    Place targetPlace;

    List<Vertex> nodes;
    List<Edge> edges;
    LinkedList<Vertex> path;
    DijkstraAlgorithm da;


    public RouteFinder(Context context, Place sourcePlace, Place targetPlace) {
        this.sourcePlace = sourcePlace;
        this.targetPlace = targetPlace;

        path = new LinkedList<>();

        getNodesAndEdgesFromPlacesAndPaths(context);

        da = new DijkstraAlgorithm(nodes, edges);
        da.execute(getVertexFromPlace(sourcePlace));

        path = da.getPath(getVertexFromPlace(targetPlace));

        if(path != null) {
            for (Vertex vertex : path) {
                Log.i(TAG, vertex.toString());
            }
        } else {
            printToLog("Caminho não encontrado...");
        }
    }

    public LinkedList<Place> getPathToTargetPlace() {
        LinkedList<Place> convertedPath = new LinkedList<>();

        if(path != null) {
            for (Vertex v : path) {
                convertedPath.add(getPlaceFromVertex(v));
            }
        } else {
            return null;
        }

        return convertedPath;
    }

    private void getNodesAndEdgesFromPlacesAndPaths(Context context) {
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
    }

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

    private Place getPlaceFromVertex(Vertex v) {
        Place p = mPlaceProvider.getByID(Long.valueOf(v.getId()));

        return p;
    }

    private void printToLog(String message) {
        if (debugExecution)
            Log.i(TAG, message);
    }
}
