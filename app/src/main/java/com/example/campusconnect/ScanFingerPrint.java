package com.example.campusconnect;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.P)
public class ScanFingerPrint {
    private Activity activity;
    private BiometricPrompt biometricPrompt;
    private CancellationSignal cancellationSignal;
    private byte[] fingerprintData;

    public ScanFingerPrint(Activity activity) {
        this.activity = activity;
        this.biometricPrompt = createBiometricPrompt(activity);
        this.cancellationSignal = new CancellationSignal();
    }

    private BiometricPrompt createBiometricPrompt(Context context) {
        Executor executor = Executors.newSingleThreadExecutor();
        BiometricPrompt.AuthenticationCallback authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(context, "Authentication Succeeded", Toast.LENGTH_SHORT).show();
                fingerprintData = result.getCryptoObject().getCipher().getIV();
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(context, "Authentication Error", Toast.LENGTH_SHORT).show();
            }
        };

        return new BiometricPrompt.Builder(context)
                .setTitle("Scan Your Fingerprint")
                .setNegativeButton("Cancel", executor, (dialog, which) -> {})
                .build();
    }

    public void startScan() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.USE_BIOMETRIC}, 1);
            return;
        }

        biometricPrompt.authenticate(new BiometricPrompt.CryptoObject((Signature) null), cancellationSignal, activity.getMainExecutor(), createCallback());
    }

    private BiometricPrompt.AuthenticationCallback createCallback() {
        return new BiometricPrompt.AuthenticationCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(activity, "Authentication Succeeded", Toast.LENGTH_SHORT).show();
                fingerprintData = result.getCryptoObject().getCipher().getIV();
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(activity, "Authentication Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {


                super.onAuthenticationFailed();
                Toast.makeText(activity, "Authentication Failed", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public byte[] getFingerprintData() {
        return fingerprintData;
    }

    public String getFingerprintDataAsString() {
        return new String(fingerprintData, StandardCharsets.ISO_8859_1);
    }
}
/*
Button scanButton = findViewById(R.id.scan_button);
scanButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
                ScanFingerPrint scanFingerPrint = new ScanFingerPrint(activity);
                scanFingerPrint.startScan();
            } else {
                Toast.makeText(activity, "Your phone is not equipped with fingerprint scanner", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "Fingerprint authentication is not supported in your device", Toast.LENGTH_SHORT).show();
        }
    }
});

 */
