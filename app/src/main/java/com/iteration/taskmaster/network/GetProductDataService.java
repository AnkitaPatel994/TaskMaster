package com.iteration.taskmaster.network;

import com.iteration.taskmaster.model.CompanyList;
import com.iteration.taskmaster.model.Message;
import com.iteration.taskmaster.model.ViewTask;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GetProductDataService {

    @FormUrlEncoded
    @POST("json_android/addcompany.php")
    Call<Message> getAddCompanyData(@Field("c_name") String c_name);

    @GET("json_android/viewcompany.php")
    Call<CompanyList> getCompanyData();

    @FormUrlEncoded
    @POST("json_android/addtask.php")
    Call<Message> getAddTaskData(@Field("t_name") String t_name,
                                 @Field("t_des") String t_des,
                                 @Field("t_due_date") String t_due_date,
                                 @Field("t_status") String t_status);

    @FormUrlEncoded
    @POST("json_android/viewtask.php")
    Call<ViewTask> getViewtTaskListData(@Field("t_status") String t_status);

    @FormUrlEncoded
    @POST("json_android/updatetask.php")
    Call<Message> getUpdateTaskListData(@Field("t_id") String t_id,
                                        @Field("t_status") String t_status);

}
