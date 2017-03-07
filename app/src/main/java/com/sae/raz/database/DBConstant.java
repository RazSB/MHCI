package com.sae.raz.database;

public class DBConstant {
    /*
     * Common
     */
    public static final String TBL_COLUMN_ID = "id";
    public static String SPLIT_STRING = "#";

    /* Related DB */
    public static final String DATABASE_NAME = "BeaconDatabase";
    public static final int DATABASE_VERSION = 1;


    public static final String CREATE_DB_SQL_PREFIX = "CREATE TABLE IF NOT EXISTS ";
    public static final String DELETE_DB_SQL_PREFIX = "DROP TABLE IF EXISTS ";

    /*
     *  DB Tables Name
     */
    public static final String TABLE_BEACON = "beaconList";

    public static final String NUMBER = "Number";
    public static final String MAC_ADDRESS = "MAC";
    public static final String UUID = "UUID";
    public static final String MAJOR = "Major";
    public static final String MINOR = "Minor";
    public static final String TYPE = "Type";
    public static final String XVALUE = "X";
    public static final String YVALUE = "Y";
    public static final String TITLE = "Title";
    public static final String WEBURL = "WebURL";
    public static final String LASTTIME = "LastTime";
}
