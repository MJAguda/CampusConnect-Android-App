package com.ams.campusconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.ams.campusconnect.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanQR extends AppCompatActivity {

    private static final String TAG = "ScanQR";
    private SurfaceView cameraSurfaceView;
    private BarcodeDetector detector;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        cameraSurfaceView = findViewById(R.id.cameraSurfaceView);

        detector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                if (detections != null && detections.getDetectedItems().size() > 0) {
                    final Barcode barcode = detections.getDetectedItems().valueAt(0);
                    if (barcode != null) {
                        Intent intent = new Intent();
                        intent.putExtra("QR_RESULT", barcode.displayValue);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });

        cameraSource = new CameraSource.Builder(this, detector)
                .setRequestedFps(25f)
                .setAutoFocusEnabled(true)
                .build();

        cameraSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    try {
                        cameraSource.start(cameraSurfaceView.getHolder());
                    } catch (IOException e) {
                        Log.e(TAG, "Error starting camera source: " + e.getMessage());
                    }
                } else {
                    ActivityCompat.requestPermissions(ScanQR.this, new String[]{Manifest.permission.CAMERA}, 1001);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) { // 1001
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        cameraSource.start(cameraSurfaceView.getHolder());
                    } catch (IOException e) {
                        Log.e(TAG, "Error starting camera source: " + e.getMessage());
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "Camera permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detector.release();
        cameraSource.stop();
        cameraSource.release();
    }
}

/*
    private static final int REQUEST_CODE_SCAN_QR = 1;

    // Put inside the main, Button action Listener in the Main
    // Set click listener on button to start ScanQR activity
        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Generate.this, ScanQR.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN_QR);
            }
        });

    // Put Outside the Main
    // Handles Scanned QR Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        id = findViewById(R.id.id_EditText);

        if (requestCode == REQUEST_CODE_SCAN_QR && resultCode == RESULT_OK && data != null) {
            String qrResult = data.getStringExtra("QR_RESULT");
            // Handle the QR code result here
            Toast.makeText(this, "QR code result: " + qrResult, Toast.LENGTH_SHORT).show();
            id.setText(qrResult);
        }
    }
 */