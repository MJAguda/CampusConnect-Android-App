package com.ams.campusconnect.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ams.campusconnect.model.School;
import com.ams.campusconnect.model.Timelog;
import com.ams.campusconnect.repository.TimelogRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class TimelogController{
    private TimelogRepository timelogRepository;
    private Context context;

    public TimelogController(Context context){
        this.context = context;
        this.timelogRepository = new TimelogRepository();
    }

    List<Timelog> timelogs = new ArrayList<>();

    // GetAllTimelogRequests
    // Populate timelogs list with TimelogRepository getAllTimelogRequests method
    public void getAllTimelogRequests(int schoolID, final TimelogRepository.OnDataFetchListener listener){
        timelogRepository.getAllTimelogRequests(schoolID, listener);
    }

    // Add TimelogChangeRequest
    public void addTimelogChangeRequest(Timelog timelog, School school) {
        timelogRepository.addTimelogChangeRequest(timelog, school);
        Toast.makeText(context, "Timelog Change Request Added Successfully", Toast.LENGTH_SHORT).show();
    }

    public void getTimelogs(int schoolID, String employeeID, String year, String month, String day, final TimelogRepository.OnDataFetchListener listener){
        timelogRepository.getTimelogs(schoolID, employeeID, year, month, day, listener);
    }
}
