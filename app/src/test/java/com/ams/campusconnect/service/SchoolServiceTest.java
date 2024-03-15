package com.ams.campusconnect.service;

import com.ams.campusconnect.model.School;

import org.junit.Test;

public class SchoolServiceTest {

    @Test
    public void testFindSchoolById() {

        final int schoolID = 305113;
        final School expectedSchool = new School(305113, "Flora National High School", "CATHERINE R. CALUYA PhD", "12345",
                "12345", false, true, true, true, false,
                18.218810, 121.420142, 18.218810, 121.421985, 18.21785994355776, 121.42115711493562);


    }
}
