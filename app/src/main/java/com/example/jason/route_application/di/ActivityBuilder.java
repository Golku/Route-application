package com.example.jason.route_application.di;

import com.example.jason.route_application.features.commentInput.CommentInputActivity;
import com.example.jason.route_application.features.commentInput.CommentInputModule;
import com.example.jason.route_application.features.addressDetails.AddressDetailsActivity;
import com.example.jason.route_application.features.addressDetails.AddressDetailsModule;
import com.example.jason.route_application.features.login.LoginActivity;
import com.example.jason.route_application.features.login.LoginModule;
import com.example.jason.route_application.features.container.ContainerActivity;
import com.example.jason.route_application.features.container.ContainerModule;
import com.example.jason.route_application.features.splash.SplashActivity;
import com.example.jason.route_application.features.splash.SplashModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Jason on 12-Feb-18.
 */

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = SplashModule.class)
    abstract SplashActivity bindSplash();

    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity bindLogin();

    @ContributesAndroidInjector(modules = ContainerModule.class)
    abstract ContainerActivity bindContainer();

    @ContributesAndroidInjector(modules = AddressDetailsModule.class)
    abstract AddressDetailsActivity bindAddressDetails();

    @ContributesAndroidInjector(modules = CommentInputModule.class)
    abstract CommentInputActivity bindCommentInput();
}
