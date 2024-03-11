package com.ams.campusconnect.service;

import com.ams.campusconnect.model.SchoolModel;
import com.ams.campusconnect.repository.SchoolRepository;

import java.util.ArrayList;
import java.util.List;

public class SchoolService {
    private SchoolRepository schoolRepository;

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    private static List<SchoolModel> schools = new ArrayList<>();
    private static  SchoolModel school = new SchoolModel();

    // find school by id
    public SchoolModel findSchoolById(int schoolID){
        schoolRepository.findSchoolById(schoolID, new SchoolRepository.SchoolDataCallback() {
            @Override
            public void onDataReceived(SchoolModel school) {
                System.out.println(school);
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println(errorMessage);
            }
        });
        return school;
    }
}