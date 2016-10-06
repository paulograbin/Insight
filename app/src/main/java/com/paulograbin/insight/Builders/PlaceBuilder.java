package com.paulograbin.insight.Builders;

import android.location.Location;

import com.paulograbin.insight.Model.Place;

/**
 * Created by i841059 on 06/10/16.
 */
public class PlaceBuilder {

    private final Place mPlace;


    public PlaceBuilder() {
        mPlace = new Place();
    }

    private PlaceBuilder(Place mPlace) {
        this.mPlace = mPlace;
    }

    public PlaceBuilder withName(String name) {
        mPlace.setName(name);

        return new PlaceBuilder(mPlace);
    }

    public PlaceBuilder withDescription(String description) {
        mPlace.setDescription(description);

        return new PlaceBuilder(mPlace);
    }

    public PlaceBuilder withMessage(String message) {
        mPlace.setMessage(message);

        return new PlaceBuilder(mPlace);
    }

    public PlaceBuilder setFavorite() {
        mPlace.setFavorite(Place.FAVORITE);

        return new PlaceBuilder(mPlace);
    }

    public PlaceBuilder setDestination() {
        mPlace.setDestination(Place.FINAL_DESTINATION);

        return new PlaceBuilder(mPlace);
    }

    public PlaceBuilder withLatitude(double latitude) {
        mPlace.setLatitude(latitude);

        return new PlaceBuilder(mPlace);
    }

    public PlaceBuilder withLongitude(double longitude) {
        mPlace.setLongitude(longitude);

        return new PlaceBuilder(mPlace);
    }

    public Place build() {
        return mPlace;
    }
}