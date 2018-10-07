package com.example.jason.route_application.interactors;

import com.example.jason.route_application.data.api.ApiCallback;
import com.example.jason.route_application.data.api.ApiService;
import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.api.ChangeAddressRequest;
import com.example.jason.route_application.data.pojos.api.AddressRequest;
import com.example.jason.route_application.data.pojos.api.Container;
import com.example.jason.route_application.data.pojos.api.Drive;
import com.example.jason.route_application.data.pojos.api.DriveRequest;
import com.example.jason.route_application.data.pojos.api.RemoveAddressRequest;
import com.example.jason.route_application.features.container.MvpContainer;

import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jason on 08-Feb-18.
 */

public class ContainerInteractor implements MvpContainer.Interactor{

    private ApiService apiService;

    @Inject
    public ContainerInteractor(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void containerRequest(String username, final ApiCallback.ContainerResponseCallback callback) {
        Call<Container> call = apiService.containerRequest(username);

        call.enqueue(new Callback<Container>() {
            @Override
            public void onResponse(Call<Container> call, Response<Container> response) {
                callback.containerResponse(response.body());
            }

            @Override
            public void onFailure(Call<Container> call, Throwable t) {
                callback.containerResponseFailure();
            }
        });
    }

    @Override
    public void addressRequest(AddressRequest request, final ApiCallback.AddAddressCallback callback) {
        Call<Address> call = apiService.addressRequest(request);

        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                callback.addressResponse(response.body());
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                callback.addressResponseFailure();
            }
        });
    }

    @Override
    public void changeAddress(ChangeAddressRequest request, final ApiCallback.AddressChangeCallback callback) {
        Call<Address> call = apiService.changeAddressRequest(request);

        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                callback.addressChangeResponse(response.body());
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                callback.addressChangeFailure();
            }
        });
    }

    @Override
    public void removeAddress(RemoveAddressRequest request) {
        Call<Void> call = apiService.removeAddressRequest(request);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    public void driveRequest(DriveRequest request, final ApiCallback.DriveResponseCallback callback) {
        Call<Drive> call = apiService.driveRequest(request);

        call.enqueue(new Callback<Drive>() {
            @Override
            public void onResponse(Call<Drive> call, Response<Drive> response) {
                callback.driveResponse(response.body());
            }

            @Override
            public void onFailure(Call<Drive> call, Throwable t) {
                callback.driveResponseFailure();
            }
        });
    }
}
