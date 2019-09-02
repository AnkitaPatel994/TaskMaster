package com.iteration.taskmaster.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.iteration.taskmaster.R;
import com.iteration.taskmaster.activity.CompanyListActivity;
import com.iteration.taskmaster.activity.HomeActivity;
import com.iteration.taskmaster.model.Company;
import com.iteration.taskmaster.model.Message;
import com.iteration.taskmaster.model.Task;
import com.iteration.taskmaster.network.GetProductDataService;
import com.iteration.taskmaster.network.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.ViewHolder> {

    Context context;
    ArrayList<Company> companyListArray;

    public CompanyListAdapter(Context context, ArrayList<Company> companyListArray) {
        this.context = context;
        this.companyListArray = companyListArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.company_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

        final String company_id = companyListArray.get(position).getC_id();
        final String company_name = companyListArray.get(position).getC_name();

        viewHolder.txtCompanyNameA.setText(company_name);

        viewHolder.llCompanyListA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,HomeActivity.class);
                i.putExtra("company_id",company_id);
                i.putExtra("company_name",company_name);
                context.startActivity(i);
            }
        });

        viewHolder.llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog_rm = new Dialog(context,android.R.style.Theme_Light_NoTitleBar);
                dialog_rm.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog_rm.setContentView(R.layout.add_company_dialog);
                dialog_rm.setCancelable(true);

                final GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

                TextView tvCTitle = dialog_rm.findViewById(R.id.tvCTitle);
                tvCTitle.setText("Edit Company");
                final EditText txtCompanyName = dialog_rm.findViewById(R.id.txtCompanyName);
                txtCompanyName.setText(company_name);

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

                        final ProgressDialog dialog = new ProgressDialog(context);
                        dialog.setMessage("Loading...");
                        dialog.setCancelable(true);
                        dialog.show();

                        Call<Message> UpdateCompanyCall = productDataService.getUpdateCompanyData(company_id,c_name);
                        UpdateCompanyCall.enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                dialog.dismiss();
                                String status = response.body().getStatus();
                                String message = response.body().getMessage();
                                if (status.equals("1"))
                                {
                                    Log.d("message",""+message);
                                    dialog_rm.dismiss();
                                    Intent i = new Intent(context,CompanyListActivity.class);
                                    context.startActivity(i);
                                }
                                else
                                {
                                    Log.d("message",""+message);
                                }
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                dialog_rm.show();
            }
        });

        viewHolder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage("Loading...");
            dialog.setCancelable(true);
            dialog.show();

            Call<Message> DeleteCompanyCall = productDataService.getDeleteCompanyData(company_id);
            DeleteCompanyCall.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    dialog.dismiss();
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equals("1"))
                    {
                        Log.d("message",""+message);
                        Intent i = new Intent(context,CompanyListActivity.class);
                        context.startActivity(i);
                    }
                    else
                    {
                        Log.d("message",""+message);
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
            }
        });

    }

    @Override
    public int getItemCount() {
        return companyListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCompanyNameA;
        LinearLayout llCompanyListA,llEdit,llDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            txtCompanyNameA = itemView.findViewById(R.id.txtCompanyNameA);
            llCompanyListA = itemView.findViewById(R.id.llCompanyListA);
            llEdit = itemView.findViewById(R.id.llEdit);
            llDelete = itemView.findViewById(R.id.llDelete);

        }
    }
}
