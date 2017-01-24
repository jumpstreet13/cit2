package com.cit.abakar.application;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class App extends Application {

    private static RestApi restApi;
    private Retrofit retrofit;


    @Override
    public void onCreate(){
        super.onCreate();


      //  retrofit = new Retrofit.Builder().baseUrl().addConverterFactory(GsonConverterFactory.create()).build();
        restApi = retrofit.create(RestApi.class);
    }

    public static RestApi getRestApi(){
        return  restApi;
    }

}
