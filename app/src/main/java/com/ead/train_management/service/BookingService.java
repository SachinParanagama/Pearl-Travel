package com.ead.train_management.service;

import com.ead.train_management.models.booking;
import com.ead.train_management.models.disable;
import com.ead.train_management.models.login;
import com.ead.train_management.models.loginRes;
import com.ead.train_management.models.rmBooking;
import com.ead.train_management.models.train;
import com.ead.train_management.models.user;
import com.ead.train_management.models.userRes;
import com.ead.train_management.models.viewBooking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Booking service
 */
public interface BookingService {

    @GET("api/Reservation/{id}")
    Call<List<viewBooking>> getBooking(@Path("id") String nic);

    @POST("api/Reservation")
    Call<String> createBooking(@Body booking u);

    @PUT("api/Reservation/{id}")
    Call<String> removeBooking(@Path("id") String id ,@Body rmBooking db);

    @GET("api/Train")
    Call<List<train>> getTrain();


}


