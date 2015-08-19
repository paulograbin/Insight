package com.paulograbin.insight.DB.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.paulograbin.insight.DB.Table.TableBeacon;
import com.paulograbin.insight.Model.Beacon;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by paulograbin on 24/07/15.
 */
public class BeaconProvider extends AbstractProvider<Beacon> {

    public final static String MY_BEACON_UUID = "5D8DE2E5-2C6D-4F3D-8651-DD66B7E4BD3E";
    public final static String OTHER_BEACON_UUID = "2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6";
    public final static String FAROL_BEACON = "64657665-6c6f-7064-6279-6d656e766961";


    public BeaconProvider(Context context) {
        super(context);
    }


    @Override
    public Beacon getDummy() {
        Beacon b = new Beacon();
        int i = (int) (Math.random() * 100);

        b.setUUID(i + "-5D8DE2E5-2C6D-4F3D-8651-DD66B7E4BD3E");
        b.setNetworktype(12);
        b.setMajor(12);
        b.setMajor(1);
        b.setChannel(13);
        b.setLatitude(39.99);
        b.setLongitude(30.00);
        b.setLocation("Teste de location");
        b.setMessage("Teste de message");

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

        b.setCreatedDate(formatDate.format(Calendar.getInstance().getTime()));
        b.setCreatedTime(formatTime.format(Calendar.getInstance().getTime()));

        return b;
    }

    @Override
    public String getTableName() {
        return TableBeacon.TABLE_NAME;
    }

    @Override
    public Beacon getFromCursor(Cursor c) {
        if (c == null)
            return null;


        Beacon b = new Beacon();
        int i = 0;

        b.setId(c.getLong(i++));
        b.setUUID(c.getString(i++));
        b.setName(c.getString(i++));
        b.setNetworktype(c.getInt(i++));
        b.setMajor(c.getInt(i++));
        b.setMinor(c.getInt(i++));
        b.setChannel(c.getInt(i++));
        b.setLocation(c.getString(i++));
        b.setLatitude(c.getDouble(i++));
        b.setLongitude(c.getDouble(i++));
        b.setMessage(c.getString(i++));
        b.setCreatedDate(c.getString(i++));
        b.setCreatedTime(c.getString(i++));

        printToLog("Beacon retornado do cursor: " + b.getUUID());

        return b;
    }

    public ContentValues getContentValues(Beacon beacon) {
        printToLog("Obtendo contentValues do beacon " + beacon.getId());

        ContentValues cv = new ContentValues();

        cv.put(TableBeacon.COLUMN_UUID, beacon.getUUID());
        cv.put(TableBeacon.COLUMN_LOCATION, beacon.getLocation());
        cv.put(TableBeacon.COLUMN_LATITUDE, beacon.getLatitude());
        cv.put(TableBeacon.COLUMN_LONGITUDE, beacon.getLongitude());
        cv.put(TableBeacon.COLUMN_CREATED_DATE, beacon.getCreatedDate());
        cv.put(TableBeacon.COLUMN_CREATED_TIME, beacon.getCreatedTime());
        cv.put(TableBeacon.COLUMN_MESSAGE, beacon.getMessage());
        cv.put(TableBeacon.COLUMN_NAME, beacon.getName());
        cv.put(TableBeacon.COLUMN_NETWORKTYPE, beacon.getNetworktype());
        cv.put(TableBeacon.COLUMN_MAJOR, beacon.getMajor());
        cv.put(TableBeacon.COLUMN_MINOR, beacon.getMinor());
        cv.put(TableBeacon.COLUMN_CHANNEL, beacon.getChannel());
        cv.put(TableBeacon.COLUMN_CREATED_DATE, beacon.getCreatedDate());
        cv.put(TableBeacon.COLUMN_CREATED_TIME, beacon.getCreatedTime());

        return cv;
    }

    public Beacon getByUUID(String uuid) throws SQLiteException {
        printToLog("Buscando beacon a partir do UUID " + uuid);
        Beacon b = null;

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TableBeacon.TABLE_NAME + " WHERE " + TableBeacon.COLUMN_UUID + " LIKE \"" + uuid + "\"", null);

        if (c.moveToFirst()) {
            b = getFromCursor(c);
        } else {
            throw new SQLiteException("Beacon not found");
        }

        c.close();
        db.close();

        return b;
    }
}