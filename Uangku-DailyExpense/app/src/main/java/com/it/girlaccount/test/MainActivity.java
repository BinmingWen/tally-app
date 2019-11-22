package com.it.girlaccount.test;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.it.girlaccount.test.Controller.RemainController;
import com.it.girlaccount.test.Fragment.FragmentHome;
import com.it.girlaccount.test.Fragment.FragmentCategory;
import com.it.girlaccount.test.Fragment.FragmentMonthlyReport;
import com.it.girlaccount.test.Fragment.FragmentDailyReport;
import com.it.girlaccount.test.R;
import com.it.girlaccount.test.Utils.SPManager;
import com.it.girlaccount.test.Utils.Utils;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment fragment = null;
    SPManager prefManager;
    FloatingActionButton fabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefManager = new SPManager(this);

        fabBtn = (FloatingActionButton) findViewById(R.id.fab);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getFragmentManager();

        if (savedInstanceState == null) {
            fragment = new FragmentHome();
            callFragment(fragment);
        }
    }


    public void showFloatingActionButton() {
        fabBtn.show();
    };

    public void hideFloatingActionButton() {
        fabBtn.hide();
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showRemainDialog();
            return true;
        }
        if (id == R.id.action_reset) {
            prefManager.setFirstTimeLaunch(true);
            getApplicationContext().deleteDatabase("money");
            finish();
            return true;
        }
        if (id == R.id.share_action) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = getResources().getString(R.string.sharebody);
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.sharesubject) );
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.sharedialog)));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showRemainDialog() {
        final RemainController remainController = new RemainController(this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_saldo, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        final EditText newRemain = (EditText) mView.findViewById(R.id.userInputDialog);
        newRemain.setText(remainController.getRemain());
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        if(newRemain.getText().toString().length()==0){
                            Toast.makeText(MainActivity.this, "输入框的值不能为空", Toast.LENGTH_SHORT).show();
                        }else {
                            remainController.updateRemain(Integer.parseInt(newRemain.getText().toString()));
                            callFragment(new FragmentHome());
                        }
                    }
                })

                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment = new FragmentHome();
            callFragment(fragment);
        } else if (id == R.id.nav_kategori) {
            fragment = new FragmentCategory();
            callFragment(fragment);
        } else if (id == R.id.nav_harian) {
            fragment = new FragmentDailyReport();
            callFragment(fragment);
        } else if (id == R.id.nav_bulanan) {
            fragment = new FragmentMonthlyReport();
            callFragment(fragment);
        } else if (id == R.id.nav_bacres) {
            backupRestoredialog();
        } else if (id == R.id.nav_info) {
            showAboutDialog();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showAboutDialog() {
            View mView = getLayoutInflater().inflate(R.layout.dialog_about, null, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle(R.string.app_name);
            builder.setView(mView);
            builder.create();
            builder.show();
    }


    private void backupRestoredialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("备份&&导入");
        builder.setItems(R.array.dialog_backup, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(MainActivity.this, Utils.doBackup(MainActivity.this), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        callFragment(new FragmentHome());
                        Toast.makeText(MainActivity.this, Utils.doRestore(MainActivity.this), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        builder.show();
    }


    private void callFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.remove(fragment);
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

}
