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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        final String company_id = companyListArray.get(position).getC_id();
        final String company_name = companyListArray.get(position).getC_name();

        viewHolder.txtCompanyNameA.setText(company_name);

        viewHolder.llCompanyListA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,HomeActivity.class);
                i.putExtra("company_id",company_id);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return companyListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCompanyNameA;
        LinearLayout llCompanyListA;

        public ViewHolder(View itemView) {
            super(itemView);

            txtCompanyNameA = itemView.findViewById(R.id.txtCompanyNameA);
            llCompanyListA = itemView.findViewById(R.id.llCompanyListA);
        }
    }
}
