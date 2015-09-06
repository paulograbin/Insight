package com.paulograbin.insight.DB.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.paulograbin.insight.DB.DatabaseHelper;
import com.paulograbin.insight.Exceptions.RecordNotFoundException;
import com.paulograbin.insight.Model.ModelInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulograbin on 08/08/15.
 */
public abstract class AbstractProvider<M extends ModelInterface> implements ProviderInterface<M> {

    DatabaseHelper mDatabaseHelper;

    AbstractProvider(Context context) {
        mDatabaseHelper = DatabaseHelper.getInstance(context);
    }

    public abstract String getTableName();

    @Override
    public abstract ContentValues getContentValues(M object);

    @Override
    public abstract M getFromCursor(Cursor c);


    @Override
    public int getCount() {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String countQuery = "SELECT * FROM " + getTableName();

        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        printToLog("Tabela " + getTableName() + " tem " + count + " registros");
        return count;
    }

    @Override
    public long insert(M object) {
        long newID;

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        newID = db.insertOrThrow(getTableName(), null, getContentValues(object));

        db.close();

        printToLog("Novo registro inserido em " + getTableName() + ", id " + newID);
        return newID;
    }

    @Override
    public M getByID(long id) throws RecordNotFoundException {
        printToLog("Obtendo registro de " + getTableName() + " através do id " + id);
        M object;

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + getTableName() + " WHERE " + BaseColumns._ID + " = " + id, null);

        if (c.moveToFirst()) {
            object = getFromCursor(c);
        } else {
            throw new RecordNotFoundException("Record not found in " + getTableName());
        }

        c.close();
        db.close();

        return object;
    }

    @Override
    public List<M> getAll() {
        printToLog("Obtendo os registros de " + getTableName());

        List<M> beacons = new ArrayList<>();
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        String query = "SELECT * FROM " + getTableName() + ";";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                beacons.add(getFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        return beacons;
    }

    @Override
    public int update(M object) {
        printToLog("Atualizando registro em " + getTableName() + ", id: " + object.getId());

        int affectedRows;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        ContentValues cv = getContentValues(object);

        affectedRows = db.update(getTableName(), cv, BaseColumns._ID + " = ?", new String[]{"" + object.getId()});

        db.close();
        return affectedRows;
    }

    @Override
    public int delete(M object) {
        printToLog("Deletando registro da tabela " + getTableName() + ", id " + object.getId());

        return this.delete(object.getId());
    }

    @Override
    public int delete(long id) {
        int affectedRows;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        affectedRows = db.delete(getTableName(), BaseColumns._ID + " = ?", new String[]{"" + id});

        printToLog("Deletando registro em" + getTableName() + ", id: " + id + ". " + affectedRows + " linhas afetadas.");
        return affectedRows;
    }

    @Override
    public int deleteAll() {
        int affectedRows;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        affectedRows = db.delete(getTableName(), "1", null);

        printToLog("Limpando tabela " + getTableName() + ", " + affectedRows + " registros afetados.");
        return affectedRows;
    }

    @Override
    public void printToLog(String msg) {
        String LOG_TAG = "Database";
        Log.i(LOG_TAG, msg);
    }
}
