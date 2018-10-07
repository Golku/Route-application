package com.example.jason.route_application.data.api;

import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.api.Container;
import com.example.jason.route_application.data.pojos.api.Drive;

/**
 * Created by Jason on 14-Feb-18.
 */

public interface ApiCallback {

    interface ContainerResponseCallback {
        void containerResponse(Container response);
        void containerResponseFailure();
    }

    interface AddAddressCallback {
        void addressResponse(Address response);
        void addressResponseFailure();
    }

    interface AddressChangeCallback {
        void addressChangeResponse(Address response);
        void addressChangeFailure();

    }

    interface DriveResponseCallback {
        void driveResponse(Drive response);
        void driveResponseFailure();

    }
}
