package com.iteration.taskmaster.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
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

public class AddReminderActivity extends AppCompatActivity {

    EditText txtReminderName;
    LinearLayout llReminderDate,llReminderTime;
    TextView txtReminderDate,txtReminderTime;
    Button btnReminderSubmit;
    String company_id,company_name;
    Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    GetProductDataService productDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        company_id = getIntent().getExtras().getString("company_id");
        company_name = getIntent().getExtras().getString("company_name");

        txtReminderName = findViewById(R.id.txtReminderName);

        llReminderDate = findViewById(R.id.llReminderDate);
        txtReminderDate = findViewById(R.id.txtReminderDate);

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        txtReminderDate.setText(sdfDate.format(new Date()));

        llReminderTime = findViewById(R.id.llReminderTime);
        txtReminderTime = findViewById(R.id.txtReminderTime);

        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm a");
        txtReminderTime.setText(sdfTime.format(new Date()));

        btnReminderSubmit = findViewById(R.id.btnReminderSubmit);

        llReminderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog mDatePicker = new DatePickerDialog(AddReminderActivity.this,new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        selectedmonth = selectedmonth + 1;

                        if(selectedmonth < 10 && selectedday < 10)
                        {
                            txtReminderDate.setText(selectedyear + "-" + "0"+selectedmonth + "-" + "0"+selectedday);
                        }
                        else if(selectedmonth < 10)
                        {
                            txtReminderDate.setText(selectedyear + "-" + "0"+selectedmonth + "-" + selectedday);
                        }
                        else if(selectedday < 10)
                        {
                            txtReminderDate.setText(selectedyear + "-" + selectedmonth + "-" + "0"+selectedday);
                        }
                        else
                        {
                            txtReminderDate.setText(selectedyear + "-" + selectedmonth + "-" + selectedday);
                        }

                    }
                }, mYear, mMonth, mDay);

                mDatePicker.getDatePicker().setMinDate(c.getTimeInMillis());
                mDatePicker.show();
            }
        });

        llReminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog mTimePicker = new TimePickerDialog(AddReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String AM_PM ;
                        String sHour = "00";
                        String sMinute = "00";

                        if(selectedHour < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }

                        if(selectedHour < 10)
                        {
                            sHour = "0"+selectedHour;
                        }
                        else
                        {
                            sHour = String.valueOf(selectedHour);
                        }

                        if(selectedMinute < 10)
                        {
                            sMinute = "0"+selectedMinute;
                        }
                        else
                        {
                            sMinute = String.valueOf(selectedMinute);
                        }

                        txtReminderTime.setText(sHour + ":" + sMinute + " "+ AM_PM);

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        btnReminderSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String r_name = txtReminderName.getText().toString();
                String r_date = txtReminderDate.getText().toString();
                String r_time = txtReminderTime.getText().toString();

                final ProgressDialog dialog = new ProgressDialog(AddReminderActivity.this);
                dialog.setMessage("Loading...");
                dialog.setCancelable(true);
                dialog.show();

                Call<Message> AddReminderCall = productDataService.getAddReminderData(r_name,r_date,r_time);
                AddReminderCall.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        dialog.dismiss();
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();
                        if (status.equals("1"))
                        {
                            Log.d("message",""+message);
                            Intent i = new Intent(AddReminderActivity.this,HomeActivity.class);
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
                        Toast.makeText(AddReminderActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
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
