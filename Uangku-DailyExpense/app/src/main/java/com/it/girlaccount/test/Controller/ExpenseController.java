package com.it.girlaccount.test.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.it.girlaccount.test.DBHelper.DBHandler;
import com.it.girlaccount.test.Model.Expense;



public class ExpenseController {

    public static final String LOGTAG = "EXPENSE TABLE";
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    public ExpenseController(Context context) {
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



    public void addExpense(Expense expense) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBHandler.KEY_IDK, expense.getIdk());
        values.put(DBHandler.KEY_DESCRIPTION, expense.getDescription());
        values.put(DBHandler.KEY_AMOUNT, expense.getAmount());
        values.put(DBHandler.KEY_DATE, expense.getDate());
        database.insert(DBHandler.TABLE_EXPENSE, null, values);
        close();
    }

    public void updateExpense(Expense expense) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBHandler.KEY_IDK, expense.getIdk());
        values.put(DBHandler.KEY_DESCRIPTION, expense.getDescription());
        values.put(DBHandler.KEY_AMOUNT,expense.getAmount());
        values.put(DBHandler.KEY_DATE, expense.getDate());
        database.update(DBHandler.TABLE_EXPENSE, values, DBHandler.KEY_ID + " = ?",
                new String[]{String.valueOf(expense.getId())});
        close();
    }

    public void deleteExpenseById(int id) {
        open();
        database.delete(DBHandler.TABLE_EXPENSE, DBHandler.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        close();
    }

    public void deleteExpenseByCategory(int idk) {
        open();
        database.delete(DBHandler.TABLE_EXPENSE, DBHandler.KEY_IDK + " = ?",
                new String[]{String.valueOf(idk)});
        close();
    }



    public List<Expense> getExpenseByDate(String date) {
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



    public Expense getExpense(int id) {
        database = dbhandler.getReadableDatabase();

        Cursor cursor = database.query(DBHandler.TABLE_EXPENSE, new String[]{DBHandler.KEY_ID,
                        DBHandler.KEY_IDK, DBHandler.KEY_DESCRIPTION,
                        DBHandler.KEY_AMOUNT, DBHandler.KEY_DATE}, DBHandler.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Expense expense = new Expense(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)), cursor.getString(2),
                Integer.parseInt(cursor.getString(3)), cursor.getString(4));
        cursor.close();
        close();
        return expense;
    }

    public String getTotal(String date) {
        database = dbhandler.getReadableDatabase();
        String total = "0";
        Cursor cursor = database.rawQuery("SELECT sum(" + DBHandler.KEY_AMOUNT + ") " +
                "FROM " + DBHandler.TABLE_EXPENSE + " where " + DBHandler.KEY_DATE
                + " = '" + date + "'", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            total = cursor.getString(0);
        }
        cursor.close();
        close();
        return total;
    }


    public int getCountByDate(String date) {
        String countQuery = "SELECT  * FROM " + DBHandler.TABLE_EXPENSE +
                " where " + DBHandler.KEY_DATE + " = '" + date + "'";
        database = dbhandler.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int a = cursor.getCount();
        cursor.close();
        return a;
    }

    public boolean isCategoryExist(int idk) {
        String countQuery = "SELECT  * FROM " + DBHandler.TABLE_EXPENSE +
                " where " + DBHandler.KEY_IDK + " = " + idk;
        database = dbhandler.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int a = cursor.getCount();
        cursor.close();
        if (a>0){
            return true;
        }
        return false;
    }

}