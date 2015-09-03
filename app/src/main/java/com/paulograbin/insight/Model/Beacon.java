package com.paulograbin.insight.Model;


/**
 * Created by paulograbin on 01/07/15.
 */
public class Beacon implements ModelInterface<Beacon> {

    private long id;
    private String uuid;
    private String name;
    private int networktype;
    private int major;
    private int minor;
    private int channel;
    private String location;
    private double latitude;
    private double longitude;
    private String createdDate;
    private String createdTime;


    public Beacon() { }


    @Override
    public boolean isEqualTo(Beacon object) {
        if (object instanceof Beacon) {
            if (this.uuid.equalsIgnoreCase(object.getUUID()) &&
                    this.major == object.getMajor() &&
                    this.minor == object.getMinor())
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNetworktype() {
        return networktype;
    }

    public void setNetworktype(int networktype) {
        this.networktype = networktype;
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

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
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
        return uuid;
    }
}
