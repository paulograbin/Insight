package com.paulograbin.insight.Model;

/**
 * Created by paulograbin on 01/07/15.
 */
public class Beacon {

    long id;

    String UUID;
    String name;
    int networktype;
    int major;
    int minor;
    int channel;



    String location;
    double latitude;
    double longitude;
    String message;
    String createdDate;
    String createdTime;

    public Beacon() { }

    public Beacon(long id, String location, double latitude, double longitude, String message) {
        this.id = id;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public Beacon setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Beacon setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Beacon setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "Beacon id: " + id;
    }
}
