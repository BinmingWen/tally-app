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
import com.it.girlaccount.test.Model.Category;



public class CategoryController {

    private ExpenseController expense;
    public static final String LOGTAG = "EXPENSE TABLE";
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    public CategoryController(Context context) {
        expense = new ExpenseController(context);
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



    public void addCategory(Category cat) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBHandler.KEY_DESCRIPTION, cat.getCategory());
        database.insert(DBHandler.TABLE_CATEGORY, null, values);
        close();
    }

    public void updateCategory(Category cat) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBHandler.KEY_DESCRIPTION, cat.getCategory());
        database.update(DBHandler.TABLE_CATEGORY, values, DBHandler.KEY_ID + " = ?",
                new String[]{String.valueOf(cat.getId())});
        close();
    }

    public void deleteCategory(int id, boolean exist) {
        open();
        if (exist){
            Log.d(LOGTAG, "Delete Exist");
            expense.deleteExpenseByCategory(id);
        }
        database.delete(DBHandler.TABLE_CATEGORY, DBHandler.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        close();
    }




    public List<Category> getAllCategory() {
        List<Category> categoryList = new ArrayList();
        String selectQuery = "SELECT  * FROM " + DBHandler.TABLE_CATEGORY;
        open();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(Integer.parseInt(cursor.getString(0)));
                category.setCategory(cursor.getString(1));
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        close();
        return categoryList;
    }


    /**
     * Return Name From IDKategori, Return Data Count
     */
    public String getName(int id) {
        database = dbhandler.getReadableDatabase();
        Cursor cursor = database.query(DBHandler.TABLE_CATEGORY, new String[]{DBHandler.KEY_ID,
                        DBHandler.KEY_DESCRIPTION}, DBHandler.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        String name = cursor.getString(1);
        cursor.close();
        close();
        return name;
    }

    public int getCount() {
        String countQuery = "SELECT  * FROM " + DBHandler.TABLE_CATEGORY;
        database = dbhandler.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int a = cursor.getCount();
        cursor.close();
        close();
        return a;
    }
    public boolean isNameExist(String name) {

        database = dbhandler.getReadableDatabase();
        Cursor cursor = database.query(DBHandler.TABLE_CATEGORY, new String[]{DBHandler.KEY_ID,
                        DBHandler.KEY_DESCRIPTION}, DBHandler.KEY_DESCRIPTION + "=?",
                new String[]{name }, null, null, null, null);
        int a = cursor.getCount();
        cursor.close();
        close();
        if(a>0){
            return true;
        }else {
            return false;
        }
    }

}