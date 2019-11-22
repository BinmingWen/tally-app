package com.it.girlaccount.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.it.girlaccount.test.Controller.RemainController;
import com.it.girlaccount.test.Model.Remain;
import com.it.girlaccount.test.R;
import com.it.girlaccount.test.Utils.SPManager;
import com.it.girlaccount.test.Utils.Utils;

public class SettingUp extends Activity {
    private ViewPager viewpager;
    private List<View> dataList;
    private AdaterViewPager adp;

    private RemainController remain;
    EditText remainTxt;
    SPManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        remain = new RemainController(this);
        prefManager = new SPManager(this);

        if(prefManager.isFirstTimeLaunch()){
            remain.addRemain(new Remain(1, 10000));
        }
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        remainTxt = (EditText) this.findViewById(R.id.setMoney);
        setContentView(R.layout.activity_welcome);
        viewpager= (ViewPager) findViewById(R.id.vp);
        initData();
        adp = new AdaterViewPager(dataList);
        viewpager.setAdapter(adp);

    }

    private void initData() {
        dataList =new ArrayList<>();
        View view1 = LayoutInflater.from(this).inflate(R.layout.activity_welcome_2,null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.activity_welcome_3,null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.activity_setting_up,null);
        dataList.add(view1);
        dataList.add(view2);
        dataList.add(view3);
    }

    public void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        Intent i = new Intent(SettingUp.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void restoreData(View view) {
//        Toast.makeText(this, Utils.doRestore(), Toast.LENGTH_LONG).show();
        launchHomeScreen();
    }

    public void firsttimeSetting(View view) {
        remainTxt = (EditText) this.findViewById(R.id.setMoney);
        if(remainTxt.length()==0) {
            Toast.makeText(this, "输入框的值不能为空", Toast.LENGTH_SHORT).show();
        } else {
            try {
                remain.updateRemain(Integer.parseInt(remainTxt.getText().toString()));
                Log.d("Saldo Baru", remain.getRemain());
                AlertDialog alertDialog = new AlertDialog.Builder(SettingUp.this).create();
                alertDialog.setTitle("成功");
                alertDialog.setMessage(getResources().getString(R.string.settingupmessagesucces));
                alertDialog.setIcon(R.drawable.ic_info_black_24dp);
                alertDialog.setButton("继续", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        launchHomeScreen();
                    }
                });
                alertDialog.show();

            } catch (SQLException e) {
                Log.d("Error", "SqlExceptionError");
            }
        }
    }

    public class AdaterViewPager extends PagerAdapter {
        private List<View> mViewList;

        public AdaterViewPager(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mViewList.get(position));
        }
    }

}
