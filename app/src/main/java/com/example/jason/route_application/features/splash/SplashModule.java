package com.example.jason.route_application.features.splash;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SplashModule {

    @Binds
    public abstract MvpSplash.View provideView(SplashActivity view);

    @Binds
    public abstract MvpSplash.Presenter providePresenter(SplashPresenter presenter);

}
