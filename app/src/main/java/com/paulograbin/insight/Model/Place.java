package com.paulograbin.insight.Model;

/**
 * Created by paulograbin on 06/08/15.
 */
public class Place implements ModelInterface<Place> {

    public static final int NO_DESTINATION = 0;
    public static final int FINAL_DESTINATION = 1;

    long id;
    String name;
    int destination;
    double latitude;
    double longitude;


    public Place() {
    }

    public Place(String name, int destination) {
        this.name = name;
        this.destination = destination;
    }

    public Place(long id, String name, int destination, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @Override
    public boolean isEqualTo(Place object) {
        if (object instanceof Place) {
            if (this.name.equalsIgnoreCase(object.getName()) &&
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
