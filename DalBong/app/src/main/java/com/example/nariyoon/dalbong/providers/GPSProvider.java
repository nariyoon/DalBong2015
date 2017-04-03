package com.example.nariyoon.dalbong.providers;

import android.app.Service;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by nari.yoon on 2017-03-31.
 */
@SuppressWarnings("MissingPermission")
public class GPSProvider extends LocationProvider {
    private LocationManager mLocationManager;
    private String mProvider = LocationManager.GPS_PROVIDER;
    private LocationListener mListener;

    public GPSProvider(Context context) {
        super();
        mLocationManager = (LocationManager)context.getSystemService(Service.LOCATION_SERVICE);
    }

    @Override
    public void unregisterLocationListener() {
        mLocationManager.removeUpdates(mListener);
    }

    @Override
    public void registerLocationListener(long minTime, float distance,
            final LocationChangeListener locationChangeListener) {
        getLastKnownLocation(locationChangeListener);
        requestLocationUpdates(minTime, distance, locationChangeListener);
    }

    private void getLastKnownLocation(final LocationChangeListener locationChangeListener) {
        locationChangeListener
                .onLastLocationReceived(mLocationManager.getLastKnownLocation(mProvider));
    }

    private void requestLocationUpdates(long minTime, float distance,
            final LocationChangeListener locationChangeListener) {

        mListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationChangeListener.onLocationChanged(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        mLocationManager.requestLocationUpdates(mProvider, minTime,
                distance, mListener);

    }
}
