package com.example.nariyoon.dalbong.providers;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by nari.yoon on 2017-03-31.
 */
@SuppressWarnings("MissingPermission")
public class FusedAPIProvider extends LocationProvider
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = FusedAPIProvider.class.getSimpleName();

    private Context mContext;
    private GoogleApiClient mClient;
    LocationListener mListener;
    LocationRequest mRequest;
    LocationChangeListener mCustomLocationChangeListener;

    public FusedAPIProvider(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void registerLocationListener(long minTime, float distance,
                                         LocationChangeListener locationChangeListener) {
        setClient();
        if (mClient != null) {
            mClient.connect();
        }

        mRequest = new LocationRequest();
        mRequest.setFastestInterval(minTime);
        mRequest.setSmallestDisplacement(distance);

        mCustomLocationChangeListener = locationChangeListener;
    }

    @Override
    public void unregisterLocationListener() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mClient, mListener);

        if (mClient != null) {
            mClient.disconnect();
        }

    }

    private void setClient() {
        mClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: ");
        getLastKnowLocation();
        requestLocationUpdates();
    }

    private void getLastKnowLocation() {
        mCustomLocationChangeListener
                .onLastLocationReceived(LocationServices.FusedLocationApi.getLastLocation(mClient));
    }

    private void requestLocationUpdates() {

        mListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged: ");
                mCustomLocationChangeListener.onLocationChanged(location);
            }
        };
        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, mRequest, mListener);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
