package com.paulograbin.insight.Model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by paulograbin on 06/08/15.
 */
public class Place implements ModelInterface<Place>, Parcelable {

    public static final int NO_DESTINATION = 0;     // Place is only a way to another source
    public static final int FINAL_DESTINATION = 1;  // Place can be choosed as destination by the user
    public static final int NOT_FAVORITE = 0;
    public static final int FAVORITE = 1;
    public static final Parcelable.Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel source) {
            return new Place(source);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[0];
        }
    };
    long id;
    String name;
    String description;
    String message;
    int favorite;
    int destination;
    double latitude;
    double longitude;
    Location location;

    public Place() {
        location = new Location("");
    }

    public Place(String name, String description, String message, int favorite, int destination, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.message = message;
        this.favorite = favorite;
        this.destination = destination;
        this.latitude = latitude;
        this.longitude = longitude;

        location = new Location(name);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
    }

    public Place(long id, String name, String description, int destination, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.latitude = latitude;
        this.longitude = longitude;

        location = new Location(name);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
    }

    public Place(Parcel p) {
        this.id = p.readLong();
        this.name = p.readString();
        this.description = p.readString();
        this.message = p.readString();
        this.favorite = p.readInt();
        this.destination = p.readInt();
        this.latitude = p.readDouble();
        this.longitude = p.readDouble();
        this.location = Location.CREATOR.createFromParcel(p);
    }

    @Override
    public boolean isEqualTo(Place anotherPlace) {
        if (anotherPlace instanceof Place) {
            if (this.name.equalsIgnoreCase(anotherPlace.getName()) &&
                    this.latitude == anotherPlace.getLatitude() &&
                    this.longitude == anotherPlace.getLongitude())
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
        this.location.setLatitude(latitude);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        this.location.setLongitude(longitude);
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(id + " - " + name);

        if (latitude > 0 && longitude > 0) {
            sb.append(", lat/long " + latitude + "/" + longitude);
        }

        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(message);
        dest.writeInt(favorite);
        dest.writeInt(destination);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeParcelable(location, 0);
    }
}
