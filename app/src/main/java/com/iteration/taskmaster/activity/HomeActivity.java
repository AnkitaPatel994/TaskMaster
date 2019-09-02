package com.iteration.taskmaster.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.LinearLayout;

import com.iteration.taskmaster.R;

public class HomeActivity extends AppCompatActivity {

    String company_id,company_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        company_name = getIntent().getExtras().getString("company_name");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(company_name);
        setSupportActionBar(toolbar);


        company_id = getIntent().getExtras().getString("company_id");

        LinearLayout llAddTask = findViewById(R.id.llAddTask);
        llAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,AddTaskActivity.class);
                i.putExtra("company_id",company_id);
                i.putExtra("company_name",company_name);
                startActivity(i);
            }
        });

        LinearLayout llViewAllTask = findViewById(R.id.llViewAllTask);
        llViewAllTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,ViewAllTaskActivity.class);
                i.putExtra("company_id",company_id);
                startActivity(i);
            }
        });

        LinearLayout llPendingTask = findViewById(R.id.llPendingTask);
        llPendingTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,PendingTaskActivity.class);
                i.putExtra("company_id",company_id);
                i.putExtra("company_name",company_name);
                startActivity(i);
            }
        });

        LinearLayout llPendingDueTask = findViewById(R.id.llPendingDueTask);
        llPendingDueTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,PendingDueTaskActivity.class);
                i.putExtra("company_id",company_id);
                i.putExtra("company_name",company_name);
                startActivity(i);
            }
        });

        LinearLayout llCompleteTask = findViewById(R.id.llCompleteTask);
        llCompleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,CompleteTaskActivity.class);
                i.putExtra("company_id",company_id);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(HomeActivity.this,CompanyListActivity.class);
        startActivity(i);
    }
}
