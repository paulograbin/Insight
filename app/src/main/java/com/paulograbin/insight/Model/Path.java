package com.paulograbin.insight.Model;

/**
 * Created by paulograbin on 13/08/15.
 */
public class Path implements ModelInterface<Path> {

    long id;
    long place;
    long connectedTo;
    int weight;


    public Path(long place, long connectedTo, int weight) {
        this.place = place;
        this.connectedTo = connectedTo;
        this.weight = weight;
    }

    public Path() {
    }


    @Override
    public boolean isEqualTo(Path object) {
        if (object instanceof Path) {
            if (this.place == object.getPlace() &&
                    this.connectedTo == object.getConnectedTo() &&
                    this.weight == object.getWeight())
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

    public long getConnectedTo() {
        return connectedTo;
    }

    public void setConnectedTo(long connectedTo) {
        this.connectedTo = connectedTo;
    }

    public long getPlace() {
        return place;
    }

    public void setPlace(long place) {
        this.place = place;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Place " + place + ", to " + connectedTo + " - " + " weight: " + weight;
    }
}
