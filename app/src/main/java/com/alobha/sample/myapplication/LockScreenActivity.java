package com.alobha.sample.myapplication;

/*import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

public class LockScreenActivity extends AppCompatActivity {

    private DevicePolicyManager dpm;
    private ComponentName adminComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity to be full-screen and block other apps
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_lock_screen);

        // Initialize DevicePolicyManager and AdminReceiver
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(this, AdminReceiver.class);

        // Show payment confirmation dialog
        showPaymentConfirmationDialog();
    }

    // Show a dialog to confirm payment
    private void showPaymentConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Payment Confirmation")
                .setMessage("Please confirm your payment to unlock the device.")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    onPaymentConfirmed(); // Call when payment is confirmed
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Lock the device again if payment is not confirmed
                    lockDevice();
                    dialog.dismiss();
                })
                .setCancelable(false) // Prevent dismissing by touching outside
                .show();
    }

    // Call this method when the payment is confirmed
    public void onPaymentConfirmed() {
        Toast.makeText(this, "Payment confirmed. Device unlocked.", Toast.LENGTH_SHORT).show();
        unlockDevice(); // Remove admin privileges
        finish(); // Close the lock screen
    }

    // Lock the device using DevicePolicyManager
    private void lockDevice() {
        if (dpm.isAdminActive(adminComponent)) {
            Toast.makeText(this, "Locking the device due to payment not confirmed.", Toast.LENGTH_SHORT).show();
            dpm.lockNow(); // Lock the device immediately
        } else {
            Toast.makeText(this, "Admin permission is not active.", Toast.LENGTH_SHORT).show();
        }
    }

    // Unlock and remove admin privileges when payment is made
    private void unlockDevice() {
        if (dpm.isAdminActive(adminComponent)) {
            dpm.removeActiveAdmin(adminComponent); // Remove admin rights
            Toast.makeText(this, "Device unlocked successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Admin permission is not active.", Toast.LENGTH_SHORT).show();
        }
    }
}*/

//attempt 10
/*

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LockScreenActivity extends AppCompatActivity {
    private DevicePolicyManager dpm;
    private ComponentName adminComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_lock_screen);

        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(this, AdminReceiver.class);

        showPaymentConfirmationDialog();
    }

    private void showPaymentConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Payment Confirmation")
                .setMessage("Please confirm your payment to unlock the device.")
                .setPositiveButton("Confirm", (dialog, which) -> onPaymentConfirmed())
                .setNegativeButton("Cancel", (dialog, which) -> {
                    lockDevice();
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    public void onPaymentConfirmed() {
        Toast.makeText(this, "Payment confirmed. Device unlocked.", Toast.LENGTH_SHORT).show();
        unlockDevice();
        finish();
    }

    private void lockDevice() {
        if (dpm.isAdminActive(adminComponent)) {
            Toast.makeText(this, "Locking the device due to payment not confirmed.", Toast.LENGTH_SHORT).show();
            dpm.lockNow();
        } else {
            Toast.makeText(this, "Admin permission is not active.", Toast.LENGTH_SHORT).show();
        }
    }

    private void unlockDevice() {
        if (dpm.isAdminActive(adminComponent)) {
            dpm.removeActiveAdmin(adminComponent);
            Toast.makeText(this, "Device unlocked successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Admin permission is not active.", Toast.LENGTH_SHORT).show();
        }
    }
}
*/
//attempt 11 worimng f9
/*import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LockScreenActivity extends AppCompatActivity {
    private DevicePolicyManager dpm;
    private ComponentName adminComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_lock_screen);

        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(this, AdminReceiver.class);

        setupCheckServiceButton(); // Set up the button to check service status
    }

    private void showPaymentConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Payment Confirmation")
                .setMessage("Please confirm your payment to unlock the device.")
                .setPositiveButton("Confirm", (dialog, which) -> onPaymentConfirmed())
                .setNegativeButton("Skip for Now", (dialog, which) -> {
                    lockDevice();
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    public void onPaymentConfirmed() {
        Toast.makeText(this, "Payment confirmed. Device unlocked.", Toast.LENGTH_SHORT).show();
        unlockDevice();
        finish();
    }

    private void lockDevice() {
        if (dpm.isAdminActive(adminComponent)) {
            Toast.makeText(this, "Locking the device due to payment not confirmed.", Toast.LENGTH_SHORT).show();
            dpm.lockNow();
        } else {
            Toast.makeText(this, "Admin permission is not active.", Toast.LENGTH_SHORT).show();
        }
    }

    private void unlockDevice() {
        if (dpm.isAdminActive(adminComponent)) {
            dpm.removeActiveAdmin(adminComponent);
            Toast.makeText(this, "Device unlocked successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Admin permission is not active.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupCheckServiceButton() {
        Button checkServiceButton = findViewById(R.id.check_service_button); // Make sure to add this button in your layout
        checkServiceButton.setOnClickListener(v -> {
            // Here you can check if the service is running
            boolean isServiceRunning = isServiceRunning(PaymentCheckService.class);
            Toast.makeText(this, "Service Running: " + isServiceRunning, Toast.LENGTH_SHORT).show();

            showPaymentConfirmationDialog();
        });
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}*/

