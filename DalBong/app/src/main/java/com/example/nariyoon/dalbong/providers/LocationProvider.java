package com.example.nariyoon.dalbong.providers;

import android.location.Location;

/**
 * Created by nari.yoon on 2017-03-31.
 */

public abstract class LocationProvider {

    public interface LocationChangeListener {
        void onLocationChanged(Location location);

        void onLastLocationReceived(Location lastLocation);
    }

    abstract public void unregisterLocationListener();

    abstract public void registerLocationListener(long minTime, float distance,
            final LocationChangeListener locationChangeListener);
}
