package com.paulograbin.insight.DB.Table;

import android.provider.BaseColumns;

/**
 * Created by paulograbin on 20/07/15.
 */
public abstract class TablePlace implements BaseColumns, DBUtils {

    public static String TABLE_NAME = "place";

    public static String COLUMN_ID = BaseColumns._ID;
    public static String COLUMN_NAME = "name";
    public static String COLUMN_DESTINATION = "destination";
    public static String COLUMN_LATITUDE = "latitude";
    public static String COLUMN_LONGITUDE = "longitude";

    public static final String TABLE_CREATE_COMMAND =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_ID + TYPE_INTEGER + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEPARATOR +
                    COLUMN_NAME + TYPE_TEXT + COMMA_SEPARATOR +
                    COLUMN_DESTINATION + TYPE_TEXT + COMMA_SEPARATOR +
                    COLUMN_LATITUDE + TYPE_TEXT + COMMA_SEPARATOR +
                    COLUMN_LONGITUDE + TYPE_DOUBLE + ")";

}