import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//attempt 13
//attempt 14
/*
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LockScreenActivity extends AppCompatActivity {
    private DevicePolicyManager dpm;
    private ComponentName adminComponent;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "PaymentPrefs";
    private static final String PREF_KEY_PAYMENT_CHECK_ENABLED = "isPaymentCheckEnabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_lock_screen);
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(this, AdminReceiver.class);

       // setupCheckServiceButton(); // Set up the button to check service status
        showPaymentConfirmationDialog(); // Show the dialog on screen launch
    }

    private void showPaymentConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Payment Confirmation")
                .setMessage("Please confirm your payment to unlock the device.")
                .setPositiveButton("Confirm", (dialog, which) -> onPaymentConfirmed())
                .setNegativeButton("Skip for Now", (dialog, which) -> {
                    // If payment was not confirmed, enable the background service to continue checking
                    setPaymentCheckEnabled(true);

                    // Save that the payment is not confirmed
                    savePaymentConfirmed(false);

                    // Start the background service to keep locking the device
                    startService(new Intent(this, PaymentCheckService.class));

                    // Lock the device immediately
                    lockDevice();

                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }


    // Method called when payment is confirmed
    public void onPaymentConfirmed() {
        Toast.makeText(this, "Payment confirmed. Device will now unlock.", Toast.LENGTH_SHORT).show();

        // Stop the background service
        stopService(new Intent(this, PaymentCheckService.class));

        // Disable payment check so that the service doesn't lock the device
        setPaymentCheckEnabled(false);

        // Save that the payment is confirmed
        savePaymentConfirmed(true);

        // Unlock the device
        unlockDevice();
    }


    // Lock the device if payment is not confirmed
    private void lockDevice() {
        if (dpm.isAdminActive(adminComponent)) {
            Toast.makeText(this, "Locking the device due to payment not confirmed.", Toast.LENGTH_SHORT).show();
            dpm.lockNow();
        } else {
            Toast.makeText(this, "Admin permission is not active.", Toast.LENGTH_SHORT).show();
        }
    }

    // Unlock the device when payment is confirmed
    private void unlockDevice() {
        if (dpm.isAdminActive(adminComponent)) {
            Toast.makeText(this, "Device unlocked successfully.", Toast.LENGTH_SHORT).show();
            finish(); // Close the lock screen activity
        } else {
            Toast.makeText(this, "Admin permission is not active.", Toast.LENGTH_SHORT).show();
        }
    }

    // Set up the button to check the service status
    private void setupCheckServiceButton() {
        Button checkServiceButton = findViewById(R.id.check_service_button); // Ensure this button exists in your layout
        checkServiceButton.setOnClickListener(v -> {
            boolean isServiceRunning = isServiceRunning(PaymentCheckService.class);
            Toast.makeText(this, "Service Running: " + isServiceRunning, Toast.LENGTH_SHORT).show();

            // Show the payment confirmation dialog when the button is clicked
            showPaymentConfirmationDialog();
        });
    }

    // Check if the service is running
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    // Save the payment confirmation status
    private void savePaymentConfirmed(boolean confirmed) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isPaymentConfirmed", confirmed);
        editor.apply();
    }

    // Check if the payment has been confirmed before
    private boolean isPaymentConfirmed() {
        return prefs.getBoolean("isPaymentConfirmed", false);
    }

    // Enable or disable payment check
    private void setPaymentCheckEnabled(boolean enabled) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_PAYMENT_CHECK_ENABLED, enabled);
        editor.apply();
    }
}
*/
//attempt 15
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.alobha.sample.myapplication.model.PaymentStatusData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//public class LockScreenActivity extends AppCompatActivity {
//    private DevicePolicyManager dpm;
//    private ComponentName adminComponent;
//    private SharedPreferences prefs;
//    private static final String PREFS_NAME = "PaymentPrefs";
//    private static final String PREF_KEY_PAYMENT_CHECK_ENABLED = "isPaymentCheckEnabled";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
//                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
//                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
//                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//
//        setContentView(R.layout.activity_lock_screen);
//        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//
//        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//        adminComponent = new ComponentName(this, AdminReceiver.class);
//
//        showPaymentConfirmationDialog(); // Show the dialog on screen launch
//    }
//
//    private void showPaymentConfirmationDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("Payment Confirmation")
//                .setMessage("Please confirm your payment to unlock the device.")
//                .setPositiveButton("Confirm", (dialog, which) -> onPaymentConfirmed())
//                .setNegativeButton("Skip for Now", (dialog, which) -> {
//                    // If payment was not confirmed, enable the background service to continue checking
//                    setPaymentCheckEnabled(true);
//
//                    // Save that the payment is not confirmed
//                    savePaymentConfirmed(false);
//
//                    // Start the background service to keep locking the device
//                    startService(new Intent(this, PaymentCheckService.class));
//
//                    // Lock the device immediately
//                    lockDevice();
//
//                    dialog.dismiss();
//                })
//                .setCancelable(false)
//                .show();
//    }
//
//    // Method called when payment is confirmed
//    public void onPaymentConfirmed() {
//        Toast.makeText(this, "Payment confirmed. Device will now unlock.", Toast.LENGTH_SHORT).show();
//
//        // Stop the background service
//        stopService(new Intent(this, PaymentCheckService.class));
//
//        // Disable payment check so that the service doesn't lock the device
//        setPaymentCheckEnabled(false);
//
//        // Save that the payment is confirmed
//        savePaymentConfirmed(true);
//
//        // Unlock the device
//        unlockDevice();
//    }
//
//    // Lock the device if payment is not confirmed
//    private void lockDevice() {
//        if (dpm.isAdminActive(adminComponent)) {
//            Toast.makeText(this, "Locking the device due to payment not confirmed.", Toast.LENGTH_SHORT).show();
//            dpm.lockNow();
//        } else {
//            Toast.makeText(this, "Admin permission is not active.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // Unlock the device when payment is confirmed
//    private void unlockDevice() {
//        if (dpm.isAdminActive(adminComponent)) {
//            Toast.makeText(this, "Device unlocked successfully.", Toast.LENGTH_SHORT).show();
//            finish(); // Close the lock screen activity
//        } else {
//            Toast.makeText(this, "Admin permission is not active.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // Save the payment confirmation status
//    private void savePaymentConfirmed(boolean confirmed) {
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putBoolean("isPaymentConfirmed", confirmed);
//        editor.apply();
//    }
//
//    // Check if the payment has been confirmed before
//    private boolean isPaymentConfirmed() {
//        return prefs.getBoolean("isPaymentConfirmed", false);
//    }
//
//    // Enable or disable payment check
//    private void setPaymentCheckEnabled(boolean enabled) {
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putBoolean(PREF_KEY_PAYMENT_CHECK_ENABLED, enabled);
//        editor.apply();
//    }
//}
//new 21
public class LockScreenActivity extends AppCompatActivity {
    private DevicePolicyManager dpm;
    private ComponentName adminComponent;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "PaymentPrefs";
    private static final String PREF_KEY_PAYMENT_CHECK_ENABLED = "isPaymentCheckEnabled";

