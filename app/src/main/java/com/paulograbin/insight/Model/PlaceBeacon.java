package com.paulograbin.insight.Model;

/**
 * Created by paulograbin on 08/08/15.
 */
public class PlaceBeacon implements ModelInterface {

    long id;
    long idPlace;
    long idBeacon;
    String createdDate;
    String createdTime;

    public PlaceBeacon() {
    }

    public PlaceBeacon(long idPlace, long idBeacon) {
        this.idPlace = idPlace;
        this.idBeacon = idBeacon;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(long idPlace) {
        this.idPlace = idPlace;
    }

    public long getIdBeacon() {
        return idBeacon;
    }

    public void setIdBeacon(long idBeacon) {
        this.idBeacon = idBeacon;
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
        return id + " / " + idPlace + " - " + idBeacon;
    }
}
