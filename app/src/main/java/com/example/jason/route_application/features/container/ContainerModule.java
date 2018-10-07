package com.example.jason.route_application.features.container;

import com.example.jason.route_application.interactors.ContainerInteractor;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Jason on 07-Feb-18.
 */

@Module
public abstract class ContainerModule {

    @Binds
    public abstract MvpContainer.View provideView(ContainerActivity view);

    @Binds
    public abstract MvpContainer.Presenter providePresenter(ContainerPresenter presenter);

    @Binds
    public abstract MvpContainer.Interactor provideInteractor(ContainerInteractor interactor);
}
