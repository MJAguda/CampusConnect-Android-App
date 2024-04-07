package com.ams.campusconnect.service;

import com.ams.campusconnect.model.School;
import com.ams.campusconnect.model.Timelog;
import com.ams.campusconnect.repository.TimelogRepository;
import android.content.Context;
import android.widget.Toast;

public class TimelogService {
    private TimelogRepository timelogRepository;
    private Context context;

    public TimelogService(Context context) {
        this.timelogRepository = new TimelogRepository();
        this.context = context;
    }

    // Add TimelogChangeRequest
    public void addTimelogChangeRequest(Timelog timelog, School school) {
        timelogRepository.addTimelogChangeRequest(timelog, school);
        Toast.makeText(context, "Timelog Change Request Added Successfully", Toast.LENGTH_SHORT).show();
    }
}
