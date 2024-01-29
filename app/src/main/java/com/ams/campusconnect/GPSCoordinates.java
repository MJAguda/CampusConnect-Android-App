package com.ams.campusconnect;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GPSCoordinates {
    // TODO Implement server-side verification
    // TODO Geofencing
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location currentLocation;
    private Context context;

    public GPSCoordinates(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
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
    }

    public Location getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            Log.d("Location", "Requesting permission for location access.");
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return null;
        }

        // Check if device is using VPN
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_VPN) {
            // The device is using a VPN, handle it appropriately
            return null;
        }

        // Open GPS if it's not enabled
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (currentLocation == null) {
                currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            while (currentLocation == null) {
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (currentLocation == null) {
                    currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
            // Check if location is valid
            double latitude = currentLocation.getLatitude();
            double longitude = currentLocation.getLongitude();
            FirebaseUtils firebaseUtils = new FirebaseUtils();
            if (!firebaseUtils.isLocationValid(latitude, longitude)) {
                return null;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return currentLocation;
    }
}


/* To call it in the main method
GPSCoordinates gpsCoordinates = new GPSCoordinates(this);
Location currentLocation = gpsCoordinates.getCurrentLocation();

if (currentLocation != null) {
    Log.d("Location", "Latitude: " + currentLocation.getLatitude() + ", Longitude: " + currentLocation.getLongitude());
}
else {
    Log.d("Location", "No location data available.");
}

 */

