package com.example.jason.route_application.features.login;

import com.example.jason.route_application.data.database.DatabaseCallback;
import com.example.jason.route_application.data.pojos.database.LoginResponse;
import com.example.jason.route_application.features.shared.BasePresenter;

import javax.inject.Inject;

public class LoginPresenter extends BasePresenter implements MvpLogin.Presenter, DatabaseCallback.LoginCallBack{

    private final String debugTag = "debugTag";

    private MvpLogin.View view;
    private MvpLogin.Interactor interactor;

    private String username;

    @Inject
    LoginPresenter(MvpLogin.View view, MvpLogin.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void loginBtnClick(String username, String password) {

        this.username = username;

        //incription
        String encryptedUsername = encryptInput(username);
        String encryptedPassword = encryptInput(password);

        interactor.loginRequest(encryptedUsername, encryptedPassword, this);
    }

    private String encryptInput (String input){
        return input;
    }

    @Override
    public void onLoginResponse(LoginResponse response) {
        if (response == null) {return;}

        if(response.isMatch()){
            beginSession(username, view.getSession());
            view.showContainer();
            view.closeActivity();
        }else{
            view.finishNetworkOperation();
            view.showToast("No match");
        }
    }

    @Override
    public void onLoginResponseFailure() {
        view.finishNetworkOperation();
        view.showToast("Failed");
    }
}
