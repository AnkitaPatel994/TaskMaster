package com.iteration.taskmaster.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iteration.taskmaster.R;
import com.iteration.taskmaster.activity.MoreDetailActivity;
import com.iteration.taskmaster.activity.ViewAllTaskActivity;
import com.iteration.taskmaster.model.Task;

import java.util.ArrayList;

public class ViewTaskListAdapter extends RecyclerView.Adapter<ViewTaskListAdapter.ViewHolder> {

    Context context;
    ArrayList<Task> viewTaskListArray;

    public ViewTaskListAdapter(Context context, ArrayList<Task> viewAllTaskListArray) {
        this.context = context;
        this.viewTaskListArray = viewAllTaskListArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_task_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        String task_id = viewTaskListArray.get(position).getT_id();
        final String task_name = viewTaskListArray.get(position).getT_name();
        final String task_des = viewTaskListArray.get(position).getT_des();
        final String task_i_date = viewTaskListArray.get(position).getT_i_date();
        final String task_due_date = viewTaskListArray.get(position).getT_due_date();
        final String task_c_date = viewTaskListArray.get(position).getT_c_date();
        String task_status = viewTaskListArray.get(position).getT_status();

        viewHolder.txtVTTaskName.setText(task_name);
        viewHolder.txtVTTaskDescription.setText(task_des);
        viewHolder.txtVTTaskCreateDate.setText(task_i_date);
        viewHolder.txtVTTaskDueDate.setText(task_due_date);
        viewHolder.txtVTTaskCompleteDate.setText(task_c_date);
        viewHolder.txtVTTaskStatus.setText(task_status);

        if (task_status.equals("Pending"))
        {
            viewHolder.llVTTaskCompleteDate.setVisibility(View.GONE);
            viewHolder.txtVTTaskStatus.setTextColor(ContextCompat.getColor(context, R.color.colorYellow));
        }
        else if (task_status.equals("Complete"))
        {
            viewHolder.txtVTTaskStatus.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }

        viewHolder.txtVTReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, MoreDetailActivity.class);
                i.putExtra("task_name",task_name);
                i.putExtra("task_des",task_des);
                context.startActivity(i);
                /*final Dialog dialog_rm = new Dialog(context,android.R.style.Theme_Light_NoTitleBar);
                dialog_rm.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog_rm.setContentView(R.layout.readmore_dialog);
                dialog_rm.setCancelable(true);

                TextView txtRDTaskName = (TextView)dialog_rm.findViewById(R.id.txtRDTaskName);
                txtRDTaskName.setText(task_name);

                TextView txtRDescription = (TextView)dialog_rm.findViewById(R.id.txtRDescription);
                txtRDescription.setText(task_des);

                ImageView ivRDClose = (ImageView) dialog_rm.findViewById(R.id.ivRDClose);
                ivRDClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_rm.dismiss();
                    }
                });

                dialog_rm.show();*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return viewTaskListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtVTTaskName,txtVTTaskDescription,txtVTTaskCreateDate,txtVTTaskDueDate,txtVTTaskCompleteDate,txtVTTaskStatus,txtVTReadMore;
        LinearLayout llVTTaskCompleteDate;

        public ViewHolder(View itemView) {
            super(itemView);

            txtVTTaskName = (TextView)itemView.findViewById(R.id.txtVTTaskName);
            txtVTTaskDescription = (TextView)itemView.findViewById(R.id.txtVTTaskDescription);
            txtVTTaskCreateDate = (TextView)itemView.findViewById(R.id.txtVTTaskCreateDate);
            txtVTTaskDueDate = (TextView)itemView.findViewById(R.id.txtVTTaskDueDate);
            txtVTTaskCompleteDate = (TextView)itemView.findViewById(R.id.txtVTTaskCompleteDate);
            txtVTTaskStatus = (TextView)itemView.findViewById(R.id.txtVTTaskStatus);
            llVTTaskCompleteDate = (LinearLayout) itemView.findViewById(R.id.llVTTaskCompleteDate);
            txtVTReadMore = (TextView)itemView.findViewById(R.id.txtVTReadMore);

        }
    }
}
