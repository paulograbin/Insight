package com.paulograbin.insight.DB.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.paulograbin.insight.DB.Table.TablePath;
import com.paulograbin.insight.Model.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulograbin on 13/08/15.
 */
public class PathProvider extends AbstractProvider<Path> {

    public PathProvider(Context context) {
        super(context);
    }


    @Override
    public String getTableName() {
        return TablePath.TABLE_NAME;
    }

    @Override
    public Path getDummy() {
        return new Path(1L, 3L, 1);
    }

    public List<Path> getAllPlaceConnections(long placeId) {
        List<Path> paths = new ArrayList<>();

        String query = "Select * " +
                "from " + getTableName() +
                " where " + TablePath.COLUMN_PLACE + " = " + placeId;

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            paths.add(getFromCursor(c));
        }

        return paths;
    }

    public List<Path> getAllPlacesConnectedTo(long placeId) {
        List<Path> paths = new ArrayList<>();

        String query = "Select * " +
                "from " + getTableName() +
                " where " + TablePath.COLUMN_CONNECTED_TO + " = " + placeId;

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            paths.add(getFromCursor(c));
        }

        return paths;
    }

    @Override
    public ContentValues getContentValues(Path object) {
        ContentValues cv = new ContentValues();

        cv.put(TablePath.COLUMN_PLACE, object.getPlace());
        cv.put(TablePath.COLUMN_CONNECTED_TO, object.getConnectedTo());
        cv.put(TablePath.COLUMN_WEIGHT, object.getWeight());

        return cv;
    }

    @Override
    public Path getFromCursor(Cursor c) {
        Path p = new Path();

        int i = 0;
        p.setId(c.getLong(i++));
        p.setPlace(c.getLong(i++));
        p.setConnectedTo(c.getLong(i++));
        p.setWeight(c.getInt(i++));

        return p;
    }
}