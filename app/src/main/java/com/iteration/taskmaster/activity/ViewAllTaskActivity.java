package com.iteration.taskmaster.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iteration.taskmaster.R;
import com.iteration.taskmaster.adapter.ViewTaskListAdapter;
import com.iteration.taskmaster.model.Message;
import com.iteration.taskmaster.model.Task;
import com.iteration.taskmaster.model.ViewTask;
import com.iteration.taskmaster.network.GetProductDataService;
import com.iteration.taskmaster.network.RetrofitInstance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllTaskActivity extends AppCompatActivity {

    RecyclerView rvViewAllTask;
    ArrayList<Task> ViewTaskListArray = new ArrayList<>();
    String company_id;
    Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);
    GetProductDataService productDataService;
    int yearM,monthM,dayM;
    TextView txtVTStartDate,txtVTEndDate;
    Button btnVTSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        txtVTStartDate = findViewById(R.id.txtVTStartDate);
        txtVTEndDate = findViewById(R.id.txtVTEndDate);
        btnVTSearch = findViewById(R.id.btnVTSearch);

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        txtVTStartDate.setText(sdfDate.format(new Date()));
        txtVTEndDate.setText(sdfDate.format(new Date()));

        txtVTStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog mDatePicker = new DatePickerDialog(ViewAllTaskActivity.this,new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        selectedmonth = selectedmonth + 1;

                        if(selectedmonth < 10 && selectedday < 10)
                        {
                            txtVTStartDate.setText(selectedyear + "-" + "0"+selectedmonth + "-" + "0"+selectedday);
                        }
                        else if(selectedmonth < 10)
                        {
                            txtVTStartDate.setText(selectedyear + "-" + "0"+selectedmonth + "-" + selectedday);
                        }
                        else if(selectedday < 10)
                        {
                            txtVTStartDate.setText(selectedyear + "-" + selectedmonth + "-" + "0"+selectedday);
                        }
                        else
                        {
                            txtVTStartDate.setText(selectedyear + "-" + selectedmonth + "-" + selectedday);
                        }

                    }
                }, mYear, mMonth, mDay);

                mDatePicker.show();
            }
        });

        txtVTEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog mDatePicker = new DatePickerDialog(ViewAllTaskActivity.this,new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        selectedmonth = selectedmonth + 1;

                        if(selectedmonth < 10 && selectedday < 10)
                        {
                            txtVTEndDate.setText(selectedyear + "-" + "0"+selectedmonth + "-" + "0"+selectedday);
                        }
                        else if(selectedmonth < 10)
                        {
                            txtVTEndDate.setText(selectedyear + "-" + "0"+selectedmonth + "-" + selectedday);
                        }
                        else if(selectedday < 10)
                        {
                            txtVTEndDate.setText(selectedyear + "-" + selectedmonth + "-" + "0"+selectedday);
                        }
                        else
                        {
                            txtVTEndDate.setText(selectedyear + "-" + selectedmonth + "-" + selectedday);
                        }

                    }
                }, mYear, mMonth, mDay);

                //mDatePicker.getDatePicker().setMinDate(c.getTimeInMillis());
                mDatePicker.show();
            }
        });

        company_id = getIntent().getExtras().getString("company_id");

        rvViewAllTask = (RecyclerView)findViewById(R.id.rvViewAllTask);
        rvViewAllTask.setHasFixedSize(true);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        rvViewAllTask.setLayoutManager(manager);

        String StartDate = txtVTStartDate.getText().toString();
        String EndDate = txtVTEndDate.getText().toString();
        ViewtTask(company_id,"*",StartDate,EndDate);

        btnVTSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String StartDate = txtVTStartDate.getText().toString();
                String EndDate = txtVTEndDate.getText().toString();
                ViewtTask(company_id,"*",StartDate,EndDate);
            }
        });

    }

    private void ViewtTask(String company_id, String status, String startDate, String endDate) {
        Call<ViewTask> ViewtTaskCall = productDataService.getViewtTaskListData(company_id,status,startDate,endDate);
        ViewtTaskCall.enqueue(new Callback<ViewTask>() {
            @Override
            public void onResponse(Call<ViewTask> call, Response<ViewTask> response) {
                String status = response.body().getStatus();
                String message = response.body().getMessage();
                if (status.equals("1"))
                {
                    Log.d("message",""+message);
                    ViewTaskListArray = response.body().getTaskList();
                    ViewTaskListAdapter categoryAllListAdapter = new ViewTaskListAdapter(ViewAllTaskActivity.this,ViewTaskListArray);
                    rvViewAllTask.setAdapter(categoryAllListAdapter);
                }
                else
                {
                    Log.d("message",""+message);
                }
            }

            @Override
            public void onFailure(Call<ViewTask> call, Throwable t) {
                Toast.makeText(ViewAllTaskActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
