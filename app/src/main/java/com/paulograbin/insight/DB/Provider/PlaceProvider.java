package com.paulograbin.insight.DB.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.paulograbin.insight.DB.Table.TablePlace;
import com.paulograbin.insight.Model.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulograbin on 06/08/15.
 */
public class PlaceProvider extends AbstractProvider<Place> {

    public PlaceProvider(Context context) {
        super(context);
    }


    @Override
    public String getTableName() {
        return TablePlace.TABLE_NAME;
    }

    @Override
    public Place getDummy() {
        Place p = new Place();

        p.setName("Buraco no chão 1");
        p.setDescription("Um simples buraco");
        p.setDestination(Place.FINAL_DESTINATION);
        p.setLatitude(39);
        p.setLongitude(39);

        return p;
    }

    @Override
    public ContentValues getContentValues(Place place) {
        printToLog("Obtendo contentValues do place " + place.getName());

        ContentValues cv = new ContentValues();

        cv.put(TablePlace.COLUMN_NAME, place.getName());
        cv.put(TablePlace.COLUMN_DESCRIPTION, place.getDescription());
        cv.put(TablePlace.COLUMN_DESTINATION, place.getDestination());
        cv.put(TablePlace.COLUMN_LATITUDE, place.getLatitude());
        cv.put(TablePlace.COLUMN_LONGITUDE, place.getLongitude());

        return cv;
    }

    public List<Place> getAllDestinationPlaces() {
        printToLog("Obtendo os registros de " + getTableName());

        List<Place> places = new ArrayList<>();
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        String query = "SELECT * FROM " + getTableName() + " WHERE " + TablePlace.COLUMN_DESTINATION + "= 1;";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                places.add(getFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        return places;
    }

    public List<Place> getAllNonDestinationPlaces() {
        printToLog("Obtendo os registros de " + getTableName());

        List<Place> places = new ArrayList<>();
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        String query = "SELECT * FROM " + getTableName() + " WHERE " + TablePlace.COLUMN_DESTINATION + "= 0;";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                places.add(getFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        return places;
    }

    @Override
    public Place getFromCursor(Cursor c) {
        if (c == null) {
            return null;
        }

        Place p = new Place();
        int i = 0;

        p.setId(c.getLong(i++));
        p.setName(c.getString(i++));
        p.setDescription(c.getString(i++));
        p.setDestination(c.getInt(i++));
        p.setLatitude(c.getDouble(i++));
        p.setLongitude(c.getDouble(i++));

        return p;
    }
}
