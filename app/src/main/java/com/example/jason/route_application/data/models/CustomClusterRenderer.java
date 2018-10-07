package com.example.jason.route_application.data.models;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.example.jason.route_application.data.pojos.Address;
import android.content.Context;
import android.content.res.Resources;

import java.util.List;
import java.util.Map;

public class CustomClusterRenderer extends DefaultClusterRenderer<Address> {

    private final String debugTag = "debugTag";
    private final Context context;
    private List<Address> routeOrder;
    private Map<Address, Integer> arrivalTimes;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<Address> clusterManager, List<Address> routeOrder, Map<Address, Integer> arrivalTimes) {
        super(context, map, clusterManager);
        this.context = context;
        this.routeOrder = routeOrder;
        this.arrivalTimes = arrivalTimes;
    }

    @Override
    protected void onClusterItemRendered(Address address, Marker marker) {
        changeMarkerIcon(address);
    }

    public void changeMarkerIcon(Address address){

        String iconName;
        Marker marker = getMarker(address);

        if(address.isFetchingDriveInfo()){
            iconName = "time_ic";
        }else{

            if(address.isSelected()){

                int openingTime = address.getOpeningTime();
                int closingTime = address.getClosingTime();
                int arrivalTime = arrivalTimes.get(address);

                if(address.isBusiness() && openingTime>0 && closingTime>0){
                    if(arrivalTime > address.getOpeningTime() && arrivalTime < address.getClosingTime()){
                        iconName = "ic_pending_marker_"+String.valueOf(routeOrder.indexOf(address)+1);
                    }else{
                        iconName = "ic_invalid_marker_"+String.valueOf(routeOrder.indexOf(address)+1);
                    }
                }else{
                    iconName = "ic_pending_marker_"+String.valueOf(routeOrder.indexOf(address)+1);
                }

            }else{
                if(address.isBusiness()){
                    iconName = "business_ic";
                }else{
                    iconName = "home_ic";
                }
            }
        }

        if(address.isCompleted()){
            iconName = "ic_done_marker_"+String.valueOf(routeOrder.indexOf(address)+1);
        }

        if(address.isUserLocation()){
            iconName = "ic_marker_origin";
        }

        Resources res = context.getResources();
        int resID = res.getIdentifier(iconName, "drawable", context.getPackageName());
        marker.setIcon(BitmapDescriptorFactory.fromResource(resID));
    }
}