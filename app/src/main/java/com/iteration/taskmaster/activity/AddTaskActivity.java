package com.iteration.taskmaster.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iteration.taskmaster.R;
import com.iteration.taskmaster.model.Message;
import com.iteration.taskmaster.network.GetProductDataService;
import com.iteration.taskmaster.network.RetrofitInstance;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTaskActivity extends AppCompatActivity {

    GetProductDataService productDataService;
    EditText txtTaskName,txtTaskDescription;
    LinearLayout llTaskDueDate;
    TextView txtTaskDueDate;
    Button btnSubmit;
    String company_id,company_name;

    Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        company_id = getIntent().getExtras().getString("company_id");
        company_name = getIntent().getExtras().getString("company_name");

        txtTaskName = findViewById(R.id.txtTaskName);
        txtTaskDescription = findViewById(R.id.txtTaskDescription);
        llTaskDueDate = findViewById(R.id.llTaskDueDate);
        txtTaskDueDate = findViewById(R.id.txtTaskDueDate);
        btnSubmit = findViewById(R.id.btnSubmit);

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        txtTaskDueDate.setText(sdfDate.format(new Date()));

        llTaskDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(AddTaskActivity.this,new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        selectedmonth = selectedmonth + 1;

                        if(selectedmonth < 10 && selectedday < 10)
                        {
                            txtTaskDueDate.setText(selectedyear + "-" + "0"+selectedmonth + "-" + "0"+selectedday);
                        }
                        else if(selectedmonth < 10)
                        {
                            txtTaskDueDate.setText(selectedyear + "-" + "0"+selectedmonth + "-" + selectedday);
                        }
                        else if(selectedday < 10)
                        {
                            txtTaskDueDate.setText(selectedyear + "-" + selectedmonth + "-" + "0"+selectedday);
                        }
                        else
                        {
                            txtTaskDueDate.setText(selectedyear + "-" + selectedmonth + "-" + selectedday);
                        }

                    }
                }, mYear, mMonth, mDay);

                mDatePicker.getDatePicker().setMinDate(c.getTimeInMillis());
                mDatePicker.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String TaskName = txtTaskName.getText().toString();
                String TaskDescription = txtTaskDescription.getText().toString();
                String TaskDueDate = txtTaskDueDate.getText().toString();

                if (TaskName.equals("") && TaskDescription.equals(""))
                {
                    Toast.makeText(AddTaskActivity.this,"Task Name not empty...",Toast.LENGTH_SHORT).show();
                }
                else if (TaskDescription.equals(""))
                {
                    Toast.makeText(AddTaskActivity.this,"Task Description not empty...",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final ProgressDialog dialog = new ProgressDialog(AddTaskActivity.this);
                    dialog.setMessage("Loading...");
                    dialog.setCancelable(true);
                    dialog.show();

                    Call<Message> AddTaskCall = productDataService.getAddTaskData(TaskName,TaskDescription,TaskDueDate,company_id,"Pending");
                    AddTaskCall.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            dialog.dismiss();
                            String status = response.body().getStatus();
                            String message = response.body().getMessage();
                            if (status.equals("1"))
                            {
                                Log.d("message",""+message);
                                Intent i = new Intent(AddTaskActivity.this,HomeActivity.class);
                                i.putExtra("company_id",company_id);
                                i.putExtra("company_name",company_name);
                                startActivity(i);
                            }
                            else
                            {
                                Log.d("message",""+message);
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Toast.makeText(AddTaskActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

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
