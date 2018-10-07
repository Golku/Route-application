package com.example.jason.route_application.di;

import com.example.jason.route_application.RouteApplication;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by Jason on 07-Feb-18.
 */
@AppScope
@Component(modules = {AndroidSupportInjectionModule.class, ActivityBuilder.class, AppModule.class})
public interface  AppComponent extends AndroidInjector<RouteApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(RouteApplication application);

        AppComponent build();
    }
}