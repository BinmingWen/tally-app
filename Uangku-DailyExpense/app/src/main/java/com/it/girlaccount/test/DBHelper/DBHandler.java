package com.it.girlaccount.test.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHandler extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "money";

    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CATEGORY = "category";    //分类

    public static final String TABLE_REMAIN = "remain";        //余额

    public static final String TABLE_EXPENSE = "expense";    //支出

    public static final String KEY_ID = "id";

    public static final String KEY_IDK = "idk";

    public static final String KEY_DESCRIPTION = "description";    //描述

    public static final String KEY_AMOUNT = "amount";          //数量

    public static final String KEY_DATE = "date";        //日期

    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE "
            + TABLE_CATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DESCRIPTION
            + " TEXT" + ")";

    private static final String CREATE_TABLE_REMAIN = "CREATE TABLE "
            + TABLE_REMAIN + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_AMOUNT
            + " INTEGER" + ")";

    private static final String CREATE_TABLE_EXPENSE = "CREATE TABLE "
            + TABLE_EXPENSE + "(" + KEY_ID + " INTEGER PRIMARY KEY ," + KEY_IDK
            + " INTEGER," + KEY_DESCRIPTION + " TEXT," + KEY_AMOUNT + " INTEGER," + KEY_DATE + " DEFAULT CURRENT_DATE" + ")";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_EXPENSE);
        db.execSQL(CREATE_TABLE_REMAIN);
        db.execSQL("INSERT into 'category' ('description') VALUES ('饮食') ");
        db.execSQL("INSERT into 'category' ('description') VALUES ('出行') ");
        db.execSQL("INSERT into 'category' ('description') VALUES ('水电') ");
        db.execSQL("INSERT into 'category' ('description') VALUES ('住房') ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMAIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);

        onCreate(db);
    }
}
