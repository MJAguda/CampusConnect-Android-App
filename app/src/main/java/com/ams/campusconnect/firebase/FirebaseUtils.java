package com.ams.campusconnect.firebase;

import android.location.Location;

import com.ams.campusconnect.model.School;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    private final School school = School.getInstance();

    public FirebaseUtils() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference locationsReference = firebaseDatabase.getReference("locations");
    }

    public boolean isLocationValid(double latitude, double longitude) {
        double expectedLatitude = Double.parseDouble(school.getLatitudeCenter());
        double expectedLongitude = Double.parseDouble(school.getLongitudeCenter());
        float[] results = new float[1];
        Location.distanceBetween(latitude, longitude, expectedLatitude, expectedLongitude, results);
        return results[0] < 1000; // location is valid if it is within 1 km of the expected location
    }

}

