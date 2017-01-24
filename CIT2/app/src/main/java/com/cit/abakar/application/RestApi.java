package com.cit.abakar.application;


import com.cit.abakar.application.database.Center;
import com.cit.abakar.application.database.Equipment;
import com.cit.abakar.application.database.Inspection;
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

public interface RestApi {

    @GET("centers/")
    Call<Response<List<Center>>> getAllCenters();

    @GET("condition/")
    Call<Response<List<String>>> getConditios();

    @GET("equipment/")
    Call<Response<List<Equipment>>> getEquipment();

    @GET("visit/")
    Call<Response<List<Visit>>> getVisits();

    @POST("visit/")
    Call<Response<ResponseBody>> addVisit(Visit visit);

    @POST("inspection/")
    Call<Response<ResponseBody>> addInspection(Inspection inspection);

    @POST("malfunction/")
    Call<Response<ResponseBody>> addMalfunction(Malfunctions malfunctions);

    



}
