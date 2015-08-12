package com.paulograbin.insight.DB.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.paulograbin.insight.DB.Table.TablePlaceBeacon;
import com.paulograbin.insight.Model.PlaceBeacon;

/**
 * Created by paulograbin on 08/08/15.
 */
public class PlaceBeaconProvider extends AbstractProvider<PlaceBeacon> {

    private final String TABLE_NAME = TablePlaceBeacon.TABLE_NAME;

    public PlaceBeaconProvider(Context context) {
        super(context);
    }

    public PlaceBeacon getDummy() {
        return new PlaceBeacon(5, 5);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues(PlaceBeacon object) {
        ContentValues cv = new ContentValues();

        cv.put(TablePlaceBeacon.COLUMN_IDPLACE, object.getIdPlace());
        cv.put(TablePlaceBeacon.COLUMN_IDBEACON, object.getIdBeacon());
        cv.put(TablePlaceBeacon.COLUMN_CREATED_DATE, object.getCreatedDate());
        cv.put(TablePlaceBeacon.COLUMN_CREATED_TIME, object.getCreatedTime());

        return cv;
    }

    @Override
    public PlaceBeacon getFromCursor(Cursor c) {
        if (c == null)
            return null;

        PlaceBeacon pb = new PlaceBeacon();
        int i = 0;

        pb.setId(c.getLong(i++));
        pb.setIdPlace(c.getLong(i++));
        pb.setIdBeacon(c.getLong(i++));
        pb.setCreatedDate(c.getString(i++));
        pb.setCreatedTime(c.getString(i++));

        return pb;
    }
}


