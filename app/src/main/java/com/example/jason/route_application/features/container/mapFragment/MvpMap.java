package com.example.jason.route_application.features.container.mapFragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.Event;

import android.content.Context;

/**
 * Created by Jason on 3/28/2018.
 */

public interface MvpMap {

    interface View{

        void deselectedMultipleMarkers();

        void postEvent(Event event);

        void getPolylineToMarker(LatLng start, LatLng end);

        void removePolyLine();

        void showToast(String message);
    }

    interface Presenter{

        void setMapData(GoogleMap googleMap, Context context);

        void multipleMarkersDeselected();

        void eventReceived(Event event);
    }

}
