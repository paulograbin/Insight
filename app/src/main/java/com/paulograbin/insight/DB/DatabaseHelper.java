package com.paulograbin.insight.DB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.paulograbin.insight.DB.Provider.BeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.DB.Records.DemoRecords;
import com.paulograbin.insight.DB.Records.Records;
import com.paulograbin.insight.DB.Table.TableBeacon;
import com.paulograbin.insight.DB.Table.TablePath;
import com.paulograbin.insight.DB.Table.TablePlace;
import com.paulograbin.insight.DB.Table.TablePlaceBeacon;

/**
 * Created by paulograbin on 30/06/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 29;
    private static final String DATABASE_NAME = "insight.db";
    private static DatabaseHelper mDatabaseHelper;
    private static Context context;
    private final String TAG = "Database";
    private Records mRecordInserter;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DatabaseHelper.context = context;


        mRecordInserter = new DemoRecords();
    }

    public static DatabaseHelper getInstance(Context context) {
        if (mDatabaseHelper == null)
            mDatabaseHelper = new DatabaseHelper(context);

        return mDatabaseHelper;
    }

    public void checkDatabase() {
        printToLog("Checking database...");

        BeaconProvider bp = new BeaconProvider(context);
        PlaceProvider pp = new PlaceProvider(context);
        PlaceBeaconProvider pbp = new PlaceBeaconProvider(context);

        if ((bp.getCount() == 0) && (pp.getCount() == 0) && (pbp.getCount() == 0)) {
            insertRecords();
        }
    }

    public void insertRecords() {
        Log.i("Database", "Inserting records...");

        mRecordInserter.insertRecords(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        printToLog("Criando tabelas...");

        try {
            createTables(db);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void createTables(SQLiteDatabase db) {
        printToLog(TableBeacon.TABLE_CREATE_COMMAND);
        db.execSQL(TableBeacon.TABLE_CREATE_COMMAND);

        printToLog(TablePlace.TABLE_CREATE_COMMAND);
        db.execSQL(TablePlace.TABLE_CREATE_COMMAND);

        printToLog(TablePlaceBeacon.TABLE_CREATE_COMMAND);
        db.execSQL(TablePlaceBeacon.TABLE_CREATE_COMMAND);

        printToLog(TablePath.TABLE_CREATE_COMMAND);
        db.execSQL(TablePath.TABLE_CREATE_COMMAND);
    }

    public void dropTables() {
        printToLog("Dropping all records from all tables");
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TablePlace.TABLE_NAME, String.valueOf(1), null);
        db.delete(TableBeacon.TABLE_NAME, String.valueOf(1), null);
        db.delete(TablePlaceBeacon.TABLE_NAME, String.valueOf(1), null);
        db.delete(TablePath.TABLE_NAME, String.valueOf(1), null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        printToLog("Upgrading - Dropping all records from all tables");

        db.execSQL("DROP TABLE IF EXISTS " + TableBeacon.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TablePlace.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TablePlaceBeacon.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TablePath.TABLE_NAME);

        onCreate(db);
    }

    private void printToLog(String message) {
        Log.i(TAG, message);
    }
}