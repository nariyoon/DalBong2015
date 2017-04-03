package com.example.nariyoon.dalbong;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.nariyoon.dalbong.providers.FusedAPIProvider;
import com.example.nariyoon.dalbong.providers.LocationProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

@SuppressWarnings("MissingPermission")
public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, LocationProvider.LocationChangeListener {

    private static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private LocationProvider mProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mProvider = new FusedAPIProvider(this);
        mProvider.registerLocationListener(0, 1, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProvider.unregisterLocationListener();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

    }

    PolylineOptions polyOptions;
    ArrayList<LatLng> points = new ArrayList<>();

    @Override
    public void onLocationChanged(final Location location) {
        Log.d(TAG, "onLocationChanged: ");
        if (mMap != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                    points.add(position);

                    // add marker
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(points.get(0));
                    mMap.addMarker(marker);

                    // add line
                    polyOptions = new PolylineOptions();
                    polyOptions.add(position);
                    polyOptions.color(Color.DKGRAY);
                    mMap.addPolyline(polyOptions);
                }
            });
        }
    }

    @Override
    public void onLastLocationReceived(final Location lastLocation) {
        Log.d(TAG, "onLastLocationReceived: ");
        if (mMap != null) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LatLng position = new LatLng(lastLocation.getLatitude(),
                            lastLocation.getLongitude());

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                }
            });
        }
    }
}
