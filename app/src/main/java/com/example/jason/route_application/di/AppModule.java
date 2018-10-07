package com.example.jason.route_application.di;

import android.content.Context;

import com.example.jason.route_application.RouteApplication;
import com.example.jason.route_application.di.network.NetworkModule;
import com.example.jason.route_application.di.utilities.AddressFormatterModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Jason on 2/9/2018.
 */

@Module(includes = {NetworkModule.class, AddressFormatterModule.class})
public class AppModule {

    @AppScope
    @Provides
    public Context provideContext(RouteApplication application) {
        return application.getApplicationContext();
    }
}
