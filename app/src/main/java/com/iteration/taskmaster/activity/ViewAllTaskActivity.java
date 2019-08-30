package com.iteration.taskmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.iteration.taskmaster.R;
import com.iteration.taskmaster.adapter.ViewTaskListAdapter;
import com.iteration.taskmaster.model.Message;
import com.iteration.taskmaster.model.Task;
import com.iteration.taskmaster.model.ViewTask;
import com.iteration.taskmaster.network.GetProductDataService;
import com.iteration.taskmaster.network.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllTaskActivity extends AppCompatActivity {

    RecyclerView rvViewAllTask;
    ArrayList<Task> ViewTaskListArray = new ArrayList<>();

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

        GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        rvViewAllTask = (RecyclerView)findViewById(R.id.rvViewAllTask);
        rvViewAllTask.setHasFixedSize(true);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        rvViewAllTask.setLayoutManager(manager);

        Call<ViewTask> ViewtTaskCall = productDataService.getViewtTaskListData("*");
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
