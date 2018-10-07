package com.example.jason.route_application.features.addressDetails;

import com.example.jason.route_application.interactors.AddressDetailsInteractor;
import dagger.Binds;
import dagger.Module;

/**
 * Created by Jason on 07-Feb-18.
 */

@Module
public abstract class AddressDetailsModule {

    @Binds
    public abstract MvpAddressDetails.View provideView(AddressDetailsActivity view);

    @Binds
    public abstract MvpAddressDetails.Presenter providePresenter(AddressDetailsPresenter presenter);

    @Binds
    public abstract MvpAddressDetails.Interactor provideInteractor(AddressDetailsInteractor interactor);

}
