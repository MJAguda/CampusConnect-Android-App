package com.example.campusconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;

public class Generate extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();
    Read read = new Read();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        // Declare Components
        EditText id = findViewById(R.id.id_EditText);
        TextView prompt = findViewById(R.id.prompt);
        Button submit = findViewById(R.id.submit_Button);
        ImageButton home = findViewById(R.id.home_Button);
        ImageButton generateQR = findViewById(R.id.generateQR_Button);
        ImageView logo = findViewById(R.id.footerlogo_ImageView);
        ImageButton generateDTR = findViewById(R.id.generateDTR_Button);
        ImageButton generateTAMS = findViewById(R.id.generateTAMS_Button);
        TextView guide1 = findViewById(R.id.buttonGuide_TextView1);
        TextView guide2 = findViewById(R.id.buttonGuide_TextView2);
        TextView guide3 = findViewById(R.id.buttonGuide_TextView3);
        TextView guide4 = findViewById(R.id.buttonGuide_TextView4);
        TextView guide5 = findViewById(R.id.buttonGuide_TextView5);

        // Hide buttons
        home.setVisibility(View.GONE);
        generateQR.setVisibility(View.GONE);
        logo.setVisibility(View.GONE);
        generateDTR.setVisibility(View.GONE);
        generateTAMS.setVisibility(View.GONE);
        guide1.setVisibility(View.GONE);
        guide2.setVisibility(View.GONE);
        guide3.setVisibility(View.GONE);
        guide4.setVisibility(View.GONE);
        guide5.setVisibility(View.GONE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setId(id.getText().toString());

                read.readRecord(school.getSchoolID() + "/employee/" + save.getId(), new Read.OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            home.setVisibility(View.VISIBLE);
                            generateQR.setVisibility(View.VISIBLE);
                            logo.setVisibility(View.VISIBLE);
                            generateDTR.setVisibility(View.VISIBLE);
                            generateTAMS.setVisibility(View.VISIBLE);
                            guide1.setVisibility(View.VISIBLE);
                            guide2.setVisibility(View.VISIBLE);
                            guide3.setVisibility(View.VISIBLE);
                            guide4.setVisibility(View.VISIBLE);
                            guide5.setVisibility(View.VISIBLE);

                            id.setVisibility(View.GONE);
                            prompt.setVisibility(View.GONE);
                            submit.setVisibility(View.GONE);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "ID Number not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(DatabaseError databaseError) {
                        // handle error here
                    }
                });
            }
        });

        // Home button
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Generate.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Generate QR for employee
        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Instance
                GenerateQR generateQR = new GenerateQR();

                // Declare ImageView
                ImageView qr = findViewById(R.id.qrCode_ImageView);
                // call generateQRCode method from GenerateQR class
                qr.setImageBitmap(generateQR.generateQRCode(save.getId()));


                // Download qr if a ImageView is clicked
                qr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DownloadImageView imageDownloader = new DownloadImageView(qr);
                        imageDownloader.downloadImage();
                        //Intent intent = new Intent(Generate.this, Attendance.class);
                        //startActivity(intent);
                    }
                });
            }
        });

        // Generate DTR for employee
        generateDTR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Generate.this, GenerateDTR.class);
                startActivity(intent);
            }
        });

        // TODO Generate TAMS file for Employee
        generateTAMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}