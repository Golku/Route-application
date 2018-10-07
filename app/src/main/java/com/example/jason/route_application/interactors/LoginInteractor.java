package com.example.jason.route_application.interactors;

import com.example.jason.route_application.data.database.DatabaseCallback;
import com.example.jason.route_application.data.database.DatabaseService;
import com.example.jason.route_application.data.pojos.database.LoginResponse;
import com.example.jason.route_application.features.login.MvpLogin;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginInteractor implements MvpLogin.Interactor{

    private DatabaseService databaseService;

    @Inject
    LoginInteractor(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public void loginRequest(final String username,
            final String password,
            final DatabaseCallback.LoginCallBack callBack) {

        Call<LoginResponse> call = databaseService.login(username, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                callBack.onLoginResponse(response.body());
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callBack.onLoginResponseFailure();
            }
        });
    }
}
