package com.example.jason.route_application.features.container;

import com.google.android.gms.maps.model.LatLng;
import com.example.jason.route_application.data.api.ApiCallback;
import com.example.jason.route_application.data.models.LocationManager;
import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.Event;
import com.example.jason.route_application.data.pojos.RouteInfoHolder;
import com.example.jason.route_application.data.pojos.api.ChangeAddressRequest;
import com.example.jason.route_application.data.pojos.api.AddressRequest;
import com.example.jason.route_application.data.pojos.api.Container;
import com.example.jason.route_application.data.pojos.Session;
import com.example.jason.route_application.data.pojos.api.Drive;
import com.example.jason.route_application.data.pojos.api.DriveRequest;
import com.example.jason.route_application.data.pojos.api.RemoveAddressRequest;
import com.example.jason.route_application.features.shared.BasePresenter;
import com.example.jason.route_application.features.shared.MvpBasePresenter;
import android.content.Context;
import android.util.Log;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 07-Feb-18.
 */

public class ContainerPresenter extends BasePresenter implements
        MvpBasePresenter,
        MvpContainer.Presenter,
        ApiCallback.ContainerResponseCallback,
        ApiCallback.AddAddressCallback,
        ApiCallback.AddressChangeCallback,
        ApiCallback.DriveResponseCallback {

    private final String debugTag = "debugTag";

    private MvpContainer.View view;

    private MvpContainer.Interactor interactor;

    private Context context;
    private Session session;
    private Container container;

    private List<Address> addressList;
    private List<Drive> driveList;

    @Inject
    public ContainerPresenter(MvpContainer.View view, MvpContainer.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    //container data

    @Override
    public void setVariables(Session session, Context context) {
        this.session = session;
        this.context = context;
    }

    private void setupContainer(Container container) {
        this.container = container;

        setupAddressList();
        setupDriveList();
        updateContainerInfo();

        RouteInfoHolder routeInfoHolder = new RouteInfoHolder();
        routeInfoHolder.setAddressList(addressList);
        routeInfoHolder.setDriveList(driveList);

        view.setupFragments(routeInfoHolder);
    }

    private void updateContainer(Container container) {
        this.container = container;

        setupAddressList();
        setupDriveList();
        updateContainerInfo();
        updateRouteEndTime();

        createEvent("all", "updateList", addressList, driveList, this);
    }

    private void setupAddressList() {
        addressList = new ArrayList<>();

        if (container.getAddressList() != null) {
            addressList.addAll(container.getAddressList());
        }
    }

    private void setupDriveList() {
        driveList = new ArrayList<>();

        if (container.getDriveList() != null) {
            driveList.addAll(container.getDriveList());
        }

//        List<String> routeOrder = new ArrayList<>();
//        for (Drive drive : driveList) {
//            routeOrder.add(drive.getDestinationAddress().getAddress());
//        }
    }

    private void updateContainerInfo() {

        int privateAddressCount = container.getPrivateAddressCount();
        int businessAddressCount = container.getBusinessAddressCount();
        int invalidAddressCount = container.getInvalidAddressCount();

//        In this method update the container by loading the values that are displayed in the top information bar

//        deliveryCompletion[0] = deliveredPrivate;
//        deliveryCompletion[2] = deliveredBusiness;
//        deliveryCompletion[1] = route.getPrivateAddressCount();
//        deliveryCompletion[3] = route.getBusinessAddressCount();
//        view.updateDeliveryCompletion(deliveryCompletion);
    }

    private void updateRouteEndTime() {
        if (driveList.size() > 0) {
            Drive finalDrive = driveList.get(driveList.size() - 1);
            view.updateRouteEndTimeTv(finalDrive.getDeliveryTimeHumanReadable());
        } else {
            view.updateRouteEndTimeTv("");
        }
    }

    @Override
    public void updateUserLocation(String userAddress, LatLng userLocation) {
        Address address = new Address();
        address.setValid(true);
        address.setAddress(userAddress);
        address.setLat(userLocation.latitude);
        address.setLng(userLocation.longitude);
        address.setUserLocation(true);
        createEvent("mapFragment", "markAddress", address ,this);
    }

    //menu bottoms

    @Override
    public void getUserLocation() {
        LocationManager locationManager = new LocationManager(context, this);
        locationManager.getUserLocation();
    }

    @Override
    public void logOut() {
        endSession(session);
        view.showLoginScreen();
        view.closeActivity();
    }

    @Override
    public void showAddressDialog() {
        createEvent("addressFragment", "showDialog", this);
    }

    //fragment interaction

    @Override
    public void publishEvent(Event event) {
        view.postEvent(event);
    }

    @Override
    public void eventReceived(Event event) {

        if (!event.getReceiver().equals("container")) {
            return;
        }

        Log.d(debugTag, "Event received on container: "+ event.getEventName());

        switch (event.getEventName()) {
            case "updateContainer":
                getContainer();
                break;
            case "itemClick":
                view.showAddressDetails(event.getAddress());
                break;
            case "showMap":
                showMap();
                break;
            case "getAddress":
                getAddress(event.getAddressString());
                break;
            case "changeAddress":
                changeAddress(event.getChangeAddressRequest());
                break;
            case "removeAddress":
                removeAddress(event.getAddress());
                break;
            case "getDrive":
                getDrive(event.getDriveRequest());
                break;
            case "updateEndTime":
                updateRouteEndTime();
                break;
            case "driveDirections":
                view.navigateToDestination(event.getAddressString());
                break;
        }
    }

    private void showMap() {
        view.showFragment(1);
    }

    private void addAddress(Address address) {
        createEvent("addressFragment", "addAddress", address, this);
    }

    private void replaceAddress(Address address) {
        createEvent("addressFragment","replaceAddress", address, this);
    }

    private void addDrive(Drive drive) {
        if(drive != null && drive.isValid()){
            createEvent("driveFragment", "addDrive", drive, this);
        }else{
            createEvent("mapFragment", "driveFailed",this);
        }
    }

    //interactor request

    @Override
    public void getContainer() {
        interactor.containerRequest(session.getUsername(), this);
    }

    private void getAddress(String address) {
        AddressRequest request = new AddressRequest(session.getUsername(), address);
        interactor.addressRequest(request, this);
    }

    private void changeAddress(ChangeAddressRequest request) {
        request.setUsername(session.getUsername());
        interactor.changeAddress(request, this);
    }

    private void removeAddress(Address address) {
        RemoveAddressRequest request = new RemoveAddressRequest();
        request.setUsername(session.getUsername());
        request.setAddress(address.getAddress());
        interactor.removeAddress(request);
    }

    private void getDrive(DriveRequest request) {
        interactor.driveRequest(request, this);
    }

    //interactor response

    //If the server has an error and sends back a routeResponse with a html page
    //the response processing will fail! FIX THIS!!!

    @Override
    public void containerResponse(Container response) {
        if (response != null) {
            if (container != null) {
                updateContainer(response);
            } else {
                setupContainer(response);
            }
        }
    }

    @Override
    public void containerResponseFailure() {
        view.showToast("Unable to fetch container from api");
    }

    @Override
    public void addressResponse(Address response) {
        if (response != null) {
            addAddress(response);
        }
    }

    @Override
    public void addressResponseFailure() {
        view.showToast("Unable to fetch address from api");
    }

    @Override
    public void addressChangeResponse(Address response) {
        if (response != null) {
            replaceAddress(response);
        }
    }

    @Override
    public void addressChangeFailure() {
        view.showToast("Unable to change address");
    }

    @Override
    public void driveResponse(Drive response) {
        addDrive(response);
    }

    @Override
    public void driveResponseFailure() {
        addDrive(null);
    }
}