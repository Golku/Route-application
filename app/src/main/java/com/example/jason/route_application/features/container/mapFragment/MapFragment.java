package com.example.jason.route_application.features.container.mapFragment;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.jason.route_application.R;
import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.Event;
import com.example.jason.route_application.data.pojos.RouteInfoHolder;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 3/22/2018.
 */

public class MapFragment extends Fragment implements
        MvpMap.View,
        OnMapReadyCallback,
        RoutingListener {

    @BindView(R.id.mapView)
    MapView mapView;

    @BindView(R.id.snack_bar_container)
    CoordinatorLayout snackBarContainer;

    private final String debugTag = "debugTag";

    private MvpMap.Presenter presenter;

    private GoogleMap googleMap;

    private List<Polyline> polylines;

    private static final int[] COLORS = new int[]{R.color.colorAccent};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        RouteInfoHolder routeInfoHolder = getArguments().getParcelable("routeInfoHolder");

        presenter = new MapPresenter(this,
                routeInfoHolder.getAddressList());

        polylines = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapView mapView = view.findViewById(R.id.mapView);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        this.googleMap = googleMap;
        presenter.setMapData(googleMap, getContext());
    }

    @Override
    public void deselectedMultipleMarkers() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage("Are you sure?")
//                .setTitle("Deselect multiple markers")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        presenter.multipleMarkersDeselected();
//                    }
//                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User cancelled the dialog
//            }
//        });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();

        Snackbar snackbar = Snackbar.make(snackBarContainer, "Deselect until here?", Snackbar.LENGTH_SHORT);
        snackbar.setAction("yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.multipleMarkersDeselected();
            }
        });
        snackbar.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEvent(Event event) {
        presenter.eventReceived(event);
    }

    @Override
    public void postEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    //PolyLines

    @Override
    public void getPolylineToMarker(LatLng start, LatLng end) {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(start, end)
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
//            showToast(e.getMessage());
//            showToast("polyline error");
        } else {
//            showToast("Routing failed");
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = googleMap.addPolyline(polyOptions);
            polylines.add(polyline);

//            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void removePolyLine() {
        for (Polyline polyline : polylines) {
            polyline.remove();
        }
        polylines.clear();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
