package com.example.jason.route_application.di.utilities;

import com.example.jason.route_application.data.models.AddressFormatter;
import com.example.jason.route_application.di.AppScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Jason on 08-Feb-18.
 */

@Module
public class AddressFormatterModule {

    @Provides
    @AppScope
    public AddressFormatter provideAddressFormatter(){
        return new AddressFormatter();
    }

}
