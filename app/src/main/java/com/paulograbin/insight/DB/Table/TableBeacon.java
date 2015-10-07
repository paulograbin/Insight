package com.paulograbin.insight.DB.Table;

import android.provider.BaseColumns;

/**
 * Created by paulograbin on 01/07/15.
 */
public abstract class TableBeacon implements BaseColumns, DBUtils {

    public static String TABLE_NAME = "beacon";

    public static String COLUMN_ID = _ID;
    public static String COLUMN_UUID = "uuid";
    public static String COLUMN_LOCATION = "location";
    public static String COLUMN_LATITUDE = "latitude";
    public static String COLUMN_LONGITUDE = "longitude";

    public static final String TABLE_CREATE_COMMAND =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_ID + TYPE_INTEGER + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEPARATOR +
                    COLUMN_UUID + TYPE_TEXT + COMMA_SEPARATOR +
                    COLUMN_LOCATION + TYPE_TEXT + COMMA_SEPARATOR +
                    COLUMN_LATITUDE + TYPE_DOUBLE + COMMA_SEPARATOR +
                    COLUMN_LONGITUDE + TYPE_DOUBLE + ")";

}
