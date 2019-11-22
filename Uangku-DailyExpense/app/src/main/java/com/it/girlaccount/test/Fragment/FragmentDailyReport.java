package com.it.girlaccount.test.Fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.it.girlaccount.test.Controller.CategoryController;
import com.it.girlaccount.test.Controller.ExpenseController;
import com.it.girlaccount.test.Controller.ReportController;
import com.it.girlaccount.test.MainActivity;
import com.it.girlaccount.test.Model.Category;
import com.it.girlaccount.test.Model.Expense;
import com.it.girlaccount.test.R;
import com.it.girlaccount.test.Utils.DatePicker;
import com.it.girlaccount.test.Utils.Utils;



public class FragmentDailyReport extends Fragment {

    public FragmentDailyReport() {
    }

    RelativeLayout view;


    private CategoryController category;
    private ExpenseController expense;
    private ReportController report;

    List<Expense> Expenses = new ArrayList<Expense>();
    List<Expense> ExpensesByCategory;
    ArrayAdapter<Expense> adapter;

    public Button btnTgl;

    ListView dailyReportList;
    MaterialSpinner spinCategory;
    TextView totalOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = (RelativeLayout) inflater.inflate(R.layout.fragment_daily_report, container, false);
        //设置标题
        getActivity().setTitle("每日报告");
        //隐藏右下方的按钮
        ((MainActivity) getActivity()).hideFloatingActionButton();
        category = new CategoryController(getActivity());
        expense = new ExpenseController(getActivity());
        report = new ReportController(getActivity());

        btnTgl = (Button) view.findViewById(R.id.tgl);
        //设置按钮的文本为当前日期
        btnTgl.setText(Utils.getDateNow());
        //设置按钮的点击事件,点击时弹出时间选择
        btnTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        totalOut = (TextView) view.findViewById(R.id.todayOut);
        dailyReportList = (ListView) view.findViewById(R.id.outList);

        new MyAsynch().execute(btnTgl.getText().toString());
        //设置列表子元素点击事件
        dailyReportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        spinCategory = (MaterialSpinner) view.findViewById(R.id.kategori);
        loadCategory();
        return view;
    }


    private class MyAsynch extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... Strings) {
            Expenses = report.getDailyReport(Strings[0]);
            return null;
        }

        protected void onPostExecute(String result) {
            setListAdapter(Expenses);
            changeTodayOut(Expenses);
            dailyReportList.setAdapter(adapter);
            if (adapter.getCount() == 0) {
                dailyReportList.setEmptyView(view.findViewById(R.id.emptyview));
            }
        }
    }



    public void setListAdapter(List<Expense> expenses) {
        adapter = new ArrayAdapter<Expense>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, expenses) {
            @Override
            public View getView(int position,
                                View convertView,
                                ViewGroup parent) {
                Expense current = getItem(position);
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.item_expense, null, false);
                }
                TextView txtDes = (TextView) convertView.findViewById(R.id.deskrip);
                TextView txtKat = (TextView) convertView.findViewById(R.id.namakat);
                TextView txtJml = (TextView) convertView.findViewById(R.id.jumlah);

                txtDes.setText(current.getDescription());
                txtKat.setText(category.getName(current.getIdk()));
                txtJml.setText(Utils.convertCur(String.valueOf(current.getAmount())));
                return convertView;
            }
        };
    }


    public void changeTodayOut(List<Expense> expenses) {
        if (expenses.size() != 0) {
            int total = 0;
            for (Expense pe : expenses) {
                total += pe.getAmount();
            }
            totalOut.setText(" " + Utils.convertCur(String.valueOf(total)));
        } else {
            totalOut.setText(" 无 ");
        }

    }


    public void loadCategory() {
        List<Category> Categories = category.getAllCategory();
        Category All = new Category();
        All.setCategory("所有类别");
        Categories.add(0, All);
        spinCategory.setItems(Categories);
        spinCategory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<Category>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Category item) {
                if (position != 0) {
                    changeListByCategory("K", item.getId());
                } else {
                    changeListByCategory("A", item.getId());

                }
            }

        });
    }


    public void changeListByCategory(String by, int id) {
        if (by.equalsIgnoreCase("K")) {
            ExpensesByCategory = new ArrayList<Expense>();
            for (Expense pe : Expenses) {
                if (pe.getIdk() == id) {
                    ExpensesByCategory.add(pe);
                }
            }
            setListAdapter(ExpensesByCategory);
            changeTodayOut(ExpensesByCategory);

        } else {
            setListAdapter(Expenses);
            changeTodayOut(Expenses);
        }
        dailyReportList.setAdapter(adapter);
        if (adapter.getCount() == 0) {
            dailyReportList.setEmptyView(view.findViewById(R.id.emptyview));
        }
    }


    private void showDatePicker() {
        DatePicker date = new DatePicker();
        //使用默认时区和区域设置获取日历
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(onGetDate);
        date.show(getFragmentManager(), "Date Picker");
    }


    DatePickerDialog.OnDateSetListener onGetDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(c.getTime());
            //设置按钮显示的日期
            btnTgl.setText(formattedDate);
            new MyAsynch().execute(btnTgl.getText().toString());
            //设置下拉菜单选中为第一项
            spinCategory.setSelectedIndex(0);
        }
    };

}