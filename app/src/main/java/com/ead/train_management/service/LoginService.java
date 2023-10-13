package com.ead.train_management.service;

import com.ead.train_management.models.disable;
import com.ead.train_management.models.login;
import com.ead.train_management.models.loginRes;
import com.ead.train_management.models.user;
import com.ead.train_management.models.userRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * User and AUth services
 */

public interface LoginService {

    @POST("Login")
    Call<loginRes> Login(@Body login lg);

    @GET("api/TravelerProfile/{id}")
    Call<userRes> getUserProfile(@Path("id") String nic);

    @POST("api/TravelerProfile")
    Call<userRes> Reg(@Body user u);

    @POST("api/TravelerProfile/")
    Call<userRes> Update(@Body userRes u);


    @PUT("api/TravelerProfile/{id}")
    Call<userRes> Dis(@Path("id") String nic ,@Body disable db);

}



