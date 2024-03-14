package com.ams.campusconnect.controller;

import com.ams.campusconnect.model.SchoolModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class SchoolControllerTest {
    private SchoolController schoolController;

    @Mock
    private DatabaseReference mockedDatabaseReference;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mockedDatabaseReference.child(anyString())).thenReturn(mockedDatabaseReference);
        when(FirebaseDatabase.getInstance().getReference()).thenReturn(mockedDatabaseReference);
        schoolController = new SchoolController(1);
    }

    @Test
    public void testAddSchool() {

    }

    @Test
    public void testGetSchoolData() {

    }
}