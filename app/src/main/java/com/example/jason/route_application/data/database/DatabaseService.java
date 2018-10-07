package com.example.jason.route_application.data.database;

import com.example.jason.route_application.data.pojos.database.AddressInformationResponse;
import com.example.jason.route_application.data.pojos.database.AddressTypeResponse;
import com.example.jason.route_application.data.pojos.database.CommentInputResponse;
import com.example.jason.route_application.data.pojos.database.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Jason on 19-Feb-18.
 */

public interface DatabaseService {

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("getAddressInfo.php")
    Call<AddressInformationResponse> getAddressInformation(
            @Field("street_name") String street,
            @Field("post_code") String postCode,
            @Field("city") String city
    );

    @FormUrlEncoded
    @POST("changeAddressType.php")
    Call<AddressTypeResponse> changeAddressType(
            @Field("street_name") String street,
            @Field("post_code") String postCode,
            @Field("city") String city,
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("changeOpeningTime.php")
    Call<Void> changeOpeningTime(
            @Field("street_name") String street,
            @Field("post_code") String postCode,
            @Field("city") String city,
            @Field("opening_time") int openingTime
    );

    @FormUrlEncoded
    @POST("changeClosingTime.php")
    Call<Void> changeClosingTime(
            @Field("street_name") String street,
            @Field("post_code") String postCode,
            @Field("city") String city,
            @Field("closing_time") int closingTime
    );

    @FormUrlEncoded
    @POST("addAddressComment.php")
    Call<CommentInputResponse> addCommentToAddress(
            @Field("street_name") String street,
            @Field("post_code") String postCode,
            @Field("city") String city,
            @Field("username") String username,
            @Field("message") String comment,
            @Field("date") String date
    );
}
