package com.example.jason.route_application.data.models;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.example.jason.route_application.features.container.ContainerPresenter;
import android.location.Address;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationManager extends AppCompatActivity {

    private Context context;
    private ContainerPresenter callback;

    public LocationManager(Context context, ContainerPresenter callback) {
        this.context = context;
        this.callback = callback;
    }

    public void getUserLocation(){

        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);

        client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    getUserAddress(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            }
        });
    }

    private void getUserAddress(LatLng userLocation){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(userLocation.latitude, userLocation.longitude, 1);
            callback.updateUserLocation(addresses.get(0).getAddressLine(0), userLocation);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
