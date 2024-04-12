package com.ams.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ams.campusconnect.controller.EmployeeController;
import com.ams.campusconnect.model.EmployeeModel;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.qrcode.DownloadQR;
import com.ams.campusconnect.qrcode.GenerateQR;
import com.ams.campusconnect.qrcode.ScanQR;
import com.ams.campusconnect.repository.EmployeeRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class Generate extends AppCompatActivity {
    School school;
    EmployeeModel employeeModel;
    EmployeeController employeeController;

    private static final int REQUEST_CODE_SCAN_QR = 1;
    EditText idEditText;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        // Get School Data
        school = (School) getIntent().getSerializableExtra("school");
        employeeModel = (EmployeeModel) getIntent().getSerializableExtra("employee");

        // Declare Components
        idEditText = findViewById(R.id.id_EditText);
        TextView prompt = findViewById(R.id.prompt);

        // Declare Button Components
        ImageButton back = findViewById(R.id.backButton_ImageButton);
        ImageButton hamburger = findViewById(R.id.hamburger_ImageButton);
        ImageButton scanQR = findViewById(R.id.scanQR_ImageButton);
        //ImageButton scanFingerPrint = findViewById(R.id.scanFingerPrint_ImageButton);
        //ImageButton scanFacial = findViewById(R.id.scanFacial_ImageButton);
        Button submit = findViewById(R.id.submit_Button);
        ImageButton home = findViewById(R.id.home_Button);
        ImageButton generateQR = findViewById(R.id.generateQR_Button);
        ImageView logo = findViewById(R.id.footerlogo_ImageView);
        ImageButton generateDTR = findViewById(R.id.generateDTR_Button);
        ImageButton generateTAMS = findViewById(R.id.generateTAMS_Button);

        // Declare guide for footer buttons
        TextView text = findViewById(R.id.generateQR_TextView);
        TextView guide1 = findViewById(R.id.buttonGuide_TextView1);
        TextView guide2 = findViewById(R.id.buttonGuide_TextView2);
        TextView guide3 = findViewById(R.id.buttonGuide_TextView3);
        TextView guide4 = findViewById(R.id.buttonGuide_TextView4);
        TextView guide5 = findViewById(R.id.buttonGuide_TextView5);

        // Hide buttons
        home.setVisibility(View.GONE);
        generateQR.setVisibility(View.GONE);
        text.setVisibility(View.GONE);
        logo.setVisibility(View.GONE);
        generateDTR.setVisibility(View.GONE);
        generateTAMS.setVisibility(View.GONE);
        guide1.setVisibility(View.GONE);
        guide2.setVisibility(View.GONE);
        guide3.setVisibility(View.GONE);
        guide4.setVisibility(View.GONE);
        guide5.setVisibility(View.GONE);

        back.setOnClickListener(view -> {
            Intent intent = new Intent(Generate.this, LogbookActivity.class);
            intent = intent.putExtra("school", school);
            intent = intent.putExtra("employee", employeeModel);
            startActivity(intent);
        });

        hamburger.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "On going", Toast.LENGTH_SHORT).show());

        scanQR.setOnClickListener(view -> {
            Intent intent = new Intent(Generate.this, ScanQR.class);
            intent = intent.putExtra("school", school);
            intent = intent.putExtra("employee", employeeModel);
            startActivityForResult(intent, REQUEST_CODE_SCAN_QR);


        });

        submit.setOnClickListener(view -> {
            if (idEditText.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "ID Number should not be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            employeeController = new EmployeeController(this, school);
            employeeController.getEmployee(school, idEditText.getText().toString(), new EmployeeRepository.OnDataFetchListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        Toast.makeText(getApplicationContext(), "ID Number not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Set data for Employee
                    employeeModel = dataSnapshot.getValue(EmployeeModel.class);

                    // Unhide Components
                    home.setVisibility(View.VISIBLE);
                    generateQR.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                    logo.setVisibility(View.VISIBLE);
                    generateDTR.setVisibility(View.VISIBLE);
                    generateTAMS.setVisibility(View.VISIBLE);
                    guide1.setVisibility(View.VISIBLE);
                    guide2.setVisibility(View.VISIBLE);
                    guide3.setVisibility(View.VISIBLE);
                    guide4.setVisibility(View.VISIBLE);
                    guide5.setVisibility(View.VISIBLE);

                    // Hide Components
                    prompt.setVisibility(View.GONE);
                    idEditText.setVisibility(View.GONE);
                    scanQR.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(DatabaseError databaseError) {
                    Log.d("Read", "Error: " + databaseError.getMessage());
                    Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Home button
        home.setOnClickListener(view -> {
            Intent intent = new Intent(Generate.this, LogbookActivity.class);
            intent = intent.putExtra("school", school);
            intent = intent.putExtra("employee", employeeModel);
            startActivity(intent);
        });

        // Generate QR for employee
        generateQR.setOnClickListener(view -> {

            // Instance
            GenerateQR generateQR1 = new GenerateQR();

            // Declare ImageView
            ImageView qr = findViewById(R.id.qrCode_ImageView);
            // call generateQRCode method from GenerateQR class
            qr.setImageBitmap(generateQR1.generateQRCode(employeeModel.getId()));

            // Set text Guide
            text.setText("Tap QR code to download");

            // Download qr if a ImageView is clicked
            qr.setOnClickListener(view1 -> {
                DownloadQR downloadQR = new DownloadQR(qr, school);
                downloadQR.downloadImage(Generate.this);
            });
        });

        // Generate DTR for employee
        generateDTR.setOnClickListener(view -> {
            Intent intent = new Intent(Generate.this, GenerateDTR.class);
            intent = intent.putExtra("school", school);
            intent = intent.putExtra("employee", employeeModel);
            startActivity(intent);
        });

        // TODO Generate TAMS file for Employee
        generateTAMS.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "On going", Toast.LENGTH_SHORT).show());
    }

    // Handles Scanned QR Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        idEditText = findViewById(R.id.id_EditText);
        submit = findViewById(R.id.submit_Button);

        if (requestCode == REQUEST_CODE_SCAN_QR && resultCode == RESULT_OK && data != null) {
            String qrResult = data.getStringExtra("QR_RESULT");

            // Handle the QR code result here
            Toast.makeText(this, "QR code result: " + qrResult, Toast.LENGTH_SHORT).show();

            // set the idNumber_TextView to the scanned QR code
            idEditText.setText(qrResult);

            // Perform click
            submit.performClick();
        }
    }
}