    String imeiNo, deviceID;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_lock_screen);
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        imeiNo = prefs.getString("imei_no","");
        deviceID = DeviceUtil.getAndroidId(this);
        apiService = RetrofitInstance.getApiService();
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(this, AdminReceiver.class);

        // Show payment confirmation dialog
//        showPaymentConfirmationDialog();

        setPaymentCheckEnabled(true);
        savePaymentConfirmed(false);
        startService(new Intent(this, PaymentCheckService.class));
        lockDevice();
    }

    private void showPaymentConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Payment Confirmation")
                .setMessage("Please confirm your payment to unlock the device.")
                .setPositiveButton("Confirm", (dialog, which) -> onPaymentConfirmed())
                .setNegativeButton("Skip for Now", (dialog, which) -> {
                    setPaymentCheckEnabled(true);
                    savePaymentConfirmed(false);
                    startService(new Intent(this, PaymentCheckService.class));
                    lockDevice();
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    public void onPaymentConfirmed() {
        Toast.makeText(this, "Payment confirmed. Device will now unlock.", Toast.LENGTH_SHORT).show();
        stopService(new Intent(this, PaymentCheckService.class));
        setPaymentCheckEnabled(false);
        savePaymentConfirmed(true);
//        setPaymentStatus(true);
//        unlockDevice();
    }

    private void lockDevice() {
        if (dpm.isAdminActive(adminComponent)) {
            Toast.makeText(this, "Locking the device due to payment not confirmed.", Toast.LENGTH_SHORT).show();
            dpm.lockNow();
        } else {
            Toast.makeText(this, "Admin permission is not active. Requesting admin permission.", Toast.LENGTH_SHORT).show();
            requestAdminPermission();
        }
    }

    private void unlockDevice() {
        if (dpm.isAdminActive(adminComponent)) {
            Toast.makeText(this, "Device unlocked successfully.", Toast.LENGTH_SHORT).show();
            finish(); // Close the lock screen activity
        } else {
            Toast.makeText(this, "Admin permission is not active.", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePaymentConfirmed(boolean confirmed) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isPaymentConfirmed", confirmed);
        editor.apply();
        setPaymentStatus(confirmed);
    }

    void setPaymentStatus(boolean enable){
        if(!imeiNo.isEmpty()){
            if(enable){
                apiService.setPaymentStatusPaid(imeiNo).enqueue(new Callback<PaymentStatusData>() {
                    @Override
                    public void onResponse(@NonNull Call<PaymentStatusData> call, @NonNull Response<PaymentStatusData> response) {
                        if(response.isSuccessful() && response.body() != null){
                            PaymentStatusData paymentStatusData = response.body();
                            Log.e("Success Paid", "MSG: Payment Status Paid/Confirmed" + response.code());
                            Toast.makeText(LockScreenActivity.this, "Payment Status Confirmed", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("API_ERROR", "Response Code: " + response.code());
                            Toast.makeText(LockScreenActivity.this, "Failed to Update Status", Toast.LENGTH_SHORT).show();                  }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PaymentStatusData> call, @NonNull Throwable t) {
                        Log.e("API_ERROR", "Error Msg: " + t.getMessage());
                        Toast.makeText(LockScreenActivity.this, "API Failure", Toast.LENGTH_SHORT).show();
                    }
                });
                unlockDevice();
            } else{

                apiService.setPaymentStatusUnPaid(imeiNo).enqueue(new Callback<PaymentStatusData>() {
                    @Override
                    public void onResponse(@NonNull Call<PaymentStatusData> call, @NonNull Response<PaymentStatusData> response) {
                        if(response.isSuccessful() && response.body() != null){
                            PaymentStatusData paymentStatusData = response.body();
                            Log.e("Success UnPaid", "MSG: Payment Status UnPaid  " + response.code());
                            Toast.makeText(LockScreenActivity.this, "Payment Status UnPaid", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("API_ERROR", "Response Code: " + response.code());
                            Toast.makeText(LockScreenActivity.this, "Failed to Update Status", Toast.LENGTH_SHORT).show();                  }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PaymentStatusData> call, @NonNull Throwable t) {
                        Log.e("API_ERROR", "Error Msg: " + t.getMessage());
                        Toast.makeText(LockScreenActivity.this, "API Failure", Toast.LENGTH_SHORT).show();
                    }
                });
                lockDevice();
            }
        } else{
            Log.e("No Data", "No Data Found : IMEI No");
            if(enable){
                unlockDevice();
            } else{
                lockDevice();
            }
        }

    }

    private boolean isPaymentConfirmed() {
        return prefs.getBoolean("isPaymentConfirmed", false);
    }

    private void setPaymentCheckEnabled(boolean enabled) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_PAYMENT_CHECK_ENABLED, enabled);
        editor.apply();
    }

    private void requestAdminPermission() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Administrator rights are required to lock/unlock the device.");
        startActivity(intent);
    }
}







