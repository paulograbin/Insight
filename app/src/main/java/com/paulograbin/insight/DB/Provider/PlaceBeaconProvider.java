package com.paulograbin.insight.DB.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.paulograbin.insight.DB.Table.TablePlaceBeacon;
import com.paulograbin.insight.Exceptions.RecordNotFoundException;
import com.paulograbin.insight.Model.PlaceBeacon;

/**
 * Created by paulograbin on 08/08/15.
 */
public class PlaceBeaconProvider extends AbstractProvider<PlaceBeacon> {

    public PlaceBeaconProvider(Context context) {
        super(context);
    }


    public PlaceBeacon getDummy() {
        return new PlaceBeacon(5, 5, BeaconProvider.MY_BEACON_UUID1);
    }

    @Override
    public String getTableName() {
        return TablePlaceBeacon.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues(PlaceBeacon pb) {
        ContentValues cv = new ContentValues();

        cv.put(TablePlaceBeacon.COLUMN_IDPLACE, pb.getIdPlace());
        cv.put(TablePlaceBeacon.COLUMN_IDBEACON, pb.getIdBeacon());
        cv.put(TablePlaceBeacon.COLUMN_UUID, pb.getUuid());
        cv.put(TablePlaceBeacon.COLUMN_CREATED_DATE, pb.getCreatedDate());
        cv.put(TablePlaceBeacon.COLUMN_CREATED_TIME, pb.getCreatedTime());

        return cv;
    }

    @Override
    public PlaceBeacon getFromCursor(Cursor c) {
        PlaceBeacon pb = new PlaceBeacon();
        int i = 0;

        pb.setId(c.getLong(i++));
        pb.setIdPlace(c.getLong(i++));
        pb.setIdBeacon(c.getLong(i++));
        pb.setUuid(c.getString(i++));
        pb.setCreatedDate(c.getString(i++));
        pb.setCreatedTime(c.getString(i++));

        return pb;
    }

    public PlaceBeacon getByUUID(String UUID) {
//        printToLog("Buscando PlaceBeacon a partir do UUID " + UUID);
        PlaceBeacon pb;

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TablePlaceBeacon.TABLE_NAME + " WHERE " + TablePlaceBeacon.COLUMN_UUID + " LIKE \"" + UUID + "\"", null);

        if (c.moveToFirst()) {
            pb = getFromCursor(c);
        } else {
            throw new RecordNotFoundException("PlaceBeacon not found");
        }

        c.close();
        db.close();

        return pb;
    }
}


