package com.it.girlaccount.test.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.it.girlaccount.test.DBHelper.DBHandler;
import com.it.girlaccount.test.Model.Expense;
import com.it.girlaccount.test.Model.Report;

import java.util.ArrayList;
import java.util.List;

public class ReportController {

    public static final String LOGTAG = "REPORT TABLE";
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    public ReportController(Context context) {
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


    public List<Expense> getDailyReport(String date) {
        List<Expense> expenseList = new ArrayList();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBHandler.TABLE_EXPENSE
                + " where " + DBHandler.KEY_DATE + " = '" + date + "' order by " + DBHandler.KEY_ID + " desc";
        open();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(Integer.parseInt(cursor.getString(0)));
                expense.setIdk(Integer.parseInt(cursor.getString(1)));
                expense.setDescription(cursor.getString(2));
                expense.setAmount(Integer.parseInt(cursor.getString(3)));
                expense.setDate(cursor.getString(4));
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }
        close();
        return expenseList;
    }


    public List<Report> getMontlyReport(String month, String year) {
        List<Report> reportList = new ArrayList();
        String selectQuery = "SELECT " +
                DBHandler.TABLE_EXPENSE + "." + DBHandler.KEY_IDK +
                ", count(" + DBHandler.KEY_IDK + ")" + ", sum(" + DBHandler.KEY_AMOUNT + "), " +
                DBHandler.TABLE_CATEGORY + "." + DBHandler.KEY_DESCRIPTION +
                " FROM " + DBHandler.TABLE_EXPENSE +
                " INNER JOIN " + DBHandler.TABLE_CATEGORY + " ON " +
                DBHandler.TABLE_EXPENSE + "." + DBHandler.KEY_IDK + " = " + DBHandler.TABLE_CATEGORY + "." + DBHandler.KEY_ID +
                " where strftime('%m', date) = '" + month + "' and strftime('%Y', date) = '" + year +
                "' group by " + DBHandler.KEY_IDK;
        Log.d("Query Laporan", selectQuery);
        open();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Report report = new Report();
                report.setIdk(Integer.parseInt(cursor.getString(0)));
                report.setTotal(Integer.parseInt(cursor.getString(1)));
                report.setAmount(Integer.parseInt(cursor.getString(2)));
                report.setNameCategory(cursor.getString(3));
                reportList.add(report);
            } while (cursor.moveToNext());
        }
        close();
        return reportList;
    }


    public List<Expense> getMontlyReportByCategory(int idk, String month, int year) {
        List<Expense> expenseList = new ArrayList();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBHandler.TABLE_EXPENSE
                + " where strftime('%m', date) = '" + month + "' and strftime('%Y', date) = '" + year +
                "' and " + DBHandler.KEY_IDK + " = " + idk + " order by " + DBHandler.KEY_DATE + " asc";
        open();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(Integer.parseInt(cursor.getString(0)));
                expense.setIdk(Integer.parseInt(cursor.getString(1)));
                expense.setDescription(cursor.getString(2));
                expense.setAmount(Integer.parseInt(cursor.getString(3)));
                expense.setDate(cursor.getString(4));
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }
        close();
        return expenseList;
    }
}

