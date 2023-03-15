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
        } else {
            // Check this
            biometricPrompt.authenticate(new BiometricPrompt.CryptoObject((Signature) null), cancellationSignal, activity.getMainExecutor(), createCallback());
        }
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
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ScanFingerPrint scanFingerPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            scanFingerPrint = new ScanFingerPrint(LogInAttendance.this);
            if (scanFingerPrint != null) {
                scanFingerPrint.startScan();
            } else {
                Toast.makeText(this, "Error initializing fingerprint scanner", Toast.LENGTH_SHORT).show();
            }
        } else{
            // Handle the case where BiometricPrompt is not available on the device
        }
    }

    // Handler Scanned FingerPrint Result
    @Override
    protected void onResume() {
        super.onResume();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            if (scanFingerPrint != null) {
                byte[] fingerprintData = scanFingerPrint.getFingerprintData();
                if (fingerprintData != null) {
                    Log.d(TAG, "Fingerprint data: " + Arrays.toString(fingerprintData));
                } else {
                    Log.d(TAG, "Fingerprint data is null");
                }
            } else {
                Toast.makeText(this, "Fingerprint scanner not initialized", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

 */
