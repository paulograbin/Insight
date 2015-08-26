package com.paulograbin.insight.DB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.paulograbin.insight.DB.Provider.BeaconProvider;
import com.paulograbin.insight.DB.Provider.MessageProvider;
import com.paulograbin.insight.DB.Provider.PathProvider;
import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.DB.Table.TableBeacon;
import com.paulograbin.insight.DB.Table.TableMessage;
import com.paulograbin.insight.DB.Table.TablePath;
import com.paulograbin.insight.DB.Table.TablePlace;
import com.paulograbin.insight.DB.Table.TablePlaceBeacon;
import com.paulograbin.insight.Model.Beacon;
import com.paulograbin.insight.Model.Message;
import com.paulograbin.insight.Model.Path;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.Model.PlaceBeacon;
import com.paulograbin.insight.Util.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by paulograbin on 30/06/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    static final int DATABASE_VERSION = 20;
    static final String DATABASE_NAME = "insight.db";
    private static DatabaseHelper mDatabaseHelper;
    private static Context context;
    private final String TAG = "Spiga";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DatabaseHelper.context = context;
    }

    public static DatabaseHelper getInstance(Context context) {
        if (mDatabaseHelper == null)
            mDatabaseHelper = new DatabaseHelper(context);

        return mDatabaseHelper;
    }

    public static void insertStandardRecords() {
        /*
         * Mensagens padrão
         */
        Message m1 = new Message("Você chegou ao seu destino.");
        Message m2 = new Message("Você está em ");

        MessageProvider mp = new MessageProvider(context);
        m1.setId(mp.insert(m1));
        m2.setId(mp.insert(m2));

        /*
         * Places
         */
        Place p1 = new Place("Ponto Inicial", 1);
        Place p2 = new Place("Ponto Final", 0);
        Place p3 = new Place("Caminho entre pontos", 1);

        PlaceProvider pp = new PlaceProvider(context);
        p1.setId(pp.insert(p1));
        p2.setId(pp.insert(p2));
        p3.setId(pp.insert(p3));

        /*
         * Beacon
         */
        Beacon b1 = new Beacon();
        b1.setUUID(BeaconProvider.MY_BEACON_UUID);
        b1.setNetworktype(12);
        b1.setMajor(12);
        b1.setMajor(1);
        b1.setChannel(13);
        b1.setLatitude(39.99);
        b1.setLongitude(30.00);
        b1.setLocation("Teste de location");
        b1.setMessage("Teste de message");

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

        b1.setCreatedDate(formatDate.format(Calendar.getInstance().getTime()));
        b1.setCreatedTime(formatTime.format(Calendar.getInstance().getTime()));

        BeaconProvider bp = new BeaconProvider(context);
        b1.setId(bp.insert(b1));


        Beacon b2 = new Beacon();
        b2.setUUID(BeaconProvider.OTHER_BEACON_UUID);
        b2.setNetworktype(12);
        b2.setMajor(12);
        b2.setMajor(1);
        b2.setChannel(13);
        b2.setLatitude(39.99);
        b2.setLongitude(30.00);
        b2.setLocation("Teste de location");
        b2.setMessage("Teste de message");

        b2.setCreatedDate(formatDate.format(Calendar.getInstance().getTime()));
        b2.setCreatedTime(formatTime.format(Calendar.getInstance().getTime()));

        b2.setId(bp.insert(b2));

        Beacon b3 = new Beacon();
        b3.setUUID(BeaconProvider.FAROL_BEACON);
        b3.setMajor(1);
        b3.setMajor(50);

        b3.setId(bp.insert(b3));

        /*
         * PlaceBeacon
         */
        PlaceBeacon pb1 = new PlaceBeacon(p1.getId(), b1.getId(), b1.getUUID());
        PlaceBeaconProvider pbp = new PlaceBeaconProvider(context);
        pb1.setId(pbp.insert(pb1));


        PlaceBeacon pb2 = new PlaceBeacon(p2.getId(), b2.getId(), b2.getUUID());
        pb2.setId(pbp.insert(pb2));

        PlaceBeacon pb3 = new PlaceBeacon(p3.getId(), b3.getId(), b3.getUUID());
        pb3.setId(pbp.insert(pb3));
        /*
         * Path
         */
        Path ph1 = new Path(p1.getId(), p3.getId(), 1);
        Path ph2 = new Path(p3.getId(), p2.getId(), 1);
        PathProvider ph = new PathProvider(context);
        ph1.setId(ph.insert(ph1));
        ph2.setId(ph.insert(ph2));
    }

    public static void copiaDB(Context context) {

        File source = new File(context.getFilesDir().getParent() + "/databases/" + DatabaseHelper.DATABASE_NAME);
        File destination = new File(context.getFilesDir() + DatabaseHelper.DATABASE_NAME);

        try {
            Util.copiaArquivos(source, destination);
        } catch (Exception e) {
//            Toast.makeText(context, "ops" + e.getClass().toString(), Toast.LENGTH_LONG).show();
            Log.e("Spiga", e.getLocalizedMessage());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Criando tabelas...");

        try {
            createTables(db);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void createTables(SQLiteDatabase db) {
        Log.i(TAG, TableBeacon.TABLE_CREATE_COMMAND);
        db.execSQL(TableBeacon.TABLE_CREATE_COMMAND);

        Log.i(TAG, TableMessage.TABLE_CREATE_COMMAND);
        db.execSQL(TableMessage.TABLE_CREATE_COMMAND);

        Log.i(TAG, TablePlace.TABLE_CREATE_COMMAND);
        db.execSQL(TablePlace.TABLE_CREATE_COMMAND);

        Log.i(TAG, TablePlaceBeacon.TABLE_CREATE_COMMAND);
        db.execSQL(TablePlaceBeacon.TABLE_CREATE_COMMAND);

        Log.i(TAG, TablePath.TABLE_CREATE_COMMAND);
        db.execSQL(TablePath.TABLE_CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableBeacon.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableMessage.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TablePlace.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TablePlaceBeacon.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TablePath.TABLE_NAME);

        onCreate(db);
    }
}