package com.it.girlaccount.test.Fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.it.girlaccount.test.Controller.CategoryController;
import com.it.girlaccount.test.Controller.ExpenseController;
import com.it.girlaccount.test.MainActivity;
import com.it.girlaccount.test.Model.Category;
import com.it.girlaccount.test.R;



public class FragmentCategory extends Fragment {

    public FragmentCategory() {
    }


    RelativeLayout view;


    private CategoryController category;
    private ExpenseController expense;
    private Category selectedCategory;
    ListView categoryListView;
    List<Category> categorisList;
    ArrayAdapter<Category> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = (RelativeLayout) inflater.inflate(R.layout.fragment_category, container, false);
        getActivity().setTitle("类别");
        ((MainActivity) getActivity()).hideFloatingActionButton();

        category = new CategoryController(getActivity());
        expense = new ExpenseController(getActivity());
        Button addBtn = (Button) view.findViewById(R.id.addKategori);
        categoryListView = (ListView) view.findViewById(R.id.listKategori);

        new MyAsynch().execute();
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = (Category) parent.getItemAtPosition(position);
                menuItemDialog();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFormDialog("Add");
            }
        });
        return view;
    }


    private class MyAsynch extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... Strings) { // run time intensive task in separate thread
            categorisList = category.getAllCategory();
            return null;
        }

        protected void onPostExecute(String result) {
            // Give the data to you adapter from here,instead of the place where you gave it earlier
            categoryListView = (ListView) view.findViewById(R.id.listKategori);
            setListAdapter(categorisList);
            categoryListView.setAdapter(adapter);
            if (adapter.getCount() == 0) {
                categoryListView.setEmptyView(view.findViewById(R.id.emptyview));
            }
        }
    }


    public void setListAdapter(List<Category> categoris) {
        adapter = new ArrayAdapter<Category>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, categoris) {
            @Override
            public View getView(int position,
                                View convertView,
                                ViewGroup parent) {
                Category current = getItem(position);
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.item_category, null, false);
                }
                TextView txtId = (TextView) convertView.findViewById(R.id.idkat);
                TextView txtKat = (TextView) convertView.findViewById(R.id.namakat);
                txtId.setText(String.valueOf(current.getId()));
                txtKat.setText(current.getCategory());
                return convertView;
            }
        };

    }




    public void menuItemDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("编辑&&删除");
        builder.setItems(R.array.dialog_kategori, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showFormDialog("Update");
                        break;
                    case 1:
                        if (category.getCount() < 5) {
                            Toast.makeText(getActivity(), "至少有4个类别", Toast.LENGTH_SHORT).show();
                        } else {
                            category.deleteCategory(
                                    selectedCategory.getId(),
                                    expense.isCategoryExist(selectedCategory.getId()));
                            // makeList();
                            new MyAsynch().execute();
                        }
                        break;
                }
            }
        });
        builder.show();
    }



    public void showFormDialog(String to) {
        LayoutInflater mainLayout = LayoutInflater.from(getActivity());
        View mView = mainLayout.inflate(R.layout.dialog_category, null);
        final String todo = to;
        final EditText newCategory = (EditText) mView.findViewById(R.id.userInputDialog);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);

        if (todo.equals("Update")) {
            newCategory.setText(selectedCategory.getCategory());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        if(newCategory.length()==0) {
                            Toast.makeText(getActivity(), "输入框的值不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            if(category.isNameExist(newCategory.getText().toString())){
                                Toast.makeText(getActivity(), "该类型已经存在", Toast.LENGTH_SHORT).show();
                            }else {
                                Category kat = new Category();
                                if (todo.equals("Add")) {
                                    kat.setCategory(newCategory.getText().toString());
                                    category.addCategory(kat);
                                } else {
                                    kat.setId(selectedCategory.getId());
                                    kat.setCategory(newCategory.getText().toString());
                                    category.updateCategory(kat);
                                }
                            }

                        }
                        new MyAsynch().execute();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();
                    }
                });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
}