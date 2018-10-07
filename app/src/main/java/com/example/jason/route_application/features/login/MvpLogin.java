package com.example.jason.route_application.features.login;

import com.example.jason.route_application.data.database.DatabaseCallback;
import com.example.jason.route_application.data.pojos.Session;

public interface MvpLogin {

    interface View{

        Session getSession();

        void onLoginBtnClick();

        void showContainer();

        void showToast(String message);

        void finishNetworkOperation();

        void closeActivity();
    }

    interface Presenter{

        void loginBtnClick(String username, String password);

    }

    interface Interactor{

        void loginRequest(String username, String password, DatabaseCallback.LoginCallBack callBack);

    }

}
