package com.paulograbin.insight.DB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.paulograbin.insight.DB.Provider.BeaconProvider;
import com.paulograbin.insight.DB.Provider.PathProvider;
import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.DB.Table.TableBeacon;
import com.paulograbin.insight.DB.Table.TablePath;
import com.paulograbin.insight.DB.Table.TablePlace;
import com.paulograbin.insight.DB.Table.TablePlaceBeacon;
import com.paulograbin.insight.Model.Beacon;
import com.paulograbin.insight.Model.Path;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.Model.PlaceBeacon;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by paulograbin on 30/06/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 26;
    private static final String DATABASE_NAME = "insight.db";
    private static DatabaseHelper mDatabaseHelper;
    private static Context context;
    private final String TAG = "Database";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DatabaseHelper.context = context;
    }

    public static DatabaseHelper getInstance(Context context) {
        if (mDatabaseHelper == null)
            mDatabaseHelper = new DatabaseHelper(context);

        return mDatabaseHelper;
    }

    public void checkDatabase() {

        BeaconProvider bp = new BeaconProvider(context);
        PlaceProvider pp = new PlaceProvider(context);
        PlaceBeaconProvider pbp = new PlaceBeaconProvider(context);

        if ((bp.getCount() == 0) && (pp.getCount() == 0) && (pbp.getCount() == 0)) {
            insertStandardRecords();
        }
    }

    private static void insertStandardRecords() {
        /*
         * Places
         */
        Log.i("Database", "Inserindo registros padrão");
        Place pInitial = new Place("Ponto Inicial", "Um ponto no inicio mapa", "Mensagem de teste!", Place.NOT_FAVORITE, Place.FINAL_DESTINATION, -29.784246, -51.143911);
        Place pMid = new Place("Caminho entre pontos", "Um caminho no meio do mapa", "Segunda Mensagem de teste!", Place.NOT_FAVORITE, Place.FINAL_DESTINATION, -29.783780, -51.144770);
        Place pNowhere = new Place("Nowhere", "Algum lugar perdido", "Não faz parte do caminho", Place.NOT_FAVORITE, Place.NO_DESTINATION, -29.898537, -51.152074);
        Place pEnd = new Place("Ponto Final", "Um ponto no fim do mapa", "Ultima mensagem de teste", Place.FAVORITE, Place.FINAL_DESTINATION, -29.796614, -51.148895);

//        Place pInitial = new Place("Setor 2E", "Custom Developtment", "Siga pela direita até a porta de correr e então continue andando reto", Place.NOT_FAVORITE, Place.FINAL_DESTINATION, -29.78440, -51.14400);
//        Place pMid = new Place("Setor 2C", "Centro do prédio", "Dobre a direita e siga até o elevador", Place.NOT_FAVORITE, Place.NO_DESTINATION, -29.91305, -51.18932);
//        Place pNowhere = new Place("Setor 2D", "Algum lugar perdido", "Bla bla bla", Place.NOT_FAVORITE, Place.FINAL_DESTINATION, -29.99447, -50.78694);
//        Place pEnd = new Place("Cafeteria", "Restaurante da SAP", "Continue enfrente até a sala de recreação", Place.FAVORITE, Place.FINAL_DESTINATION, -30.03201, -51.21678);

        PlaceProvider pp = new PlaceProvider(context);
        pInitial.setId(pp.insert(pInitial));
        pMid.setId(pp.insert(pMid));
        pEnd.setId(pp.insert(pEnd));
        pNowhere.setId(pp.insert(pNowhere));


        /*
         * Beacon
         */
        Beacon b1 = new Beacon();
        b1.setUUID(BeaconProvider.FAROL_BEACON);
        b1.setNetworktype(12);
        b1.setMajor(12);
        b1.setMajor(1);
        b1.setChannel(13);
        b1.setLatitude(39.99);
        b1.setLongitude(30.00);
        b1.setLocation("Teste de location");

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

        b1.setCreatedDate(formatDate.format(Calendar.getInstance().getTime()));
        b1.setCreatedTime(formatTime.format(Calendar.getInstance().getTime()));

        BeaconProvider bp = new BeaconProvider(context);
        b1.setId(bp.insert(b1));


        Beacon b2 = new Beacon();
        b2.setUUID(BeaconProvider.MY_BEACON_UUID);
        b2.setNetworktype(12);
        b2.setMajor(12);
        b2.setMajor(1);
        b2.setChannel(13);
        b2.setLatitude(39.99);
        b2.setLongitude(30.00);
        b2.setLocation("Teste de location");

        b2.setCreatedDate(formatDate.format(Calendar.getInstance().getTime()));
        b2.setCreatedTime(formatTime.format(Calendar.getInstance().getTime()));

        b2.setId(bp.insert(b2));

        Beacon b3 = new Beacon();
        b3.setUUID(BeaconProvider.OTHER_BEACON_UUID);
        b3.setMajor(1);
        b3.setMajor(50);

        b3.setId(bp.insert(b3));


        /*
         * PlaceBeacon
         */
        PlaceBeacon pb1 = new PlaceBeacon(pInitial.getId(), b1.getId(), b1.getUUID());
        PlaceBeaconProvider pbp = new PlaceBeaconProvider(context);
        pb1.setId(pbp.insert(pb1));


        PlaceBeacon pb2 = new PlaceBeacon(pEnd.getId(), b3.getId(), b3.getUUID());
        pb2.setId(pbp.insert(pb2));

        PlaceBeacon pb3 = new PlaceBeacon(pMid.getId(), b2.getId(), b2.getUUID());
        pb3.setId(pbp.insert(pb3));


        /*
         * Path
         */
        Path ph1 = new Path(pInitial.getId(), pMid.getId(), 1);     // 10   20
        Path ph3 = new Path(pInitial.getId(), pNowhere.getId(), 1); // 10   30
        Path ph2 = new Path(pMid.getId(), pEnd.getId(), 1);         // 20   40
        Path ph4 = new Path(pMid.getId(), pNowhere.getId(), 1);


        PathProvider ph = new PathProvider(context);
        ph1.setId(ph.insert(ph1));
        ph2.setId(ph.insert(ph2));
        ph3.setId(ph.insert(ph3));
        ph4.setId(ph.insert(ph4));
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

//        insertStandardRecords();
    }

    public void dropTables() {
        printToLog("Dropping all records from all tables");
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TablePlace.TABLE_NAME, String.valueOf(1), null);
        db.delete(TableBeacon.TABLE_NAME, String.valueOf(1), null);
        db.delete(TablePlaceBeacon.TABLE_NAME, String.valueOf(1), null);
        db.delete(TablePath.TABLE_NAME, String.valueOf(1), null);

        insertStandardRecords();
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