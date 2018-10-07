package com.example.jason.route_application;

import com.example.jason.route_application.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

/**
 * Created by Jason on 08-Feb-18.
 */

public class RouteApplication extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}