package com.example.campusconnect;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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
                Toast.makeText(activity, "Finger data: " + getFingerprintDataAsString(), Toast.LENGTH_LONG).show();
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

        try {
            // Create a Signature object and initialize it with a key pair
            KeyPair keyPair = getKeyPair();
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(keyPair.getPrivate());

            // Create a CryptoObject using the Signature object
            BiometricPrompt.CryptoObject cryptoObject = new BiometricPrompt.CryptoObject(signature);

            // Authenticate using the CryptoObject
            biometricPrompt.authenticate(cryptoObject, cancellationSignal, activity.getMainExecutor(), createCallback());

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private KeyPair getKeyPair() {
        try {
            // Generate a key pair and store it in the Android Keystore
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
            KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder("my_key", KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                    .setDigests(KeyProperties.DIGEST_SHA256)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .setKeySize(2048)
                    .build();
            keyPairGenerator.initialize(keyGenParameterSpec);
            return keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException | NoSuchProviderException |
                 InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
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
    private ScanFingerPrint scanFingerPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanFingerPrint = new ScanFingerPrint(this);
        scanFingerPrint.startScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanFingerPrint.startScan();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (scanFingerPrint != null) {
            scanFingerPrint.stopScan();
        }
    }

    private void showFingerprintData(String data) {
        Toast.makeText(this, "Finger data: " + data, Toast.LENGTH_LONG).show();
    }
}


 */
