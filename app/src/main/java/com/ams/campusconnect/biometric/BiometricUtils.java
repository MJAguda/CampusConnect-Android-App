package com.ams.campusconnect.biometric;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

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
