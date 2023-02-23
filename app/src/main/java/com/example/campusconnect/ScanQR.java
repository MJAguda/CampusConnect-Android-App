package com.example.campusconnect;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanQR extends AppCompatActivity {

    private static final String TAG = "ScanQR";
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private CameraSource mCameraSource;
    private SurfaceView cameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the camera preview
        setContentView(R.layout.activity_scan_qr);
        cameraPreview = findViewById(R.id.cameraPreview);

        // Check camera permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            // Start the camera preview
            startCameraPreview();
        }
    }

    private void startCameraPreview() {
        // Set up the barcode detector
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        if (!barcodeDetector.isOperational()) {
            Log.w(TAG, "Barcode detector is not operational");
            return;
        }

        // Set up the camera source
        mCameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(30.0f)
                .setAutoFocusEnabled(true)
                .build();

        // Start the camera preview
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanQR.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        mCameraSource.start(cameraPreview.getHolder());
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error starting camera preview", e);
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                mCameraSource.stop();
            }
        });

        // Set up the barcode detector listener
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0) {
                    // Barcode detected, extract value and finish activity
                    Barcode qrCode = barcodes.valueAt(0);
                    String qrCodeValue = qrCode.rawValue;
                    Log.d(TAG, "QR code value: " + qrCodeValue);
                    setResult(RESULT_OK, getIntent().putExtra("qrCodeValue", qrCodeValue));
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, start the camera preview
                startCameraPreview();
            } else {
                // Camera permission not granted, finish the activity
                finish();
            }
        }
    }

}
