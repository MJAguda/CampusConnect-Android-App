package com.ams.campusconnect.biometric;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class BiometricUtils {

    private final Context context;
    private final BiometricPrompt.AuthenticationCallback authenticationCallback;

    public BiometricUtils(Context context, BiometricPrompt.AuthenticationCallback authenticationCallback) {
        this.context = context;
        this.authenticationCallback = authenticationCallback;
    }

    public void authenticate(boolean usePinFallback) {
        Executor executor = ContextCompat.getMainExecutor(context);
        BiometricPrompt biometricPrompt = new BiometricPrompt((AppCompatActivity) context,
                executor, authenticationCallback);

        BiometricPrompt.PromptInfo.Builder promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Login using your biometric credential");

        if (usePinFallback) {
            promptInfo.setDeviceCredentialAllowed(true);
        } else {
            promptInfo.setNegativeButtonText("Cancel");
        }

        biometricPrompt.authenticate(promptInfo.build());
    }

    public void checkBiometricSupported() {
        BiometricManager manager = BiometricManager.from(context);
        switch (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK
                | BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Toast.makeText(context, "App can authenticate using biometrics", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(context, "No biometric features available on this device", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(context, "Biometric features are currently unavailable", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(context, "Need register at least on fingerprint", Toast.LENGTH_SHORT).show();
                Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_WEAK
                                | BiometricManager.Authenticators.BIOMETRIC_STRONG);
                context.startActivity(enrollIntent);
                break;
            default:
                Toast.makeText(context, "Unknown cause", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

//
//        import com.example.fingerprinttest.biometric.BiometricUtils;
//
//public class MainActivity extends AppCompatActivity {
//
//    // Component Initialization
//    Button btn_fp, btn_fppin;
//    TextView txinfo;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Hooks
//        btn_fp = findViewById(R.id.btn_fp);
//        btn_fppin = findViewById(R.id.btn_fppin);
//        txinfo = findViewById(R.id.tx_info);
//
//        BiometricUtils biometricManagerHelper = new BiometricUtils(this, createBiometricCallback());
//
//        biometricManagerHelper.checkBiometricSupported();
//
//        // For fingerprint only
//        btn_fp.setOnClickListener(view -> biometricManagerHelper.authenticate(false));
//
//        // For fingerprint and pin
//        btn_fppin.setOnClickListener(view -> biometricManagerHelper.authenticate(true));
//    }
//
//    private BiometricPrompt.AuthenticationCallback createBiometricCallback() {
//        return new BiometricPrompt.AuthenticationCallback() {
//            @Override
//            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
//                super.onAuthenticationError(errorCode, errString);
//                Toast.makeText(MainActivity.this, "Auth error: " + errString, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
//                super.onAuthenticationSucceeded(result);
//                Toast.makeText(MainActivity.this, "Auth succeeded", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAuthenticationFailed() {
//                super.onAuthenticationFailed();
//                Toast.makeText(MainActivity.this, "Auth failed", Toast.LENGTH_SHORT).show();
//            }
//        };
//    }
//}
