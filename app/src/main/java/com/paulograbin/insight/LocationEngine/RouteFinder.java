package com.paulograbin.insight.LocationEngine;

import android.content.Context;
import android.util.Log;

import com.paulograbin.insight.DB.Provider.PathProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Path;
import com.paulograbin.insight.Model.Place;

import java.util.List;

/**
 * Created by paulograbin on 16/08/15.
 */
public class RouteFinder {

    Place initialPlace;
    Place destinationPlace;

    List<Place> placesToWalk;

    private PlaceProvider mPlaceProvider;
    private PathProvider mPathProvider;

    public RouteFinder(Context context, long initialPlaceId, long idDestinationPlaceId) {
        mPlaceProvider = new PlaceProvider(context);
        mPathProvider = new PathProvider(context);

        initialPlace = mPlaceProvider.getByID(initialPlaceId);
        destinationPlace = mPlaceProvider.getByID(idDestinationPlaceId);
    }

    public void findWayToPlace() {
        List<Path> paths = mPathProvider.getAllPlaceConnections(initialPlace.getId());

        for (Path p : paths) {
            Log.i("Spiga", "Initial place is connected to " + mPlaceProvider.getByID(p.getId()).getName());
        }

    }
}
