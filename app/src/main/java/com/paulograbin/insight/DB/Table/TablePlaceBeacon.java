package com.paulograbin.insight.DB.Table;

import android.provider.BaseColumns;

/**
 * Created by paulograbin on 08/08/15.
 */
public abstract class TablePlaceBeacon implements BaseColumns, DBUtils {


    //TODO: Create constraint to make combination of idPlace and idBeacon unique

    public static String TABLE_NAME = "placeBeacon";

    public static String COLUMN_ID = BaseColumns._ID;
    public static String COLUMN_IDPLACE = "idPlace";
    public static String COLUMN_IDBEACON = "idBeacon";
    public static String COLUMN_UUID = "uuid";
    public static String COLUMN_CREATED_DATE = "createdDate";
    public static String COLUMN_CREATED_TIME = "createdTime";

    public static final String TABLE_CREATE_COMMAND =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_ID + TYPE_INTEGER + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEPARATOR +
                    COLUMN_IDPLACE + TYPE_INTEGER + NOT_NULL + COMMA_SEPARATOR +
                    COLUMN_IDBEACON + TYPE_INTEGER + NOT_NULL + COMMA_SEPARATOR +
                    COLUMN_UUID + TYPE_INTEGER + NOT_NULL + COMMA_SEPARATOR +
                    COLUMN_CREATED_DATE + TYPE_TEXT + COMMA_SEPARATOR +
                    COLUMN_CREATED_TIME + TYPE_TEXT + COMMA_SEPARATOR +
                    " FOREIGN KEY(" + COLUMN_IDPLACE + ") REFERENCES " + TablePlace.TABLE_NAME + "(" + TablePlace.COLUMN_ID + ")" +
                    " FOREIGN KEY(" + COLUMN_IDBEACON + ") REFERENCES " + TableBeacon.TABLE_NAME + "(" + TableBeacon.COLUMN_ID + ")" +
                    ")";

}
