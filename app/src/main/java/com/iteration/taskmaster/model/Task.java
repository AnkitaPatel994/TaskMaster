package com.iteration.taskmaster.model;

import com.google.gson.annotations.SerializedName;

public class Task {

    @SerializedName("t_id")
    private String t_id;
    @SerializedName("t_name")
    private String t_name;
    @SerializedName("t_des")
    private String t_des;
    @SerializedName("t_i_date")
    private String t_i_date;
    @SerializedName("t_due_date")
    private String t_due_date;
    @SerializedName("t_c_date")
    private String t_c_date;
    @SerializedName("t_status")
    private String t_status;

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getT_name() {
        return t_name;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
    }

    public String getT_des() {
        return t_des;
    }

    public void setT_des(String t_des) {
        this.t_des = t_des;
    }

    public String getT_i_date() {
        return t_i_date;
    }

    public void setT_i_date(String t_i_date) {
        this.t_i_date = t_i_date;
    }

    public String getT_due_date() {
        return t_due_date;
    }

    public void setT_due_date(String t_due_date) {
        this.t_due_date = t_due_date;
    }

    public String getT_c_date() {
        return t_c_date;
    }

    public void setT_c_date(String t_c_date) {
        this.t_c_date = t_c_date;
    }

    public String getT_status() {
        return t_status;
    }

    public void setT_status(String t_status) {
        this.t_status = t_status;
    }
}
