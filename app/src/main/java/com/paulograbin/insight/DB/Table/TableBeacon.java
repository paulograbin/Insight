package com.paulograbin.insight.DB.Table;

import android.provider.BaseColumns;

/**
 * Created by paulograbin on 01/07/15.
 */
public abstract class TableBeacon implements BaseColumns, DBUtils {

    public static String TABLE_NAME = "beacon";

    public static String COLUMN_ID = _ID;
    public static String COLUMN_UUID = "uuid";
    public static String COLUMN_NAME = "name";
    public static String COLUMN_NETWORKTYPE = "networkType";
    public static String COLUMN_MAJOR = "major";
    public static String COLUMN_MINOR = "minor";
    public static String COLUMN_CHANNEL = "chanel";
    public static String COLUMN_LOCATION = "location";
    public static String COLUMN_LATITUDE = "latitude";
    public static String COLUMN_LONGITUDE = "longitude";
    public static String COLUMN_MESSAGE = "message";
    public static String COLUMN_CREATED_DATE = "createdDate";
    public static String COLUMN_CREATED_TIME = "createdTime";

    public static final String TABLE_CREATE_COMMAND =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_ID + TYPE_INTEGER + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEPARATOR +
                    COLUMN_UUID + TYPE_TEXT + COMMA_SEPARATOR +
                    COLUMN_NAME + TYPE_TEXT + COMMA_SEPARATOR +
                    COLUMN_NETWORKTYPE + TYPE_TEXT + COMMA_SEPARATOR +
                    COLUMN_MAJOR + TYPE_TEXT + COMMA_SEPARATOR +
                    COLUMN_MINOR + TYPE_TEXT + COMMA_SEPARATOR +
                    COLUMN_CHANNEL + TYPE_TEXT + COMMA_SEPARATOR +
                    COLUMN_LOCATION + TYPE_TEXT + COMMA_SEPARATOR +
                    COLUMN_LATITUDE + TYPE_DOUBLE + COMMA_SEPARATOR +
                    COLUMN_LONGITUDE + TYPE_DOUBLE + COMMA_SEPARATOR +
                    COLUMN_MESSAGE + TYPE_TEXT + COMMA_SEPARATOR +
                    COLUMN_CREATED_DATE + TYPE_DATETIME + COMMA_SEPARATOR +
                    COLUMN_CREATED_TIME + TYPE_DATETIME +
                    ")";

}
