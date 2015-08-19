package com.paulograbin.insight.Model;

/**
 * Created by paulograbin on 08/08/15.
 */
public class PlaceBeacon implements ModelInterface<PlaceBeacon> {

    long id;
    long idPlace;
    long idBeacon;
    String uuid;
    String createdDate;
    String createdTime;


    public PlaceBeacon() {
    }

    public PlaceBeacon(long idPlace, long idBeacon, String uuid) {
        this.uuid = uuid;
        this.idBeacon = idBeacon;
        this.idPlace = idPlace;
    }

    public PlaceBeacon(long idPlace, long idBeacon) {
        this.idPlace = idPlace;
        this.idBeacon = idBeacon;
    }

    public PlaceBeacon(long idPlace, String uuid) {
        this.idPlace = idPlace;
        this.uuid = uuid;
    }


    @Override
    public boolean isEqualTo(PlaceBeacon object) {
        if (object instanceof PlaceBeacon) {
            if (this.idPlace == object.getIdPlace() &&
                    this.idBeacon == object.getIdBeacon() &&
                    this.uuid == object.getUuid())
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
        return "PlaceBeacon id " + id + ", Place " + idPlace + " - Beacon " + idBeacon;
    }
}
