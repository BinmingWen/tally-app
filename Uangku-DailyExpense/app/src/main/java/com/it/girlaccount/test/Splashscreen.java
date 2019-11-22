package com.it.girlaccount.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.it.girlaccount.test.Controller.RemainController;
import com.it.girlaccount.test.Utils.SPManager;

public class Splashscreen extends AppCompatActivity {
    private RemainController remain;
    EditText remainTxt;
    SPManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return;
        }
//        prefManager = new SPManager(this);
//        if(prefManager.isFirstTimeLaunch()) {
//            Intent intent = new Intent(this, welcome_1.class);
//            startActivity(intent);
//            finish();
//        } else {
//            Intent i = new Intent(this, MainActivity.class);
//            startActivity(i);
//            finish();
//        }
        Intent i = new Intent(this, SettingUp.class);
        startActivity(i);
        finish();
    }
}
