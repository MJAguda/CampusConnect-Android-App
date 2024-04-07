package com.ams.campusconnect.controller;

import android.content.Context;

import com.ams.campusconnect.model.School;
import com.ams.campusconnect.model.Timelog;
import com.ams.campusconnect.service.TimelogService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TimelogController{
    private DatabaseReference databaseReference;
    private TimelogService timelogService;
    private Context context;

    public TimelogController(Context context){
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.context = context;
        this.timelogService = new TimelogService(context);
    }

    // Add TimelogChangeRequest
    public void addTimelogChangeRequest(Timelog timelog, School school) {
        timelogService.addTimelogChangeRequest(timelog, school);
    }

}
