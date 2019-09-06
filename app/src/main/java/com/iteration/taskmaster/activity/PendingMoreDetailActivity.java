package com.iteration.taskmaster.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.iteration.taskmaster.R;
import com.iteration.taskmaster.model.Message;
import com.iteration.taskmaster.network.GetProductDataService;
import com.iteration.taskmaster.network.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingMoreDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_more_detail);

        final GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        String task_name = getIntent().getExtras().getString("task_name");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(task_name);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final String task_id = getIntent().getExtras().getString("task_id");
        String task_des = getIntent().getExtras().getString("task_des");
        final String company_id = getIntent().getExtras().getString("company_id");
        final String company_name = getIntent().getExtras().getString("company_name");

        TextView txtPMDTaskDescription = findViewById(R.id.txtPMDTaskDescription);
        txtPMDTaskDescription.setText(task_des);

        Button btnCTSubmit = findViewById(R.id.btnCTSubmit);
        btnCTSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog dialog = new ProgressDialog(PendingMoreDetailActivity.this);
                dialog.setMessage("Loading...");
                dialog.setCancelable(true);
                dialog.show();

                Call<Message> UpdateTaskCall = productDataService.getUpdateTaskListData(task_id,"Complete");
                UpdateTaskCall.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        dialog.dismiss();
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();
                        if (status.equals("1"))
                        {
                            Log.d("message",""+message);
                            Intent i = new Intent(PendingMoreDetailActivity.this, HomeActivity.class);
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
                        Toast.makeText(PendingMoreDetailActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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
