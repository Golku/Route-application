package com.example.jason.route_application.features.container;

import com.google.android.gms.maps.model.LatLng;
import com.example.jason.route_application.data.api.ApiCallback;
import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.Event;
import com.example.jason.route_application.data.pojos.RouteInfoHolder;
import com.example.jason.route_application.data.pojos.Session;
import com.example.jason.route_application.data.pojos.api.ChangeAddressRequest;
import com.example.jason.route_application.data.pojos.api.AddressRequest;
import com.example.jason.route_application.data.pojos.api.DriveRequest;
import com.example.jason.route_application.data.pojos.api.RemoveAddressRequest;

import android.content.Context;

/**
 * Created by Jason on 07-Feb-18.
 */

public interface MvpContainer {

    interface View{

        void setupFragments(RouteInfoHolder routeInfoHolder);

        void showFragment(int position);

        void updateDeliveryCompletion(int[] deliveryCompletion);

        void updateRouteEndTimeTv(String endTime);

        void postEvent(Event event);

        void showAddressDetails(Address address);

        void navigateToDestination(String address);

        void showLoginScreen();

        void showToast(String message);

        void closeActivity();
    }

    interface Presenter{

        void setVariables(Session session, Context context);

        void getContainer();

        void showAddressDialog();

        void getUserLocation();

        void updateUserLocation(String userAddress, LatLng userLocation);

        void logOut();

        void eventReceived(Event event);
    }

    interface Interactor{

        void containerRequest(String username, ApiCallback.ContainerResponseCallback callback);

        void addressRequest(AddressRequest request, ApiCallback.AddAddressCallback callback);

        void changeAddress(ChangeAddressRequest request, ApiCallback.AddressChangeCallback callback);

        void removeAddress(RemoveAddressRequest request);

        void driveRequest(DriveRequest request, ApiCallback.DriveResponseCallback callback);
    }
}
