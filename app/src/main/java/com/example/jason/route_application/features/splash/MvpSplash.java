package com.example.jason.route_application.features.splash;

import com.example.jason.route_application.data.pojos.Session;

public interface MvpSplash {

    interface View{

        void showContainer();

        void showLogin();

        void closeActivity();
    }

    interface Presenter{

        void redirectUser(Session session);
    }

}
