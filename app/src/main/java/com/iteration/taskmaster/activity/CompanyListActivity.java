package com.iteration.taskmaster.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iteration.taskmaster.R;
import com.iteration.taskmaster.adapter.CompanyListAdapter;
import com.iteration.taskmaster.adapter.PendingTaskListAdapter;
import com.iteration.taskmaster.model.Company;
import com.iteration.taskmaster.model.CompanyList;
import com.iteration.taskmaster.model.Message;
import com.iteration.taskmaster.model.ViewTask;
import com.iteration.taskmaster.network.GetProductDataService;
import com.iteration.taskmaster.network.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyListActivity extends AppCompatActivity {

    RecyclerView rvCompanyList;
    ArrayList<Company> CompanyListArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        LinearLayout llAddCompany = findViewById(R.id.llAddCompany);
        llAddCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog_rm = new Dialog(CompanyListActivity.this,android.R.style.Theme_Light_NoTitleBar);
                dialog_rm.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog_rm.setContentView(R.layout.add_company_dialog);
                dialog_rm.setCancelable(true);

                final GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

                final EditText txtCompanyName = dialog_rm.findViewById(R.id.txtCompanyName);

                ImageView ivPDClose = (ImageView) dialog_rm.findViewById(R.id.ivPDClose);
                ivPDClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_rm.dismiss();
                    }
                });

                Button btnCompanySubmit = (Button) dialog_rm.findViewById(R.id.btnCompanySubmit);
                btnCompanySubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String c_name = txtCompanyName.getText().toString();

                        final ProgressDialog dialog = new ProgressDialog(CompanyListActivity.this);
                        dialog.setMessage("Loading...");
                        dialog.setCancelable(true);
                        dialog.show();

                        Call<Message> AddCompanyCall = productDataService.getAddCompanyData(c_name);
                        AddCompanyCall.enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                dialog.dismiss();
                                String status = response.body().getStatus();
                                String message = response.body().getMessage();
                                if (status.equals("1"))
                                {
                                    Log.d("message",""+message);
                                    dialog_rm.dismiss();
                                    finish();
                                    startActivity(getIntent());
                                }
                                else
                                {
                                    Log.d("message",""+message);
                                }
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                Toast.makeText(CompanyListActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                dialog_rm.show();
            }
        });

        rvCompanyList = findViewById(R.id.rvCompanyList);
        rvCompanyList.setHasFixedSize(true);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        rvCompanyList.setLayoutManager(manager);

        Call<CompanyList> CompanyCall = productDataService.getCompanyData();
        CompanyCall.enqueue(new Callback<CompanyList>() {
            @Override
            public void onResponse(Call<CompanyList> call, Response<CompanyList> response) {
                String status = response.body().getStatus();
                String message = response.body().getMessage();
                if (status.equals("1"))
                {
                    Log.d("message",""+message);
                    CompanyListArray = response.body().getCompanyList();
                    CompanyListAdapter companyListAdapter = new CompanyListAdapter(CompanyListActivity.this,CompanyListArray);
                    rvCompanyList.setAdapter(companyListAdapter);
                }
                else
                {
                    Log.d("message",""+message);
                }
            }

            @Override
            public void onFailure(Call<CompanyList> call, Throwable t) {
                Toast.makeText(CompanyListActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
