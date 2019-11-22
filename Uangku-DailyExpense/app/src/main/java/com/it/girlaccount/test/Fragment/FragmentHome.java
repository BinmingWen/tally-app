package com.it.girlaccount.test.Fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.it.girlaccount.test.Controller.ExpenseController;
import com.it.girlaccount.test.MainActivity;
import com.it.girlaccount.test.Model.Expense;
import com.it.girlaccount.test.Model.Remain;
import com.it.girlaccount.test.Utils.DatePicker;
import com.it.girlaccount.test.Utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TooManyListenersException;

import com.it.girlaccount.test.Controller.CategoryController;
import com.it.girlaccount.test.Controller.RemainController;
import com.it.girlaccount.test.Model.Category;
import com.it.girlaccount.test.R;


public class FragmentHome extends Fragment {

    public FragmentHome() {
    }

    RelativeLayout view;
    View mView;



    int SELECTED_ID = 0;
    int SELECTED_IDK = 0;

    Remain myRemain;
    Expense selectedExpense;

    List<Expense> expensesList;
    ArrayAdapter<Expense> adapter;

    EditText inputAmount, inputDesc;
    TextView currentRemain, todayTotal;
    Spinner spinCategory;
    ListView expensesListView;
    Button btnTgl,btnNext,btnPrev;

    private CategoryController category;
    private RemainController remain;
    private ExpenseController expense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = (RelativeLayout) inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("我的小金库");
        ((MainActivity) getActivity()).showFloatingActionButton();

        category = new CategoryController(getActivity());
        expense = new ExpenseController(getActivity());
        remain = new RemainController(getActivity());

        currentRemain = (TextView) view.findViewById(R.id.currentSaldo);
        todayTotal = (TextView) view.findViewById(R.id.todayOut);
        expensesListView = (ListView) view.findViewById(R.id.outList);

        btnTgl = (Button) view.findViewById(R.id.tgl);
        btnTgl.setText(Utils.getDateNow());
        btnTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnNext = (Button) view.findViewById(R.id.next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDay("+");
            }
        });

        btnPrev = (Button) view.findViewById(R.id.prev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDay("-");
            }
        });

        new MyAsynch().execute(btnTgl.getText().toString());
        expensesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedExpense = (Expense) parent.getItemAtPosition(position);
                SELECTED_ID = selectedExpense.getId();
                menuItemDialog();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFormDialog("Add");
            }
        });
        return view;
    }

    private void changeDay(String s){
        String date = btnTgl.getText().toString();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(df.parse(date));
            if (s.equalsIgnoreCase("+")){
                c.add(Calendar.DATE,1);
            }else{
                c.add(Calendar.DATE,-1);
            }
            date = df.format(c.getTime());
            btnTgl.setText(date);
            new MyAsynch().execute(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }




    private class MyAsynch extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... Strings) { // run time intensive task in separate thread
            expensesList = expense.getExpenseByDate(Strings[0]);
            return null;
        }

        protected void onPostExecute(String result) {
            // Give the data to you adapter from here,instead of the place where you gave it earlier
            setMyRemain();
            setListAdapter(expensesList);
            expensesListView.setAdapter(adapter);
            if (expense.getCountByDate(btnTgl.getText().toString()) != 0) {
                todayTotal.setText(Utils.convertCur(expense.getTotal(btnTgl.getText().toString())));
            } else {
                todayTotal.setText(" 无 ");
            }

            if (adapter.getCount() == 0) {
                expensesListView.setEmptyView(view.findViewById(R.id.emptyview));
            }
        }
    }


    public void setMyRemain() {

        myRemain = new Remain(1, Integer.parseInt(remain.getRemain()));
        String current = Utils.convertCur(String.valueOf(myRemain.getRemain()));
        currentRemain.setText(current);
    }


    public void setListAdapter(List<Expense> expense) {
        adapter = new ArrayAdapter<Expense>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, expense) {

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


    public void loadCategory() {
        List<Category> categories = category.getAllCategory();
        ArrayAdapter<Category> dataAdapter = new ArrayAdapter<Category>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, categories);
        spinCategory.setAdapter(dataAdapter);
        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category kat = (Category) parent.getItemAtPosition(position);
                SELECTED_IDK = kat.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }



    public void menuItemDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("更新&&删除");
        builder.setItems(R.array.dialog_expense, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showFormDialog("Update");
                        break;
                    case 1:
                        expense.deleteExpenseById(SELECTED_ID);
                        remain.updateRemain(myRemain.getRemain() + selectedExpense.getAmount());
                        new MyAsynch().execute(btnTgl.getText().toString());
                        break;
                }
            }
        });
        builder.show();
    }

    public void showFormDialog(String to) {
        final String todo = to;
        LayoutInflater mainLayout = LayoutInflater.from(getActivity());
        mView = mainLayout.inflate(R.layout.dialog_expense, null);
        inputAmount = (EditText) mView.findViewById(R.id.inputJumlah);
        inputDesc = (EditText) mView.findViewById(R.id.inputDesk);
        spinCategory = (Spinner) mView.findViewById(R.id.spinner);
        loadCategory();

        if (todo.equals("Update")) {
            inputAmount.setText(String.valueOf(selectedExpense.getAmount()));
            inputDesc.setText(selectedExpense.getDescription());
            setSelectedSpinner(spinCategory, category.getName(selectedExpense.getIdk()));
        }

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        if(inputAmount.length()==0||inputDesc.length()==0) {
                            Toast.makeText(getActivity(), "输入框的值不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            Expense exp = new Expense();
                            exp.setId(SELECTED_ID);
                            exp.setIdk(SELECTED_IDK);
                            exp.setDescription(inputDesc.getText().toString());
                            exp.setAmount(Integer.parseInt(inputAmount.getText().toString()));
                            exp.setDate(btnTgl.getText().toString());
                            if (todo.equals("Add")) {
                                expense.addExpense(exp);
                                remain.updateRemain(myRemain.getRemain() - exp.getAmount());
                            } else {
                                expense.updateExpense(exp);
                                remain.updateRemain(changeBalance(exp.getAmount()));
                            }
                        }
                        new MyAsynch().execute(btnTgl.getText().toString());
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


    private void showDatePicker() {
        DatePicker date = new DatePicker();
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
            btnTgl.setText(formattedDate);
            new MyAsynch().execute(formattedDate);
        }
    };


    private int changeBalance(int newAmount) {
        int oldAmount = selectedExpense.getAmount();
        if (newAmount > oldAmount) {
            return myRemain.getRemain() - (newAmount - oldAmount);
        } else if (newAmount < oldAmount) {
            return myRemain.getRemain() + (oldAmount - newAmount);
        } else {
            return myRemain.getRemain();
        }
    }

    private void setSelectedSpinner(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}