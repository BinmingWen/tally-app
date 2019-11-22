package com.it.girlaccount.test.Fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.it.girlaccount.test.Controller.ReportController;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.it.girlaccount.test.Controller.ExpenseController;
import com.it.girlaccount.test.DetailReport;
import com.it.girlaccount.test.MainActivity;
import com.it.girlaccount.test.Model.Report;
import com.it.girlaccount.test.R;
import com.it.girlaccount.test.Utils.Utils;



public class FragmentMonthlyReport extends Fragment {

    private static final int REQUEST_PERMISSION_CODE = 0 ;

    public FragmentMonthlyReport() {
    }

    RelativeLayout view;


    MaterialSpinner monthSpinner;
    LinearLayout contentLayout;
    ImageButton save;
    PieChart mChart;


    List<Report> Reports = new ArrayList<Report>();
    ListView detaillist;

    List<PieEntry> chartValue;
    List<Integer> chartColor;
    ArrayAdapter<Report> adapter;
    String[] arrayColor;


    int nowYear;
    String newMonth;

    private ExpenseController expense;
    private ReportController report;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = (RelativeLayout) inflater.inflate(R.layout.fragment_monthly_report, container, false);
        getActivity().setTitle("每月报告");
        ((MainActivity) getActivity()).hideFloatingActionButton();
        expense = new ExpenseController(getActivity());
        report = new ReportController(getActivity());

        contentLayout = (LinearLayout) view.findViewById(R.id.contentlayout);
        detaillist = (ListView) view.findViewById(R.id.outList);
        arrayColor = getResources().getStringArray(R.array.mdcolor_500);
        mChart = (PieChart) view.findViewById(R.id.chart1);
        settingUpchart();

        save = (ImageButton) view.findViewById(R.id.savebtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSaveChart();
            }
        });

        monthSpinner = (MaterialSpinner) view.findViewById(R.id.bulantahun);
        monthSpinner.setItems(dataSpinner());
        monthSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (position != 0) {
                    newMonth = String.format("%02d", position);
                    setDataForPieChart(newMonth);
                    setDataForList();
                } else {
                    contentLayout.setVisibility(View.GONE);
                }
            }
        });

        detaillist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Report p = (Report) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), DetailReport.class);
                intent.putExtra("Idkategori", p.getIdk());
                intent.putExtra("Kategori", p.getNameCategory());
                intent.putExtra("Jumlah", p.getAmount());
                intent.putExtra("Month", newMonth);
                intent.putExtra("Year", nowYear);
                intent.putExtra("Color", chartColor.get(position));
                startActivity(intent);
            }
        });

        return view;
    }


    private void toSaveChart() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String name = "消费图_" + timeStamp;
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION_CODE);
            if(REQUEST_PERMISSION_CODE==0) {
                Toast.makeText(getActivity(), "请授予权限!", Toast.LENGTH_SHORT).show();
            }
        }
        try {

            if(mChart.saveToGallery(name, 100)) {
                Toast.makeText(getActivity(), "保存成功!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }
    }


    public void setDataForList() {
        adapter = new ArrayAdapter<Report>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Reports) {
            @Override
            public View getView(int position,
                                View convertView,
                                ViewGroup parent) {
                Report current = getItem(position);
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.item_report, null, false);
                }
                TextView txtNama = (TextView) convertView.findViewById(R.id.namakat);
                TextView txtJum = (TextView) convertView.findViewById(R.id.jumlah);
                TextView txtTot = (TextView) convertView.findViewById(R.id.totalkeluar);
                ImageView icon = (ImageView) convertView.findViewById(R.id.iconnya);

                icon.setBackgroundColor(chartColor.get(position));
                txtNama.setText(current.getNameCategory());
                txtTot.setText(String.valueOf(current.getTotal()) + " 支出");
                txtJum.setText(Utils.convertCur(String.valueOf(current.getAmount())));
                return convertView;
            }
        };
        detaillist.setAdapter(adapter);
        Utils.setListViewHeightBasedOnChildren(detaillist);
        if (adapter.getCount() == 0) {
            detaillist.setEmptyView(view.findViewById(R.id.emptyview));
        }
    }


    public void settingUpchart() {
        Description description = new Description();
        description.setTextColor(000);
        description.setText("数据图表");
        mChart.setDescription(description);
        mChart.setRotationEnabled(true);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                PieEntry pe = (PieEntry) e;
                Toast.makeText(getActivity(),
                        String.valueOf(pe.getLabel()+" :"+ Math.round(pe.getValue())).split("少女账记")[0] + "笔支出 ",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }


    public void setDataForPieChart(String month) {
        Reports = report.getMontlyReport(month, String.valueOf(nowYear));
        if (Reports.size() != 0) {
            int total = 0, amount = 0, col = 0;
            contentLayout.setVisibility(View.VISIBLE);
            chartValue = new ArrayList<PieEntry>();
            chartColor = new ArrayList<Integer>();
            for (Report lp : Reports) {
                amount += lp.getAmount();
                total += lp.getTotal();
                chartValue.add(new PieEntry(lp.getTotal(), lp.getNameCategory()));
                chartColor.add(Color.parseColor(arrayColor[col]));
                col++;
            }

            PieDataSet dataSet = new PieDataSet(chartValue, "");
            dataSet.setSliceSpace(3);
            dataSet.setSelectionShift(5);
            dataSet.setColors(chartColor);
            dataSet.setValueFormatter(new MyValueFormatter());
            //图例,设置在图下方
            Legend l = mChart.getLegend();
            l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
            l.setWordWrapEnabled(true);
            l.setXEntrySpace(9);
            l.setYEntrySpace(5);

            PieData data = new PieData(dataSet);
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            mChart.setData(data);
            String center = String.valueOf(total) + " 支出 \n 总 :\n" + Utils.convertCur(String.valueOf(amount));
            mChart.setCenterText(center);

            mChart.highlightValues(null);
            mChart.invalidate();
            mChart.animateXY(1500, 1500);

        } else {
            Toast.makeText(getActivity(), "无数据", Toast.LENGTH_SHORT).show();
            contentLayout.setVisibility(View.GONE);
        }

    }


    public List<String> dataSpinner() {
        List<String> tospinner = new ArrayList<>();
        String bulan;
        nowYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 12; i++) {
            bulan = getResources().getStringArray(R.array.Bulan)[i];
            tospinner.add(String.valueOf(nowYear) + "年 " + bulan);
        }
        tospinner.add(0, "选择年月");
        return tospinner;
    }


    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }
    }

}