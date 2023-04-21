package com.example.campusconnect;

import android.location.Location;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference locationsReference;

    public FirebaseUtils() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        locationsReference = firebaseDatabase.getReference("locations");
    }

    public boolean isLocationValid(double latitude, double longitude) {
        double expectedLatitude = 18.2178;
        double expectedLongitude = 121.4212;
        float[] results = new float[1];
        Location.distanceBetween(latitude, longitude, expectedLatitude, expectedLongitude, results);
        return results[0] < 1000; // location is valid if it is within 1 km of the expected location
    }

}

