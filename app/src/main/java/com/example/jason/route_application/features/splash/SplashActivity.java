package com.example.jason.route_application.features.splash;

import com.example.jason.route_application.R;
import com.example.jason.route_application.data.pojos.Session;
import com.example.jason.route_application.features.container.ContainerActivity;
import com.example.jason.route_application.features.login.LoginActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class SplashActivity extends DaggerAppCompatActivity implements MvpSplash.View{

    @Inject
    MvpSplash.Presenter presenter;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.redirectUser(new Session(SplashActivity.this));
            }
        }, 2000);
    }

    @Override
    public void showLogin() {
        Intent i = new Intent (this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    public void showContainer() {
        Intent i = new Intent (this, ContainerActivity.class);
        startActivity(i);
    }

    @Override
    public void closeActivity(){
        finish();
    }
}
