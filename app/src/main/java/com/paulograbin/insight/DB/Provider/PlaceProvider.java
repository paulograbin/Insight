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
        p.setMessage("Teste de mensagem dummy");
        p.setFavorite(0);
        p.setDestination(Place.FINAL_DESTINATION);
        p.setLatitude(39);
        p.setLongitude(39);

        return p;
    }

    @Override
    public ContentValues getContentValues(Place p) {
        printToLog("Obtendo contentValues do place " + p.getName());

        ContentValues cv = new ContentValues();

        cv.put(TablePlace.COLUMN_NAME, p.getName());
        cv.put(TablePlace.COLUMN_DESCRIPTION, p.getDescription());
        cv.put(TablePlace.COLUMN_MESSAGE, p.getMessage());
        cv.put(TablePlace.COLUMN_FAVORITE, p.getFavorite());
        cv.put(TablePlace.COLUMN_DESTINATION, p.getDestination());
        cv.put(TablePlace.COLUMN_LATITUDE, p.getLatitude());
        cv.put(TablePlace.COLUMN_LONGITUDE, p.getLongitude());

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

    public boolean setPlaceAsFavorite(long id) {

    }

    public List<Place> getAllFavoritePlaces() {
        
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
        p.setMessage(c.getString(i++));
        p.setFavorite(c.getInt(i++));
        p.setDestination(c.getInt(i++));
        p.setLatitude(c.getDouble(i++));
        p.setLongitude(c.getDouble(i++));

        return p;
    }
}
