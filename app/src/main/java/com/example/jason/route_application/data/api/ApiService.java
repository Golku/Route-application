package com.example.jason.route_application.data.api;

import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.api.ChangeAddressRequest;
import com.example.jason.route_application.data.pojos.api.AddressRequest;
import com.example.jason.route_application.data.pojos.api.Container;
import com.example.jason.route_application.data.pojos.api.Drive;
import com.example.jason.route_application.data.pojos.api.DriveRequest;
import com.example.jason.route_application.data.pojos.api.RemoveAddressRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Jason on 14-Feb-18.
 */

public interface ApiService {

    @GET("container/{username}")
    Call<Container> containerRequest(@Path("username")String username);

    @POST("address")
    Call<Address> addressRequest(@Body AddressRequest request);

    @POST("changeaddress")
    Call<Address> changeAddressRequest(@Body ChangeAddressRequest request);

    @POST("removeaddress")
    Call<Void> removeAddressRequest(@Body RemoveAddressRequest request);

    @POST("drive")
    Call<Drive> driveRequest(@Body DriveRequest request);
}
