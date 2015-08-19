package com.paulograbin.insight.DB.Table;

import android.provider.BaseColumns;

/**
 * Created by paulograbin on 13/08/15.
 */
public abstract class TablePath implements BaseColumns, DBUtils {

    public static final String TABLE_NAME = "path";

    public static String COLUMN_ID = BaseColumns._ID;
    public static String COLUMN_PLACE = "place";
    public static String COLUMN_CONNECTED_TO = "connectedTo";
    public static String COLUMN_WEIGHT = "weight";

    public static final String TABLE_CREATE_COMMAND =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_ID + TYPE_INTEGER + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEPARATOR +
                    COLUMN_PLACE + TYPE_INTEGER + NOT_NULL + COMMA_SEPARATOR +
                    COLUMN_CONNECTED_TO + TYPE_INTEGER + NOT_NULL + COMMA_SEPARATOR +
                    COLUMN_WEIGHT + TYPE_INTEGER + NOT_NULL + ")";
}
