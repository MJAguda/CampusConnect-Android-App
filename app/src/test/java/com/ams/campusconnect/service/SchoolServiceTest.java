package com.ams.campusconnect.service;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ams.campusconnect.model.SchoolModel;
import com.ams.campusconnect.repository.SchoolRepository;
import com.google.firebase.database.DatabaseReference;

public class SchoolServiceTest {

    @Test
    public void testFindSchoolById() {

        final int schoolID = 305113;
        final SchoolModel expectedSchool = new SchoolModel(305113, "Flora National High School", "CATHERINE R. CALUYA PhD", "12345",
                "12345", false, true, true, true, false,
                18.218810, 121.420142, 18.218810, 121.421985, 18.21785994355776, 121.42115711493562);


    }
}
