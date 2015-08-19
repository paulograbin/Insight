package com.paulograbin.insight.DB.Table;

import android.provider.BaseColumns;

/**
 * Created by paulograbin on 11/07/15.
 */
public abstract class TableMessage implements BaseColumns, DBUtils {

    public static String TABLE_NAME = "message";

    public static String COLUMN_ID = BaseColumns._ID;
    public static String COLUMN_TEXT = "text";

    public static final String TABLE_CREATE_COMMAND =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_ID + TYPE_INTEGER + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEPARATOR +
                    COLUMN_TEXT + TYPE_TEXT + NOT_NULL +
                    ")";

}
