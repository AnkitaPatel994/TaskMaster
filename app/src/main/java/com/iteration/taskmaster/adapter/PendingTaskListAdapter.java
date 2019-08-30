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
import android.widget.TextView;
import android.widget.Toast;

import com.iteration.taskmaster.R;
import com.iteration.taskmaster.activity.AddTaskActivity;
import com.iteration.taskmaster.activity.HomeActivity;
import com.iteration.taskmaster.model.Message;
import com.iteration.taskmaster.model.Task;
import com.iteration.taskmaster.network.GetProductDataService;
import com.iteration.taskmaster.network.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingTaskListAdapter extends RecyclerView.Adapter<PendingTaskListAdapter.ViewHolder> {

    Context context;
    ArrayList<Task> viewTaskListArray;

    public PendingTaskListAdapter(Context context, ArrayList<Task> viewAllTaskListArray) {
        this.context = context;
        this.viewTaskListArray = viewAllTaskListArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final String task_id = viewTaskListArray.get(position).getT_id();
        final String task_name = viewTaskListArray.get(position).getT_name();
        final String task_des = viewTaskListArray.get(position).getT_des();
        String task_due_date = viewTaskListArray.get(position).getT_due_date();
        String task_status = viewTaskListArray.get(position).getT_status();

        viewHolder.txtTaskNameA.setText(task_name);
        viewHolder.txtTaskDescriptionA.setText(task_des);
        viewHolder.txtTaskDueDateA.setText(task_due_date);
        viewHolder.txtTaskStatusA.setText(task_status);

        if (task_status.equals("Pending"))
        {
            viewHolder.txtTaskStatusA.setTextColor(ContextCompat.getColor(context, R.color.colorYellow));
        }
        else if (task_status.equals("Complete"))
        {
            viewHolder.txtTaskStatusA.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }

        viewHolder.txtReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog_rm = new Dialog(context,android.R.style.Theme_Light_NoTitleBar);
                dialog_rm.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog_rm.setContentView(R.layout.pending_dialog);
                dialog_rm.setCancelable(true);

                final GetProductDataService productDataService = RetrofitInstance.getRetrofitInstance().create(GetProductDataService.class);

                TextView txtPDTaskName = (TextView)dialog_rm.findViewById(R.id.txtPDTaskName);
                txtPDTaskName.setText(task_name);

                TextView txtPDescription = (TextView)dialog_rm.findViewById(R.id.txtPDescription);
                txtPDescription.setText(task_des);

                ImageView ivPDClose = (ImageView) dialog_rm.findViewById(R.id.ivPDClose);
                ivPDClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_rm.dismiss();
                    }
                });

                Button btnPDCTask = (Button) dialog_rm.findViewById(R.id.btnPDCTask);
                btnPDCTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog dialog = new ProgressDialog(context);
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
                                    Intent i = new Intent(context, HomeActivity.class);
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

    }

    @Override
    public int getItemCount() {
        return viewTaskListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTaskNameA,txtTaskDescriptionA,txtTaskDueDateA,txtTaskStatusA,txtReadMore;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTaskNameA = (TextView)itemView.findViewById(R.id.txtTaskNameA);
            txtTaskDescriptionA = (TextView)itemView.findViewById(R.id.txtTaskDescriptionA);
            txtTaskDueDateA = (TextView)itemView.findViewById(R.id.txtTaskDueDateA);
            txtTaskStatusA = (TextView)itemView.findViewById(R.id.txtTaskStatusA);
            txtReadMore = (TextView)itemView.findViewById(R.id.txtReadMore);
        }
    }
}