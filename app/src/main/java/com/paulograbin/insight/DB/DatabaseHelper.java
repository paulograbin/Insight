package com.paulograbin.insight.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.paulograbin.insight.Model.Beacon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by paulograbin on 30/06/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = "Spiga";

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "insight.db";

    SQLiteDatabase mDB;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = getWritableDatabase();
    }


    public void addDummyBeacon() {
        Random r = new Random();
        Long id = Math.abs(r.nextLong());

        Beacon b = new Beacon();

        b.setId(id);
        b.setCreatedDate(Calendar.getInstance().toString());
        b.setCreatedTime(Calendar.getInstance().toString());
        b.setLatitude(39.99);
        b.setLongitude(30.00);
        b.setLocation("Teste de location");
        b.setMessage("Teste de message");

        addBeacon(b);
    }

    // Adding new beacon
    public void addBeacon(Beacon beacon) {
        ContentValues cv = new ContentValues();
        cv.put(TableBeacon._ID, beacon.getId());
        cv.put(TableBeacon.COLUMN_LOCATION, beacon.getLocation());
        cv.put(TableBeacon.COLUMN_LATITUDE, beacon.getLatitude());
        cv.put(TableBeacon.COLUMN_LONGITUDE, beacon.getLongitude());
        cv.put(TableBeacon.COLUMN_CREATED_DATE, beacon.getCreatedDate());
        cv.put(TableBeacon.COLUMN_CREATED_TIME, beacon.getCreatedTime());
        cv.put(TableBeacon.COLUMN_MESSAGE, beacon.getMessage());

        mDB.insert(TableBeacon.TABLE_NAME, null, cv);

        Log.i(LOG_TAG, "Beacon inserido!");
    }

//    // Getting single beacon
//    public Beacon getBeacon(int id) {
//
//    }
//
    // Getting All Beacon
    public List<Beacon> getAllBeacons() {
        List<Beacon> beacons = new ArrayList<Beacon>();

        String query = "SELECT * FROM " + TableBeacon.TABLE_NAME + ";";

        Cursor cursor = mDB.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                Beacon b = new Beacon();
                b.setId(cursor.getLong(0));

                beacons.add(b);
            } while (cursor.moveToNext());
        }

        Log.i(LOG_TAG, beacons.toString());
        return beacons;
    }
//
    // Getting beacons Count
    public int getBeaconsCount() {
        String countQuery = "SELECT * FROM beacon";

        Cursor cursor = mDB.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        Log.i(LOG_TAG, "Table beacons tem " + count + " registros");
        return count;
    }

//    // Updating single beacon
//    public int updateBeacon(Beacon beacon) {
//
//    }
//
//    // Deleting single beacon
//    public void deleteBeacon(Beacon beacon) {
//
//    }

    // Deleting every beacon
    public void dropTableBeacon() {
        String dropTableQuery = "DELETE FROM beacon";

        mDB.execSQL(dropTableQuery);

        Log.i(LOG_TAG, "Matou todos");
    }
    

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_TAG, "Criando banco...");

        try {
            db.execSQL(TableBeacon.TABLE_CREATE_COMMAND);
            db.execSQL(TableMessage.TABLE_CREATE_COMMAND);
        } catch (SQLException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableBeacon.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableMessage.TABLE_NAME);

        onCreate(db);
    }
}