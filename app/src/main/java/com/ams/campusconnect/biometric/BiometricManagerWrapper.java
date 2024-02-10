package com.ams.campusconnect.biometric;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.ams.campusconnect.LogInAttendance;

import java.util.concurrent.Executor;

public class BiometricManagerWrapper {

    private final Context context;

    public BiometricManagerWrapper(Context context) {
        this.context = context;
    }

    public String checkBiometricSupported() {
        String info = "";

        BiometricManager manager = BiometricManager.from(context);
        switch(manager.canAuthenticate(getBiometricAuthenticators())) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                info = "App can authenticate using biometrics";
                ((LogInAttendance) context).enableButton(true);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                info = "No biometric features available on this device";
                ((LogInAttendance) context).enableButton(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                info = "Biometric features are currently unavailable";
                ((LogInAttendance) context).enableButton(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                info = "Need to register at least one fingerprint";
                ((LogInAttendance) context).enableButton(false, true);
                break;
            default:
                info = "Unknown cause";
                break;
        }

        return info;
    }

    public void authenticate(boolean withPin) {
        Executor executor = ContextCompat.getMainExecutor(context);
        BiometricPrompt.PromptInfo.Builder promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Login using your biometric credential");

        if (withPin) {
            promptInfo.setDeviceCredentialAllowed(true);
        } else {
            promptInfo.setNegativeButtonText("Cancel");
        }

        BiometricPrompt biometricPrompt = new BiometricPrompt((LogInAttendance) context,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                showToast("Auth error: " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                showToast("Auth succeeded");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                showToast("Auth failed");
            }
        });

        biometricPrompt.authenticate(promptInfo.build());
    }

    private void showToast(String message) {
        ((LogInAttendance) context).runOnUiThread(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }

    public static int getBiometricAuthenticators() {
        return BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.BIOMETRIC_WEAK;
    }
}
