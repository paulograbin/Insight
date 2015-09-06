package com.paulograbin.insight.DB.Provider;

import android.content.ContentValues;
import android.database.Cursor;

import com.paulograbin.insight.Exceptions.RecordNotFoundException;

import java.util.List;

/**
 * Created by paulograbin on 24/07/15.
 */
public interface ProviderInterface<T> {

    T getDummy();

    ContentValues getContentValues(T object);

    T getFromCursor(Cursor c);

    long insert(T object);

    T getByID(long id) throws RecordNotFoundException;

    List<T> getAll();

    int update(T object);

    int delete(T object);

    int delete(long id);

    int deleteAll();

    int getCount();

    void printToLog(String msg);
}
