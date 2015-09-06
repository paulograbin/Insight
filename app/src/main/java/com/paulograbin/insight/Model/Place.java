package com.paulograbin.insight.Model;

import java.io.Serializable;

/**
 * Created by paulograbin on 06/08/15.
 */
public class Place implements ModelInterface<Place>, Serializable {

    public static final int NO_DESTINATION = 0;     // Place is only a way to another place
    public static final int FINAL_DESTINATION = 1;  // Place can be choosed as destination by the user
    public static final int NOT_FAVORITE = 0;
    public static final int FAVORITE = 1;

    long id;
    String name;
    String description;
    String message;
    int favorite;
    int destination;
    double latitude;
    double longitude;


    public Place() {
    }

    public Place(String name, String description, String message, int favorite, int destination, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.message = message;
        this.favorite = favorite;
        this.destination = destination;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Place(String name, String description, int destination) {
        this.name = name;
        this.description = description;
        this.destination = destination;
    }

    public Place(long id, String name, String description, int destination, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean isEqualTo(Place object) {
        if (object instanceof Place) {
            if (this.name.equalsIgnoreCase(object.getName()) &&
                    this.favorite == object.getFavorite() &&
                    this.latitude == object.getLatitude() &&
                    this.longitude == object.getLongitude())
                return true;
        }

        return false;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(id + " - " + name);

        if (latitude > 0 && longitude > 0) {
            sb.append(", lat/long " + latitude + "/" + longitude);
        }

        return sb.toString();
    }
}
