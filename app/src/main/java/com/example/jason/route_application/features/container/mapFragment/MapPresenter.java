package com.example.jason.route_application.features.container.mapFragment;

import com.example.jason.route_application.data.models.CustomClusterRenderer;
import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.Event;
import com.example.jason.route_application.data.pojos.api.Drive;
import com.example.jason.route_application.data.pojos.api.DriveRequest;
import com.example.jason.route_application.features.shared.BasePresenter;
import com.example.jason.route_application.features.shared.MvpBasePresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason on 3/28/2018.
 */

public class MapPresenter extends BasePresenter implements
        MvpBasePresenter,
        MvpMap.Presenter,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener {

    private final String debugTag = "debugTag";

    private MvpMap.View view;

    private GoogleMap googleMap;
    private ClusterManager<Address> clusterManager;
    private CustomClusterRenderer renderer;

    private Address userLocation;
    private Address currentAddress;
    private Address previousSelectedAddress;
    private List<Address> addressList;
    private List<Address> routeOrder;
    private Map<Address, Integer> arrivalTime;

    MapPresenter(MvpMap.View view, List<Address> addressList) {
        this.view = view;
        this.addressList = addressList;
        routeOrder = new ArrayList<>();
        arrivalTime = new HashMap<>();

        userLocation = new Address();
        userLocation.setUserLocation(true);
        userLocation.setAddress("Vrij-Harnasch 21, Den Hoorn");
        userLocation.setLat(52.008234);
        userLocation.setLng(4.312999);
        userLocation.setUserLocation(true);

        previousSelectedAddress = userLocation;
    }

    @Override
    public void setMapData(GoogleMap googleMap, Context context) {

        clusterManager = new ClusterManager<>(context, googleMap);
        renderer = new CustomClusterRenderer(context, googleMap, clusterManager, routeOrder, arrivalTime);
        clusterManager.setRenderer(renderer);

        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);

        clusterManager.addItem(userLocation);

        for (Address address : addressList) {
            if (address.isValid()) {
                clusterManager.addItem(address);
            }
        }

        clusterManager.cluster();

        this.googleMap = googleMap;

        moveMapCamera(userLocation.getLat(), userLocation.getLng());
    }

    private void moveMapCamera(double lat, double lng) {
        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(lat, lng)).zoom(10f).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        createEvent("container", "itemClick", findAddress(marker.getTitle()), this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Address address = findAddress(marker.getTitle());

        if (address == null) {
            return false;
        }

        if (address.isSelected()) {

            if (previousSelectedAddress.equals(address)) {

                address.setSelected(false);
                address.setCompleted(false);
                routeOrder.remove(address);

                if (routeOrder.size() > 0) {
                    previousSelectedAddress = routeOrder.get(routeOrder.size() - 1);
                } else {
                    previousSelectedAddress = userLocation;
                }

                removeDrive();
            } else {
                currentAddress = address;
                view.deselectedMultipleMarkers();
            }
        } else {

            googleMap.setOnMarkerClickListener(null);
            currentAddress = address;
            address.setFetchingDriveInfo(true);
            getDrive(address);
        }

        renderer.changeMarkerIcon(address);
        marker.showInfoWindow();

        return true;
    }

    private void getDrive(Address address) {

        LatLng start;
        LatLng end;
        DriveRequest request = new DriveRequest();

        request.setOrigin(previousSelectedAddress.getAddress());
        start = new LatLng(previousSelectedAddress.getLat(), previousSelectedAddress.getLng());

        request.setDestination(address.getAddress());
        end = new LatLng(address.getLat(), address.getLng());

        createEvent("container", "getDrive", request, this);

        view.getPolylineToMarker(start, end);
    }

    private void removeDrive() {
        createEvent("driveFragment", "removeDrive", this);
        view.removePolyLine();
    }

    @Override
    public void multipleMarkersDeselected()  {

        int addressPosition = routeOrder.indexOf(currentAddress);

        for (int i = addressPosition; i < routeOrder.size(); i++) {
            Address address = routeOrder.get(i);
            address.setSelected(false);
            address.setCompleted(false);
            renderer.changeMarkerIcon(address);
        }

        routeOrder.subList(addressPosition, routeOrder.size()).clear();

        if (routeOrder.size() > 0) {
            previousSelectedAddress = routeOrder.get(routeOrder.size() - 1);
        } else {
            previousSelectedAddress = userLocation;
        }

        view.removePolyLine();

        createEvent("driveFragment", "RemoveMultipleDrive", currentAddress.getAddress(), this);
    }

    private Address findAddress(String addressString){
        Address address = null;
        for(Address it : addressList){
            if(it.getAddress().equals(addressString)){
                address = it;
                break;
            }
        }
        return address;
    }

    @Override
    public void publishEvent(Event event) {
        view.postEvent(event);
    }

    @Override
    public void eventReceived(Event event) {

        if (!(event.getReceiver().equals("mapFragment") || event.getReceiver().equals("all"))) {
            return;
        }

        Log.d(debugTag, "Event received on mapFragment: " + event.getEventName());

        switch (event.getEventName()) {
            case "updateList":
                updateMarkers(event.getAddressList());
                break;
            case "addressTypeChange":
                addressTypeChange(event.getAddress());
                break;
            case "showMarker":
                showMarker(event.getAddress());
                break;
            case "markAddress":
                addMarkerToMap(event.getAddress());
                break;
            case "removeMarker":
                removeMarkerFromMap(event.getAddress());
                break;
            case "driveSuccess":
                driveSuccess(event.getDrive());
                break;
            case "driveFailed":
                driveFailed();
                break;
            case "driveCompleted":
                driveCompleted(event.getAddress());
                break;
        }
    }

    private void updateMarkers(List<Address> addressList) {
        this.addressList = addressList;
        routeOrder.clear();
        arrivalTime.clear();
        clusterManager.clearItems();
        previousSelectedAddress = userLocation;
        view.removePolyLine();

        clusterManager.addItem(userLocation);

        for (Address address : addressList) {
            if (address.isValid()) {
                clusterManager.addItem(address);
            }
        }

        clusterManager.cluster();
        moveMapCamera(userLocation.getLat(), userLocation.getLng());
    }

    private void addressTypeChange(Address address) {
        renderer.changeMarkerIcon(findAddress(address.getAddress()));
    }

    private void showMarker(Address address) {
        if (address.isValid()) {
            Marker marker = renderer.getMarker(findAddress(address.getAddress()));
            moveMapCamera(address.getLat(), address.getLng());
            marker.showInfoWindow();
        }
    }

    private void addMarkerToMap(Address address) {
        if (address.isValid()) {
            if (address.isUserLocation()) {
                clusterManager.removeItem(userLocation);
                userLocation = address;
                if (routeOrder.size() == 0) {
                    previousSelectedAddress = address;
                }
            }
            clusterManager.addItem(address);
            clusterManager.cluster();
            moveMapCamera(address.getLat(), address.getLng());
        }
    }

    private void removeMarkerFromMap(Address address) {
        clusterManager.removeItem(address);
        clusterManager.cluster();

        if (address.isSelected()) {
            currentAddress = address;
            multipleMarkersDeselected();
        }
    }

    private void driveSuccess(Drive drive) {

        String[] deliveryTime = drive.getDeliveryTimeHumanReadable().split(":");
        String hourString = deliveryTime[0];
        String minuteString = deliveryTime[1];

        int hour = Integer.parseInt(hourString);
        int minute = Integer.parseInt(minuteString);

        arrivalTime.put(currentAddress, ((hour*60) + minute));

        currentAddress.setFetchingDriveInfo(false);
        currentAddress.setSelected(true);
        routeOrder.add(currentAddress);
        previousSelectedAddress = currentAddress;
        renderer.changeMarkerIcon(currentAddress);
        googleMap.setOnMarkerClickListener(this);
    }

    private void driveFailed() {
        currentAddress.setFetchingDriveInfo(false);
        renderer.changeMarkerIcon(currentAddress);
        view.removePolyLine();
        googleMap.setOnMarkerClickListener(this);
        view.showToast("Failed to get drive");
    }

    private void driveCompleted(Address address) {
        for (Address it: routeOrder) {
            if (it.getAddress().equals(address.getAddress())) {
                it.setCompleted(true);
                renderer.changeMarkerIcon(it);
                break;
            }
        }
    }
}