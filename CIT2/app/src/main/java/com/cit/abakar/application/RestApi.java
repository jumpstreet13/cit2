package com.cit.abakar.application;


import com.cit.abakar.application.ExampleClasses.Center;
import com.cit.abakar.application.ExampleClasses.Condition;
import com.cit.abakar.application.ExampleClasses.Dismantling;
import com.cit.abakar.application.ExampleClasses.Equipment;
import com.cit.abakar.application.ExampleClasses.Inspection;
import com.cit.abakar.application.ExampleClasses.Installation;
import com.cit.abakar.application.ExampleClasses.Malfunction;
import com.cit.abakar.application.ExampleClasses.Visit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApi {

    @GET("centers/")
    Call<List<Center>> getAllCenters();

    @GET("condition/")
    Call<List<Condition>> getConditios();

    @GET("equipment/")
    Call<List<Equipment>> getEquipment();

    @GET("visit/")
    Call<Response<List<Visit>>> getVisits();

    @GET("visit/")
    Call<Response<List<Visit>>> getVisitsByDate(@Path("id") String id, @Path("date_visit") String date);


    @POST("visit/")
    Call<Void> addVisit(@Body Visit visit);

    @POST("inspection/")
    Call<Void> addInspection(@Body Inspection inspection);

    @POST("malfunction/")
    Call<Void> addMalfunction(@Body Malfunction malfunctions);

    @POST("installation/")
    Call<Void> addInstallation(@Body Installation installation);

    @POST("dismantling/")
    Call<Void> addDismantling(@Body Dismantling dismantling);

}
