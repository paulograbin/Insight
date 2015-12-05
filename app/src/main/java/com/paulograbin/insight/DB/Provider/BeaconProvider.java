package com.paulograbin.insight.DB.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.paulograbin.insight.DB.Table.TableBeacon;
import com.paulograbin.insight.Exceptions.RecordNotFoundException;
import com.paulograbin.insight.Model.Beacon;

import java.text.SimpleDateFormat;

/**
 * Created by paulograbin on 24/07/15.
 */
public class BeaconProvider extends AbstractProvider<Beacon> {

    public final static String MY_BEACON_UUID1 = "5D8DE2E5-2C6D-4F3D-8651-DD66B7E4BD31";
    public final static String MY_BEACON_UUID2 = "5D8DE2E5-2C6D-4F3D-8651-DD66B7E4BD32";
    public final static String MY_BEACON_UUID3 = "5D8DE2E5-2C6D-4F3D-8651-DD66B7E4BD33";
    public final static String MY_BEACON_UUID4 = "5D8DE2E5-2C6D-4F3D-8651-DD66B7E4BD34";
    public final static String MY_BEACON_UUID5 = "5D8DE2E5-2C6D-4F3D-8651-DD66B7E4BD35";
    public final static String MY_BEACON_UUID6 = "5D8DE2E5-2C6D-4F3D-8651-DD66B7E4BD36";
    public final static String MY_BEACON_UUID7 = "5D8DE2E5-2C6D-4F3D-8651-DD66B7E4BD37";

    public final static String FAROL_BEACON = "64657665-6c6f-7064-6279-6D656E766961";

    public final static String BEACON_AMARELO  = "ebefd083-70a2-47c8-9837-e7b5634df524";
    public final static String BEACON_AZUL     = "ebefd083-70a2-47c8-9837-e7b5634df525";
    public final static String BEACON_PRETO    = "ebefd083-70a2-47c8-9837-e7b5634df526";
    public final static String BEACON_BRANCO_1 = "ebefd083-70a2-47c8-9837-e7b5634df527";
    public final static String BEACON_BRANCO_2 = "ebefd083-70a2-47c8-9837-e7b5634df528";


    public BeaconProvider(Context context) {
        super(context);
    }

    @Override
    public Beacon getDummy() {
        Beacon b = new Beacon();
        int i = (int) (Math.random() * 100);

        b.setUUID(i + "-5D8DE2E5-2C6D-4F3D-8651-DD66B7E4BD3E");

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

        return b;
    }

    @Override
    public String getTableName() {
        return TableBeacon.TABLE_NAME;
    }

    @Override
    public Beacon getFromCursor(Cursor c) {
        Beacon b = new Beacon();
        int i = 0;

        b.setId(c.getLong(i++));
        b.setUUID(c.getString(i));

        printToLog("Beacon retornado do cursor: " + b.getUUID());

        return b;
    }

    public ContentValues getContentValues(Beacon beacon) {
//        printToLog("Obtendo contentValues do beacon " + beacon.getId());

        ContentValues cv = new ContentValues();

        cv.put(TableBeacon.COLUMN_UUID, beacon.getUUID());

        return cv;
    }

    public Beacon getByUUID(String uuid) throws RecordNotFoundException {
        printToLog("Buscando beacon a partir do UUID " + uuid);
        Beacon b;

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TableBeacon.TABLE_NAME + " WHERE " + TableBeacon.COLUMN_UUID + " LIKE \"" + uuid + "\"", null);

        if (c.moveToFirst()) {
            b = getFromCursor(c);
        } else {
            throw new RecordNotFoundException("Beacon not found");
        }

        c.close();
        db.close();

        return b;
    }
}