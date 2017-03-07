package com.sae.raz.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sae.raz.model.BeaconModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DBManager {
    private static DBManager instance;

    private DBOpenHelper mdbhelper;
    private final String SELECT_DB_STR = "select * from ";
    private final String DELETE_DB_STR = "delete from ";
    private final String REPLACE_ROW_STR = " ";

    public DBManager(Context context) {
        this.mdbhelper = new DBOpenHelper(context);
        synchronized (DBOpenHelper.DB_LOCK) {
            try {
                mdbhelper.createDatabase();
                mdbhelper.openDatabase();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        instance = this;
    }

    public static DBManager getInstance() {
        return instance;
    }

    // check exist
    synchronized public boolean isBeaconExist(BeaconModel beaconModel) {
        String macAddress = beaconModel.macAddress;
        String uuid = beaconModel.uuid;
        int major = beaconModel.major;
        int minor = beaconModel.minor;

        synchronized (DBOpenHelper.DB_LOCK) {
            SQLiteDatabase mdb = mdbhelper.getReadableDatabase();

            if (mdb != null) {
                String sql_str = SELECT_DB_STR + DBConstant.TABLE_BEACON + " where "
                        + DBConstant.UUID + " = '" + uuid + "' AND "
                        + DBConstant.MAJOR + " = " + major + " AND "
                        + DBConstant.MINOR + " = " + minor;
                Cursor cursor = mdb.rawQuery(sql_str, null);
                int size = cursor.getCount();
                cursor.close();
                if (size > 0) {
                    return true;
                }
            }

            return false;
        }
    }

    // get beacon type
    synchronized public int getBeaconType(BeaconModel beaconModel) {
        String macAddress = beaconModel.macAddress;
        String uuid = beaconModel.uuid;
        int major = beaconModel.major;
        int minor = beaconModel.minor;

        synchronized (DBOpenHelper.DB_LOCK) {
            SQLiteDatabase mdb = mdbhelper.getReadableDatabase();
            int beaconType = -1;

            if (mdb != null) {
                String sql_str = SELECT_DB_STR + DBConstant.TABLE_BEACON + " where "
                        + DBConstant.UUID + " = '" + uuid + "' AND "
                        + DBConstant.MAJOR + " = " + major + " AND "
                        + DBConstant.MINOR + " = " + minor;
                Cursor cursor = mdb.rawQuery(sql_str, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int columIndex = cursor.getColumnIndex(DBConstant.TYPE);
                    beaconType = cursor.getInt(columIndex);
                }
                cursor.close();
            }
            return beaconType;
        }
    }

    // get x value
    synchronized public int getXposition(BeaconModel beaconModel) {
        String macAddress = beaconModel.macAddress;
        String uuid = beaconModel.uuid;
        int major = beaconModel.major;
        int minor = beaconModel.minor;

        synchronized (DBOpenHelper.DB_LOCK) {
            SQLiteDatabase mdb = mdbhelper.getReadableDatabase();
            int xValue = 0;

            if (mdb != null) {
                String sql_str = SELECT_DB_STR + DBConstant.TABLE_BEACON + " where "
                        + DBConstant.UUID + " = '" + uuid + "' AND "
                        + DBConstant.MAJOR + " = " + major + " AND "
                        + DBConstant.MINOR + " = " + minor;
                Cursor cursor = mdb.rawQuery(sql_str, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int columIndex = cursor.getColumnIndex(DBConstant.XVALUE);
                    xValue = cursor.getInt(columIndex);
                }
                cursor.close();
            }
            return xValue;
        }
    }

    // get y value
    synchronized public int getYposition(BeaconModel beaconModel) {
        String macAddress = beaconModel.macAddress;
        String uuid = beaconModel.uuid;
        int major = beaconModel.major;
        int minor = beaconModel.minor;

        synchronized (DBOpenHelper.DB_LOCK) {
            SQLiteDatabase mdb = mdbhelper.getReadableDatabase();
            int yValue = 0;

            if (mdb != null) {
                String sql_str = SELECT_DB_STR + DBConstant.TABLE_BEACON + " where "
                        + DBConstant.UUID + " = '" + uuid + "' AND "
                        + DBConstant.MAJOR + " = " + major + " AND "
                        + DBConstant.MINOR + " = " + minor;
                Cursor cursor = mdb.rawQuery(sql_str, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int columIndex = cursor.getColumnIndex(DBConstant.YVALUE);
                    yValue = cursor.getInt(columIndex);
                }
                cursor.close();
            }
            return yValue;
        }
    }

    // get beacon number
    synchronized public int getBeaconNumber(BeaconModel beaconModel) {
        String macAddress = beaconModel.macAddress;
        String uuid = beaconModel.uuid;
        int major = beaconModel.major;
        int minor = beaconModel.minor;

        synchronized (DBOpenHelper.DB_LOCK) {
            SQLiteDatabase mdb = mdbhelper.getReadableDatabase();
            int beaconNumber = -1;

            if (mdb != null) {
                String sql_str = SELECT_DB_STR + DBConstant.TABLE_BEACON + " where "
                        + DBConstant.UUID + " = '" + uuid + "' AND "
                        + DBConstant.MAJOR + " = " + major + " AND "
                        + DBConstant.MINOR + " = " + minor;
                Cursor cursor = mdb.rawQuery(sql_str, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int columIndex = cursor.getColumnIndex(DBConstant.NUMBER);
                    beaconNumber = cursor.getInt(columIndex);
                }
                cursor.close();
            }
            return beaconNumber;
        }
    }

    // get beacon notification title
    synchronized public String getBeaconTitle(BeaconModel beaconModel) {
        String macAddress = beaconModel.macAddress;
        String uuid = beaconModel.uuid;
        int major = beaconModel.major;
        int minor = beaconModel.minor;

        synchronized (DBOpenHelper.DB_LOCK) {
            SQLiteDatabase mdb = mdbhelper.getReadableDatabase();
            String notificationTitle = "";

            if (mdb != null) {
                String sql_str = SELECT_DB_STR + DBConstant.TABLE_BEACON + " where "
                        + DBConstant.UUID + " = '" + uuid + "' AND "
                        + DBConstant.MAJOR + " = " + major + " AND "
                        + DBConstant.MINOR + " = " + minor;
                Cursor cursor = mdb.rawQuery(sql_str, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int columIndex = -1;

                    columIndex = cursor.getColumnIndex(DBConstant.TITLE);
                    notificationTitle = cursor.getString(columIndex);

                }
                cursor.close();
            }
            return notificationTitle;
        }
    }

    // get web url
    synchronized public String getWebUrl(BeaconModel beaconModel) {
        String macAddress = beaconModel.macAddress;
        String uuid = beaconModel.uuid;
        int major = beaconModel.major;
        int minor = beaconModel.minor;

        synchronized (DBOpenHelper.DB_LOCK) {
            SQLiteDatabase mdb = mdbhelper.getReadableDatabase();
            String webUrl = "";

            if (mdb != null) {
                String sql_str = SELECT_DB_STR + DBConstant.TABLE_BEACON + " where "
                        + DBConstant.UUID + " = '" + uuid + "' AND "
                        + DBConstant.MAJOR + " = " + major + " AND "
                        + DBConstant.MINOR + " = " + minor;
                Cursor cursor = mdb.rawQuery(sql_str, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int columIndex = -1;

                    columIndex = cursor.getColumnIndex(DBConstant.WEBURL);
                    webUrl = cursor.getString(columIndex);

                }
                cursor.close();
            }
            return webUrl;
        }
    }

    // get last time
    synchronized public long getLastTime(BeaconModel beaconModel) {
        String macAddress = beaconModel.macAddress;
        String uuid = beaconModel.uuid;
        int major = beaconModel.major;
        int minor = beaconModel.minor;

        synchronized (DBOpenHelper.DB_LOCK) {
            SQLiteDatabase mdb = mdbhelper.getReadableDatabase();
            long lastTime = 0;

            if (mdb != null) {
                String sql_str = SELECT_DB_STR + DBConstant.TABLE_BEACON + " where "
                        + DBConstant.UUID + " = '" + uuid + "' AND "
                        + DBConstant.MAJOR + " = " + major + " AND "
                        + DBConstant.MINOR + " = " + minor;
                Cursor cursor = mdb.rawQuery(sql_str, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int columIndex = cursor.getColumnIndex(DBConstant.LASTTIME);
                    lastTime = Long.parseLong(cursor.getString(columIndex));
                }
                cursor.close();
            }
            return lastTime;
        }
    }

    // Update Last Time
    synchronized public boolean updateLastTime(BeaconModel beaconModel) {
        String macAddress = beaconModel.macAddress;
        String uuid = beaconModel.uuid;
        int major = beaconModel.major;
        int minor = beaconModel.minor;

        String curTime = String.valueOf(Calendar.getInstance().getTimeInMillis());

        synchronized (DBOpenHelper.DB_LOCK) {
            SQLiteDatabase mdb = mdbhelper.getReadableDatabase();
            long lastTime = 0;

            if (mdb != null) {
                String sql_str = "Update " + DBConstant.TABLE_BEACON
                        + " SET " + DBConstant.LASTTIME + " = '" + curTime + "' WHERE "
                        + DBConstant.UUID + " = '" + uuid + "' AND "
                        + DBConstant.MAJOR + " = " + major + " AND "
                        + DBConstant.MINOR + " = " + minor;
                Cursor cursor = mdb.rawQuery(sql_str, null);
                int size = cursor.getCount();
                cursor.close();
                if (size > 0) {
                    return true;
                }
            }
            return false;
        }
    }

    synchronized public List<BeaconModel> getAllBeacons() {
        synchronized (DBOpenHelper.DB_LOCK) {
            SQLiteDatabase mdb = mdbhelper.getReadableDatabase();
            ArrayList<BeaconModel> modelList = new ArrayList<BeaconModel>();

            if (mdb != null) {
                String sql_str = SELECT_DB_STR + DBConstant.TABLE_BEACON;

                Cursor cursor = mdb.rawQuery(sql_str, null);
                if (cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        while (!cursor.isAfterLast()) {
                            BeaconModel model = new BeaconModel();

                            int columIndex = cursor.getColumnIndex(DBConstant.NUMBER);
                            model.beaconNumber = cursor.getInt(columIndex);

                            columIndex = cursor.getColumnIndex(DBConstant.TYPE);
                            model.equType = cursor.getInt(columIndex);

                            columIndex = cursor.getColumnIndex(DBConstant.TITLE);
                            model.beaconTitle = cursor.getString(columIndex);

                            columIndex = cursor.getColumnIndex(DBConstant.WEBURL);
                            model.webUrl = cursor.getString(columIndex);

                            columIndex = cursor.getColumnIndex(DBConstant.LASTTIME);
                            model.lastTime = Long.parseLong(cursor.getString(columIndex));

                            columIndex = cursor.getColumnIndex(DBConstant.XVALUE);
                            model.xPosition = cursor.getInt(columIndex);

                            columIndex = cursor.getColumnIndex(DBConstant.YVALUE);
                            model.yPosition = cursor.getInt(columIndex);

                            modelList.add(model);
                            cursor.moveToNext();
                        }
                    }
                }
            }
            return modelList;
        }
    }
}
