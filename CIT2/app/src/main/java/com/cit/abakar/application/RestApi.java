package com.cit.abakar.application;


import com.cit.abakar.application.database.Center;
import com.cit.abakar.application.database.Dismantling;
import com.cit.abakar.application.database.Equipment;
import com.cit.abakar.application.database.Inspection;
import com.cit.abakar.application.database.Installation;
import com.cit.abakar.application.database.Malfunctions;
import com.cit.abakar.application.database.Visit;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestApi {

    @GET("centers/")
    Call<Response<List<Center>>> getAllCenters();

    @GET("condition/")
    Call<Response<List<String>>> getConditios();

    @GET("equipment/")
    Call<Response<List<Equipment>>> getEquipment();

    @GET("visit/")
    Call<Response<List<Visit>>> getVisits();

    @GET("visit/")
    Call<Response<List<Visit>>> getVisitsByDate(@Path("id") String id, @Path("date_visit") String date);


    @POST("visit/")
    Call<Response<ResponseBody>> addVisit(@Body Visit visit);

    @POST("inspection/")
    Call<Response<ResponseBody>> addInspection(@Body Inspection inspection);

    @POST("malfunction/")
    Call<Response<ResponseBody>> addMalfunction(@Body Malfunctions malfunctions);

    @POST("installation/")
    Call<Response<ResponseBody>> addInstallation(@Body Installation installation);

    @POST("dismantling/")
    Call<Response<ResponseBody>> addDismantling(@Body Dismantling dismantling);


}
