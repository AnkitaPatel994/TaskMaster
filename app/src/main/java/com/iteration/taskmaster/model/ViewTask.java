package com.iteration.taskmaster.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ViewTask {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("Task")
    private ArrayList<Task> taskList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }
}
