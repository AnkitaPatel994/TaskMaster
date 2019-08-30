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
import android.widget.TextView;
import com.iteration.taskmaster.R;
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
                .inflate(R.layout.task_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        String task_id = viewTaskListArray.get(position).getT_id();
        final String task_name = viewTaskListArray.get(position).getT_name();
        final String task_des = viewTaskListArray.get(position).getT_des();
        final String task_due_date = viewTaskListArray.get(position).getT_due_date();
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
                dialog_rm.show();
            }
        });

        viewHolder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                String body="Hello,\nYour Task Title is :  \""+task_name+"\"\n\nTask description is below,\n\""
                        +task_des+"\"\n\nDue Date is :  \""+task_due_date+"\"\n\nPlease Let me know when you done it.";
                i.putExtra(Intent.EXTRA_SUBJECT,body);
                i.putExtra(Intent.EXTRA_TEXT,body);
                i.setPackage("com.whatsapp");
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return viewTaskListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTaskNameA,txtTaskDescriptionA,txtTaskDueDateA,txtTaskStatusA,txtReadMore;
        ImageView ivShare;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTaskNameA = (TextView)itemView.findViewById(R.id.txtTaskNameA);
            txtTaskDescriptionA = (TextView)itemView.findViewById(R.id.txtTaskDescriptionA);
            txtTaskDueDateA = (TextView)itemView.findViewById(R.id.txtTaskDueDateA);
            txtTaskStatusA = (TextView)itemView.findViewById(R.id.txtTaskStatusA);
            txtReadMore = (TextView)itemView.findViewById(R.id.txtReadMore);
            ivShare = (ImageView) itemView.findViewById(R.id.ivShare);
        }
    }
}
