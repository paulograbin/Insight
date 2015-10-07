package com.paulograbin.insight.Model;


/**
 * Created by paulograbin on 01/07/15.
 */
public class Beacon implements ModelInterface<Beacon> {

    private long id;
    private String uuid;
    private String location;
    private double latitude;
    private double longitude;

    public Beacon() { }


    @Override
    public boolean isEqualTo(Beacon object) {
        if (object instanceof Beacon) {
            if (this.uuid.equalsIgnoreCase(object.getUUID()))
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

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    @Override
    public String toString() {
        return uuid;
    }
}
