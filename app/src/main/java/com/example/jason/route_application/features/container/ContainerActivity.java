package com.example.jason.route_application.features.container;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jason.route_application.R;
import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.Event;
import com.example.jason.route_application.data.pojos.RouteInfoHolder;
import com.example.jason.route_application.data.pojos.Session;
import com.example.jason.route_application.features.addressDetails.AddressDetailsActivity;
import com.example.jason.route_application.features.container.addressListFragment.AddressListFragment;
import com.example.jason.route_application.features.container.driveListFragment.DriveListFragment;
import com.example.jason.route_application.features.container.mapFragment.MapFragment;
import com.example.jason.route_application.features.login.LoginActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ContainerActivity extends DaggerAppCompatActivity implements MvpContainer.View {

    @Inject
    MvpContainer.Presenter presenter;
    @BindView(R.id.loading_screen)
    ConstraintLayout loadingScreen;
    @BindView(R.id.menu_btn_wrapper)
    ConstraintLayout menuBtnWrapper;
    @BindView(R.id.menu_wrapper)
    ConstraintLayout menuWrapper;
    @BindView(R.id.nav_bar)
    BottomNavigationView navBar;
    @BindView(R.id.snack_bar_container)
    CoordinatorLayout snackBarContainer;
    @BindView(R.id.address_input_btn)
    TextView addressInputBtn;
    @BindView(R.id.get_user_location_btn)
    TextView getUserLocationBtn;
    @BindView(R.id.refresh_info_btn)
    TextView refreshInfoBtn;
    @BindView(R.id.log_out_btn)
    TextView logOutBtn;
    @BindView(R.id.fragment_container)
    ViewPager fragmentContainer;
    @BindView(R.id.route_end_time)
    TextView routeEndTime;

    private final String debugTag = "debugTag";

    private boolean menuIsVisible;

    private boolean backPress = false;

    @Override
    public void onBackPressed() {
        if (backPress) {
            closeActivity();
        } else {
            backPress = true;
            onBackPressToast();
        }
    }

    private void onBackPressToast() {
        Snackbar snackbar = Snackbar.make(snackBarContainer, "Press again to exit", Snackbar.LENGTH_SHORT);
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                backPress = false;
            }
        });
        snackbar.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_container);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        loadingScreen.bringToFront();
        presenter.setVariables(new Session(this), this);
        presenter.getContainer();

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (menuIsVisible) {
                    hideMenu();
                }

                switch(item.getItemId()){

                    case R.id.nav_address:
                        fragmentContainer.setCurrentItem(0);
                        return true;
                    case R.id.nav_map:
                        fragmentContainer.setCurrentItem(1);
                        return true;
                    case R.id.nav_route:
                        fragmentContainer.setCurrentItem(2);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void setupFragments(RouteInfoHolder routeInfoHolder) {

        Bundle bundle = new Bundle();
        bundle.putParcelable("routeInfoHolder", routeInfoHolder);

        Fragment addressListFragment = new AddressListFragment();
        Fragment MapFragment = new MapFragment();
        Fragment routeListFragment = new DriveListFragment();

        addressListFragment.setArguments(bundle);
        MapFragment.setArguments(bundle);
        routeListFragment.setArguments(bundle);

        ContainerSectionPagerAdapter containerSectionPagerAdapter = new ContainerSectionPagerAdapter(getSupportFragmentManager());
        containerSectionPagerAdapter.addFragment(addressListFragment);
        containerSectionPagerAdapter.addFragment(MapFragment);
        containerSectionPagerAdapter.addFragment(routeListFragment);

        fragmentContainer.setAdapter(containerSectionPagerAdapter);
        loadingScreen.setVisibility(View.GONE);
    }

    @Override
    public void showFragment(int position) {
        navBar.setSelectedItemId(R.id.nav_map);
        fragmentContainer.setCurrentItem(position);
    }

    @OnClick(R.id.menu_btn_wrapper)
    public void showMenuBtnClick() {

        if (menuIsVisible) {
            hideMenu();
        } else {
            switch (fragmentContainer.getCurrentItem()) {
                case 0:
                    getUserLocationBtn.setVisibility(View.GONE);
                    addressInputBtn.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    addressInputBtn.setVisibility(View.GONE);
                    getUserLocationBtn.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    addressInputBtn.setVisibility(View.GONE);
                    getUserLocationBtn.setVisibility(View.GONE);
                    break;
            }
            showMenu();
        }
    }

    private void showMenu() {
        menuWrapper.setVisibility(View.VISIBLE);
        menuWrapper.bringToFront();
        menuIsVisible = true;
        menuBtnWrapper.setBackgroundResource(R.drawable.drop_menu_btn_selected);
    }

    private void hideMenu() {
        menuWrapper.setVisibility(View.GONE);
        menuIsVisible = false;
        menuBtnWrapper.setBackgroundResource(R.drawable.drop_menu_btn);
    }

    @OnClick({R.id.address_input_btn, R.id.get_user_location_btn, R.id.refresh_info_btn, R.id.log_out_btn})
    public void menuBtnClick(View view) {
        hideMenu();
        switch (view.getId()) {
            case R.id.address_input_btn:
                presenter.showAddressDialog();
                break;
            case R.id.get_user_location_btn:
                ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
                presenter.getUserLocation();
                break;
            case R.id.refresh_info_btn:
                presenter.getContainer();
                break;
            case R.id.log_out_btn:
                presenter.logOut();
                break;
        }
    }

    @Override
    public void updateDeliveryCompletion(int[] deliveryCompletion) {
//        privateCompletion.setText(String.valueOf(deliveryCompletion[0]) + "/" + String.valueOf(deliveryCompletion[1]) + " delivered");
//        businessCompletion.setText(String.valueOf(deliveryCompletion[2]) + "/" + String.valueOf(deliveryCompletion[3]) + " delivered");
    }

    @Override
    public void updateRouteEndTimeTv(String endTime) {
        routeEndTime.setText(endTime);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEvent(Event event) {
        presenter.eventReceived(event);
    }

    @Override
    public void postEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    @Override
    public void showAddressDetails(Address address) {
        Intent i = new Intent(this, AddressDetailsActivity.class);
        i.putExtra("address", address);
        startActivity(i);
    }

    @Override
    public void navigateToDestination(String address) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + address));
        startActivity(intent);
    }

    @Override
    public void showLoginScreen() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    public void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void closeActivity() {
        EventBus.getDefault().unregister(this);
        finish();
    }
}