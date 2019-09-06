package com.iteration.taskmaster.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.iteration.taskmaster.adapter.PendingTaskListAdapter;
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

public class PendingDueTaskActivity extends AppCompatActivity {

    RecyclerView rvPendingDueTask;
    ArrayList<Task> ViewTaskListArray = new ArrayList<>();
    String company_id,company_name;
    Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);
    GetProductDataService productDataService;
    int yearM,monthM,dayM;
    TextView txtPDTStartDate,txtPDTEndDate;
    Button btnPDTSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_due_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        txtPDTStartDate = findViewById(R.id.txtPDTStartDate);
        txtPDTEndDate = findViewById(R.id.txtPDTEndDate);
        btnPDTSearch = findViewById(R.id.btnPDTSearch);

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        txtPDTStartDate.setText(sdfDate.format(new Date()));
        txtPDTEndDate.setText(sdfDate.format(new Date()));

        txtPDTStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog mDatePicker = new DatePickerDialog(PendingDueTaskActivity.this,new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        selectedmonth = selectedmonth + 1;

                        if(selectedmonth < 10 && selectedday < 10)
                        {
                            txtPDTStartDate.setText(selectedyear + "-" + "0"+selectedmonth + "-" + "0"+selectedday);
                        }
                        else if(selectedmonth < 10)
                        {
                            txtPDTStartDate.setText(selectedyear + "-" + "0"+selectedmonth + "-" + selectedday);
                        }
                        else if(selectedday < 10)
                        {
                            txtPDTStartDate.setText(selectedyear + "-" + selectedmonth + "-" + "0"+selectedday);
                        }
                        else
                        {
                            txtPDTStartDate.setText(selectedyear + "-" + selectedmonth + "-" + selectedday);
                        }

                    }
                }, mYear, mMonth, mDay);

                //mDatePicker.getDatePicker().setMinDate(c.getTimeInMillis());
                mDatePicker.show();
            }
        });

        txtPDTEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog mDatePicker = new DatePickerDialog(PendingDueTaskActivity.this,new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        selectedmonth = selectedmonth + 1;

                        if(selectedmonth < 10 && selectedday < 10)
                        {
                            txtPDTEndDate.setText(selectedyear + "-" + "0"+selectedmonth + "-" + "0"+selectedday);
                        }
                        else if(selectedmonth < 10)
                        {
                            txtPDTEndDate.setText(selectedyear + "-" + "0"+selectedmonth + "-" + selectedday);
                        }
                        else if(selectedday < 10)
                        {
                            txtPDTEndDate.setText(selectedyear + "-" + selectedmonth + "-" + "0"+selectedday);
                        }
                        else
                        {
                            txtPDTEndDate.setText(selectedyear + "-" + selectedmonth + "-" + selectedday);
                        }

                    }
                }, mYear, mMonth, mDay);

                //mDatePicker.getDatePicker().setMinDate(c.getTimeInMillis());
                mDatePicker.show();
            }
        });

        company_id = getIntent().getExtras().getString("company_id");
        company_name = getIntent().getExtras().getString("company_name");

        rvPendingDueTask = (RecyclerView)findViewById(R.id.rvPendingDueTask);
        rvPendingDueTask.setHasFixedSize(true);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        rvPendingDueTask.setLayoutManager(manager);

        String StartDate = txtPDTStartDate.getText().toString();
        String EndDate = txtPDTEndDate.getText().toString();
        ViewtTask(company_id,"PendingDue",StartDate,EndDate);

        btnPDTSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String StartDate = txtPDTStartDate.getText().toString();
                String EndDate = txtPDTEndDate.getText().toString();
                ViewtTask(company_id,"PendingDue",StartDate,EndDate);
            }
        });

    }

    private void ViewtTask(final String company_id, String status, String startDate, String endDate) {
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
                    PendingTaskListAdapter pendingTaskListAdapter = new PendingTaskListAdapter(PendingDueTaskActivity.this,ViewTaskListArray,company_id,company_name);
                    rvPendingDueTask.setAdapter(pendingTaskListAdapter);
                }
                else
                {
                    Log.d("message",""+message);
                }
            }

            @Override
            public void onFailure(Call<ViewTask> call, Throwable t) {
                Toast.makeText(PendingDueTaskActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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
