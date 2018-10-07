package com.example.jason.route_application.di.network;

import com.example.jason.route_application.data.api.ApiService;
import com.example.jason.route_application.data.database.DatabaseService;
import com.example.jason.route_application.di.AppScope;
import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jason on 08-Feb-18.
 */

@Module
public class    NetworkModule {

    @Provides
    @AppScope
    OkHttpClient provideOkHttpClient(){
        return new OkHttpClient();
    }

    @Provides
    @AppScope
    Gson provideGson (){
        return new Gson();
    }

    @Provides
    @AppScope
    @Named("api")
    Retrofit provideRetrofitForApi(OkHttpClient okHttpClient, Gson gson){
        return new Retrofit.Builder()
                .client(okHttpClient)//192.168.0.16 - 217.103.231.118
                .baseUrl("http://217.103.231.118:8080/webapi/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @AppScope
    @Named("database")
    Retrofit provideRetrofitForDatabase(OkHttpClient okHttpClient, Gson gson){
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://217.103.231.118/map/v1/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @AppScope
    @Provides
    ApiService provideApiService(@Named("api") Retrofit retrofit){
        return retrofit.create(ApiService.class);
    }

    @AppScope
    @Provides
    DatabaseService provideDatabaseService(@Named("database") Retrofit retrofit){
        return retrofit.create(DatabaseService.class);
    }

}
