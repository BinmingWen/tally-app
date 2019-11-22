package com.it.girlaccount.test.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;


public class Utils {

    private static final int REQUEST_PERMISSION_CODE = 0;


    public static String convertCur(String param) {
        Double money = Double.parseDouble(param);
        DecimalFormat df = new DecimalFormat();
        //设置国家货币符号
        df.setCurrency(Currency.getInstance("CNY"));
        //设置最多保留2位
        df.setMaximumFractionDigits(2);
        //设置分组大小
        df.setGroupingSize(3);
        //设置后缀
//        df.setNegativePrefix("￥");
//        df.setPositivePrefix("￥");
        df.setPositiveSuffix("￥");
        df.setNegativeSuffix("￥");
        return df.format(money);

    }


    public static String getDateNow() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }



    public static String doRestore(Context context) {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION_CODE);

        } else {
            try {
                File sd = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();

                if (sd.canWrite()) {
                    String currentDBPath = "/data/data/com.it.girlaccount.test/databases/money";
                    String backupDBPath = "test";
                    File currentDB = new File(currentDBPath);
                    File backupDB = new File(sd, backupDBPath);
                    if (backupDB.exists()){
                        if (currentDB.exists()) {
                            Log.d("Restoring", "UangkuDB");
                            FileChannel src = new FileInputStream(backupDB).getChannel();
                            FileChannel dst = new FileOutputStream(currentDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                            return "数据导入成功！";
                        }else{
                            return "数据不存在！";
                        }
                    }else{
                        return "没有数据库备份！";
                    }
                }
            } catch (Exception e) {
                return "没有数据库！";
            }
            return "成功！";
        }
        return "请授予权限！";


    }


    public static String doBackup(Context context) {

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION_CODE);

        }
        else {
            try {
                File sd = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();
                if (sd.canWrite()) {
                    String currentDBPath = "/data/data/com.it.girlaccount.test/databases/money";
                    String backupDBPath = "test";
                    File currentDB = new File(currentDBPath);
                    File backupDB = new File(sd, backupDBPath);

                    if (currentDB.exists()) {
                        Log.d("Backuping", "UangkuDB");
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        return "数据库备份成功！";
                    }
                    else {
                        Log.d("asd", "doBackup: 111");
                    }
                }
            } catch (Exception e) {
                Log.e("errror11", "doBackup() returned: " + e);
            }
            return null;
        }
        return "请授予权限!";
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}