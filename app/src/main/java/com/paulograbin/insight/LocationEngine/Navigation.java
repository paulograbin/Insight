package com.paulograbin.insight.LocationEngine;

import android.location.Location;
import android.util.Log;

import com.paulograbin.insight.Model.Place;

import java.util.List;

/**
 * Created by paulograbin on 11/09/15.
 */
public class Navigation {

    List<Place> path;
    Place mInitialPlace;
    Place mTargetPlace;

    int currentPlaceNumber;


    public Navigation(List<Place> path, Place sourcePlace, Place targetPlace) {
        this.path = path;
        this.mInitialPlace = sourcePlace;
        this.mTargetPlace = targetPlace;
        this.currentPlaceNumber = -1;
    }

    public Place get(int i) {
        Log.i("rola", "chamando com i" + i);
        return path.get(i);
    }

    public double getRemainingDistance() {
        double distance = 0;

        Location location = new Location("aux");
        Location location2 = new Location("aux2");

        Place starting = get(0);
        location.setLatitude(starting.getLatitude());
        location.setLongitude(starting.getLongitude());

        for (int i = 1; i < path.size(); i++) {
            Place p = get(i);

            location2.setLatitude(p.getLatitude());
            location2.setLongitude(p.getLongitude());

            distance += location.distanceTo(location2);

            starting = p;
            location.setLatitude(starting.getLatitude());
            location.setLongitude(starting.getLongitude());
        }

        return Math.round(distance);
    }

    public Place getCurrentPlace() {
        return path.get(currentPlaceNumber);
    }

    public Place getNextPlace() {
        currentPlaceNumber++;

        if (currentPlaceNumber < path.size())
            return path.get(currentPlaceNumber);
        else
            throw new RuntimeException("Acabou o caminho...");
    }

    public boolean isLastPlace() {
        if (currentPlaceNumber == (path.size() - 1))
            return true;
        else
            return false;
    }

    public int getPathSize() {
        return path.size();
    }

    public List<Place> getPath() {
        return path;
    }

    public int getCurrentPlaceNumber() {
        return currentPlaceNumber;
    }

    public Place getInitialPlace() {
        return mInitialPlace;
    }

    public Place getTargetPlace() {
        return mTargetPlace;
    }

    public Place checkPreviousPlace() {
        int placeIndex = currentPlaceNumber - 1;

        if (placeIndex >= 0 && placeIndex < path.size() - 1)
            return path.get(placeIndex);
        else
            throw new RuntimeException("There is no place before");
    }

    public Place checkNextPlace() {
        return path.get(currentPlaceNumber + 1);
    }
}
