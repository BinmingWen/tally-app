package com.it.girlaccount.test.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.it.girlaccount.test.DBHelper.DBHandler;
import com.it.girlaccount.test.Model.Remain;

public class RemainController {
    public static final String LOGTAG = "REMAIN TABLE";
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    public RemainController(Context context) {
        dbhandler = new DBHandler(context);
    }


    public void open() {
        Log.i(LOGTAG, "Database Opened");
        database = dbhandler.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();
    }


    public void addRemain(Remain remain) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBHandler.KEY_AMOUNT, remain.getRemain());
        database.insert(DBHandler.TABLE_REMAIN, null, values);
        close();
    }

    public void updateRemain(int newRemain) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBHandler.KEY_AMOUNT, newRemain);
        database.update(DBHandler.TABLE_REMAIN, values, DBHandler.KEY_ID + " = ?",
                new String[]{String.valueOf(1)});
        close();
    }


    public String getRemain() {
        database = dbhandler.getReadableDatabase();
        String newRemain = "";
        String selectQuery = "SELECT * FROM " + DBHandler.TABLE_REMAIN;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            newRemain = cursor.getString(1);
        }
        cursor.close();
        close();
        return newRemain;
    }
}
