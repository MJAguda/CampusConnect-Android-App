package com.ams.campusconnect.controller;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class SchoolControllerTest {
    private SchoolController schoolController = new SchoolController();

    @Mock
    private DatabaseReference mockedDatabaseReference;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mockedDatabaseReference.child(anyString())).thenReturn(mockedDatabaseReference);
        when(FirebaseDatabase.getInstance().getReference()).thenReturn(mockedDatabaseReference);
    }

    @Test
    public void testAddSchool() {

    }

    @Test
    public void testGetSchoolData() {

    }
}