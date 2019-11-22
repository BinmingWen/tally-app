package com.it.girlaccount.test;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.it.girlaccount.test.Controller.ReportController;
import com.it.girlaccount.test.Model.Expense;
import com.it.girlaccount.test.R;
import com.it.girlaccount.test.Utils.Utils;

public class DetailReport extends AppCompatActivity {

    ListView detaillist;
    ArrayAdapter<Expense> adapter;
    private ReportController report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_report);
        Intent dataintent = getIntent();
        report = new ReportController(this);
        //backicon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(dataintent.getStringExtra("Kategori"));
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(dataintent.getIntExtra("Color",0)
        ));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarColor(dataintent.getIntExtra("Color",0));
        }

        List<Expense> Expenses = new ArrayList<Expense>();
        detaillist = (ListView) findViewById(R.id.listdetail);
        Expenses = report.getMontlyReportByCategory(
                dataintent.getIntExtra("Idkategori",0),
                dataintent.getStringExtra("Month"),
                dataintent.getIntExtra("Year",0)
        );
        makeList(Expenses);
        detaillist.setAdapter(adapter);

        LinearLayout ln = (LinearLayout) findViewById(R.id.linearLayout);
        ln.setBackgroundColor(dataintent.getIntExtra("Color",0));
        TextView todayout = (TextView) findViewById(R.id.todayOut);
        todayout.setText(Utils.convertCur(String.valueOf(dataintent.getIntExtra("Jumlah",0))));

    }

    private void makeList(List<Expense> expense) {
        adapter = new ArrayAdapter<Expense>(this,
                android.R.layout.simple_dropdown_item_1line, expense) {
            @Override
            public View getView(int position,
                                View convertView,
                                ViewGroup parent) {
                Expense current = getItem(position);
                if (convertView == null) {
                    LayoutInflater vi;
                    vi = LayoutInflater.from(getContext());
                    convertView = vi.inflate(R.layout.item_expense, null, false);
                }
                TextView txtDes = (TextView) convertView.findViewById(R.id.deskrip);
                TextView txtKat = (TextView) convertView.findViewById(R.id.namakat);
                TextView txtJml = (TextView) convertView.findViewById(R.id.jumlah);

                txtDes.setText(current.getDescription());
                txtKat.setText(current.getDate());
                txtJml.setText(Utils.convertCur(String.valueOf(current.getAmount())));
                return convertView;
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColor(int color){
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
