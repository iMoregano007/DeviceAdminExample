package com.alobha.sample.myapplication;


import static com.alobha.sample.myapplication.utils.ServiceTrackerKt.getServiceState;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.alobha.sample.myapplication.model.DeviceCreatedResponse;
import com.alobha.sample.myapplication.model.GetStatusData;
import com.alobha.sample.myapplication.utils.Actions;
import com.alobha.sample.myapplication.utils.BatteryOptimizationHelper;
import com.alobha.sample.myapplication.utils.FactoryResetManager;
import com.alobha.sample.myapplication.utils.ServiceState;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ENABLE_ADMIN = 1;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 2001;
    private static final int REQUEST_CODE_FOREGROUND_SERVICE = 2002;
    private static final int REQUEST_CODE_PHONE_STATE = 100;


    private DevicePolicyManager dpm;
    private ComponentName adminComponent;

    SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "PaymentPrefs";
    ScrollView formView;
    EditText name;
    EditText email;
    EditText phoneNo;
    EditText imeiNo;

    String imeiNoV;
    Button addDevice;
    String deviceID;
    ApiService apiService;
    boolean deviceRegistered;
    boolean lockDevice;
    Button factoryResetBtn;
    Button removeDeviceOwner;
    Button addRestrictionBtn;
    Button removeRestrictionBtn;

    LinearLayout inputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Device Policy Manager and Admin Component

        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(this, AdminReceiver.class);

//        formView = findViewById(R.id.formView);
//        name = findViewById(R.id.name);
//        email = findViewById(R.id.emailID);
//        phoneNo = findViewById(R.id.phoneNo);
        imeiNo = findViewById(R.id.imeiNo);
        factoryResetBtn = findViewById(R.id.factoryResetButton);
        addDevice = findViewById(R.id.addDevice);
        inputView = findViewById(R.id.inputDetails);
        addRestrictionBtn = findViewById(R.id.addRestrictionsBtn);
        removeRestrictionBtn = findViewById(R.id.removeRestrictions);
        removeDeviceOwner = findViewById(R.id.removeDeviceOwner);
//        checkReadPhoneStatePermission();
        apiService = RetrofitInstance.getApiService();
        deviceID = DeviceUtil.getAndroidId(MainActivity.this);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        deviceRegistered = sharedPreferences.getBoolean("device_registered", false);
        Log.d("deviceID", "onCreate: >>>"+deviceID);
        imeiNoV = sharedPreferences.getString("imei_no","");


// working one time request
        // Create a OneTimeWorkRequest for PaymentCheckWorker
//        WorkRequest paymentCheckWorkRequest = new OneTimeWorkRequest.Builder(PaymentCheckWorker.class).build();
//
//        // Enqueue the work
//        WorkManager.getInstance(this).enqueue(paymentCheckWorkRequest);

        PeriodicWorkRequest paymentCheckPeriodicWorkRequest =
                new PeriodicWorkRequest.Builder(PaymentCheckWorker.class, 15, TimeUnit.MINUTES).setInitialDelay(5, TimeUnit.MINUTES) // Minimum period is 15 minutes
                        .build();


// Enqueue the periodic work with a unique name


        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "PaymentCheckWork", // Unique work name
                ExistingPeriodicWorkPolicy.UPDATE, // Keep the previous work if it exists
                paymentCheckPeriodicWorkRequest
        );

        WorkManager.getInstance(MainActivity.this).getWorkInfosForUniqueWorkLiveData("PaymentCheckWork")
                .observe(this, workInfos -> {
                    for (WorkInfo workInfo : workInfos) {
                        Log.d("PaymentCheckWork", "State: " + workInfo.getState());
                    }
                });

        factoryResetBtn.setOnClickListener(v -> {
//            dpm.clearDeviceOwnerApp(getPackageName());
            FactoryResetManager factoryResetManager = new FactoryResetManager(MainActivity.this,adminComponent);
            factoryResetManager.initiateFactoryReset();
        });

        removeDeviceOwner.setOnClickListener(v -> {
            dpm.clearDeviceOwnerApp(getPackageName());

//            FactoryResetManager factoryResetManager = new FactoryResetManager(MainActivity.this,adminComponent);
//            factoryResetManager.initiateFactoryReset();
        });

        addRestrictionBtn.setOnClickListener(v ->{
            addRestrictions();
        });

        removeRestrictionBtn.setOnClickListener(v->{
            removeRestrictions();
        });

//        getUnrestrictedUsagePermission();

        BatteryOptimizationHelper.checkAndRequestBatteryOptimization(MainActivity.this);
//        For Testing Purpose

        if(!imeiNoV.isEmpty()){
//            inputView.setVisibility(View.GONE);
            showInputView(false);
            getPaymentStatus(imeiNoV);
            actionOnService(Actions.START);
        } else {
//            inputView.setVisibility(View.VISIBLE);
            showInputView(true);
        }


        addDevice.setOnClickListener(v -> {
//            createDevice();
            String imeiString = imeiNo.getText().toString().trim();
            if(imeiString.isEmpty()){
                Toast.makeText(this, "Please Enter IMEI No", Toast.LENGTH_SHORT).show();
            } else {
                sharedPreferences.edit().putString("imei_no",imeiString ).commit();
//                lockDevice = true;
//                moveForward();
                imeiNoV = imeiString;
                getPaymentStatus(imeiString);
                actionOnService(Actions.START);
            }


        });

        // Check if the admin permission is granted
//        for now only
//        if (!dpm.isAdminActive(adminComponent)) {
//            requestAdminPermission();
//        } else {
//            // Check for notification permissions
//            checkNotificationPermission();
//        }
    }

    void showInputView(boolean visible){
        if(visible){
            inputView.setVisibility(View.VISIBLE);
            factoryResetBtn.setVisibility(View.GONE);
            removeDeviceOwner.setVisibility(View.GONE);
            removeRestrictionBtn.setVisibility(View.GONE);
            addRestrictionBtn.setVisibility(View.GONE);
        } else {
            inputView.setVisibility(View.GONE);
            factoryResetBtn.setVisibility(View.VISIBLE);
            removeDeviceOwner.setVisibility(View.VISIBLE);
            removeRestrictionBtn.setVisibility(View.VISIBLE);
            addRestrictionBtn.setVisibility(View.VISIBLE);
        }
    }

    void getPaymentStatus(String imeiNoV){
        apiService.getPaymentStatus(imeiNoV).enqueue(new Callback<GetStatusData>() {
            @Override
            public void onResponse(@NonNull Call<GetStatusData> call, @NonNull Response<GetStatusData> response) {
                if(response.isSuccessful() && response.body() != null){
                    GetStatusData getStatusData = response.body();
                    String paymentStatus = getStatusData.getDevice().getPayment_status();
                    deviceRegistered = true;
                    if(paymentStatus != null && !paymentStatus.isEmpty()){
                        lockDevice = paymentStatus.equals("1");
                    } else {
                        lockDevice = false;
                    }
                    Log.d("PaymentCheckService","Msg >> "+getStatusData.getMessage()+" Payment Status >>>"+getStatusData.getDevice().getPayment_status());
                    Toast.makeText(MainActivity.this, getStatusData.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("PaymentCheckService","API ERROR  >>>: ERROR_CODE >> "+response.code());
                    Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    deviceRegistered = false;
                }

                moveForward();
            }

            @Override
            public void onFailure(@NonNull Call<GetStatusData> call, @NonNull Throwable t) {
                Log.d("PaymentCheckService","API ERROR : MSG >> "+t.getMessage());
                Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                deviceRegistered = false;
                moveForward();
            }
        });

    }

    void moveForward(){
        if(!imeiNoV.isEmpty()){
//            inputView.setVisibility(View.GONE);
            showInputView(false);
            Log.d("ImeiValue","IMEI NO >> "+ imeiNoV);

            checkDpm();

//            if(!lockDevice){
////                actionOnService(Actions.START);
//                checkDpm();
//            }

        } else {
//            inputView.setVisibility(View.VISIBLE);
            showInputView(true);
        }
    }


    void createDevice(){
        String nameV = name.getText().toString().trim();
        String emailV = email.getText().toString().trim();
        String phoneV = phoneNo.getText().toString().trim();
        String imeiV = imeiNo.getText().toString().trim();


        if(nameV.isEmpty() || !validateEmail(emailV) || phoneV.isEmpty() || imeiV.isEmpty()){
            Toast.makeText(MainActivity.this, "Please Enter Necessary Data" , Toast.LENGTH_SHORT).show();

        } else {
            Log.d("createDeviceData", "createDevice: >>>"+"nameV>>>"+nameV+">> emailV >>>"+emailV+" >> phone>>>"+phoneV+">> deviceID >>"+imeiV);
            Toast.makeText(MainActivity.this, "nameV>>>"+nameV+">> emailV >>>"+emailV+" >> phone>>>"+phoneV+">> deviceID >>"+deviceID , Toast.LENGTH_SHORT).show();

            apiService.createDevice(nameV, emailV, imeiV, phoneV, "1","1").enqueue(new Callback<DeviceCreatedResponse>() {
                @SuppressLint("ApplySharedPref")
                @Override
                public void onResponse(@NonNull Call<DeviceCreatedResponse> call, @NonNull Response<DeviceCreatedResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        DeviceCreatedResponse deviceCreatedResponse = response.body();
                        Log.d("API_RESPONSE", "Device Created: " + deviceCreatedResponse.getMessage() + ">>Imei no >>"+deviceCreatedResponse.getDevice().getImei_no());
                        Toast.makeText(MainActivity.this, "Device Created: " + deviceCreatedResponse.getMessage() + ">>Imei no >>"+deviceCreatedResponse.getDevice().getImei_no(), Toast.LENGTH_SHORT).show();
                        formView.setVisibility(View.GONE);
                        sharedPreferences.edit().putBoolean("device_registered", true).commit();
                        sharedPreferences.edit().putString("imei_no", deviceCreatedResponse.getDevice().getImei_no()).commit();
                        actionOnService(Actions.START);
                        checkDpm();

                    } else {
                        Log.e("API_ERROR", "Response Code: " + response.code());
                        Toast.makeText(MainActivity.this, "Failed to create device", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DeviceCreatedResponse> call, @NonNull Throwable t) {
                    Log.e("API_FAILURE", "Error: " + t.getMessage());
                    Toast.makeText(MainActivity.this, "Network error, please try again", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    private void actionOnService(Actions action) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) {
            return;
        }
        Intent intent = new Intent(this, EndlessService.class);
        intent.setAction(action.name());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("StartEndlessService", "actionOnService: >>> Starting the service in >=26 Mode");
//            log("Starting the service in >=26 Mode");
            startForegroundService(intent);
            return;
        }
//        log("Starting the service in < 26 Mode");
        Log.d("StartEndlessService", "actionOnService: >>> Starting the service in < 26 Mode");

        startService(intent);
    }


    void checkDpm(){
        if (!dpm.isAdminActive(adminComponent)) {
            requestAdminPermission();
        } else {
            // Check for notification permissions
//            checkNotificationPermission();
            checkDeviceOwner();
        }
    }

    void addRestrictions(){
        if (dpm.isDeviceOwnerApp(getPackageName())) {
            // Perform device management actions here
            Toast.makeText(this, "App is Device Owner", Toast.LENGTH_SHORT).show();
//            dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_FACTORY_RESET);
//            dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_SAFE_BOOT);
//            dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_INSTALL_APPS);

            try{
                dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_OUTGOING_CALLS);
//                dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_CAMERA_TOGGLE);
                dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_UNINSTALL_APPS);
                dpm.setCameraDisabled(adminComponent,true);
//                dpm.setLockTaskPackages(adminComponent, new String[]{getPackageName()});
//                enableKioskMode();
                disableChrome();
            } catch(Exception e){
                Log.d("Restrictions", "addRestrictions: exc"+e.getMessage());
            }


//            checkNotificationPermission();
//            enableKioskMode(); // Example function to enable Kiosk Mode
        }
    }

    void enableKioskMode(){
        try{
            startLockTask();
        } catch (Exception e){
            Log.d("Restrictions", "enableKioskMode: exc >>"+e.getMessage());
        }
//        startLockTask();
    }

    void disableKioskMode(){
        try{
            stopLockTask();
        } catch (Exception e){
            Log.d("Restrictions", "disableKioskMode: exc >>"+e.getMessage());
        }
//        startLockTask();
    }


    void removeRestrictions(){
        if (dpm.isDeviceOwnerApp(getPackageName())) {
            // Perform device management actions here
            Toast.makeText(this, "App is Device Owner", Toast.LENGTH_SHORT).show();
//            dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_FACTORY_RESET);
//            dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_SAFE_BOOT);
//            dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_INSTALL_APPS);
            try{
                dpm.clearUserRestriction(adminComponent, UserManager.DISALLOW_OUTGOING_CALLS);
//                dpm.clearUserRestriction(adminComponent, UserManager.DISALLOW_CAMERA_TOGGLE);
                dpm.clearUserRestriction(adminComponent, UserManager.DISALLOW_UNINSTALL_APPS);
                dpm.setCameraDisabled(adminComponent,false);
//                disableKioskMode();
                enableChrome();

//                dpm.cle
//                dpm.clearLockTaskPackages(adminComponentName);

            }catch (Exception e){
                Log.d("Restrictions", "removeRestrictions: exc>>"+e.getMessage());
            }


//            checkNotificationPermission();
//            enableKioskMode(); // Example function to enable Kiosk Mode
        }
    }

    public void disableChrome() {
        try {
            dpm.setApplicationHidden(adminComponent, "com.android.chrome", true);
            Toast.makeText(this, "Chrome disabled!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Enable Chrome
    public void enableChrome() {
        try {
            dpm.setApplicationHidden(adminComponent, "com.android.chrome", false);
            Toast.makeText(this, "Chrome enabled!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void checkDeviceOwner(){
        if (dpm.isDeviceOwnerApp(getPackageName())) {
            // Perform device management actions here
            Toast.makeText(this, "App is Device Owner", Toast.LENGTH_SHORT).show();
            dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_FACTORY_RESET);
            dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_SAFE_BOOT);
//            dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_INSTALL_APPS);
//            dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_OUTGOING_CALLS);
//            dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_CAMERA_TOGGLE);
//            dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_UNINSTALL_APPS);

            checkNotificationPermission();
//            enableKioskMode(); // Example function to enable Kiosk Mode
        } else {
            Toast.makeText(this, "App is not Device Owner", Toast.LENGTH_SHORT).show();
            checkNotificationPermission();
        }
    }


    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermission();
            } else {
                // Permission granted, check foreground service permission
                checkForegroundServicePermission();
            }
        } else {
            // No need for POST_NOTIFICATIONS permission in lower versions
            checkForegroundServicePermission();
        }
    }

    private void requestNotificationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
    }

    private void checkForegroundServicePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request foreground service permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.FOREGROUND_SERVICE}, REQUEST_CODE_FOREGROUND_SERVICE);
            } else {
                // Start the service if permission is granted
                startPaymentCheckService();
                showLockScreen();
            }
        } else {
            // For lower versions, just start the service
            startPaymentCheckService();
            showLockScreen();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkForegroundServicePermission();
            } else {
                Toast.makeText(this, "Notification permission required to run the service.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_FOREGROUND_SERVICE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startPaymentCheckService();
                showLockScreen();
            } else {
                Toast.makeText(this, "Foreground service permission required to run the service.", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private void stop

    private void startPaymentCheckService() {
        Intent serviceIntent = new Intent(this, PaymentCheckService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void requestAdminPermission() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "We need admin permissions to lock your phone if EMI payment is overdue.");
        startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ENABLE_ADMIN) {
            if (resultCode == Activity.RESULT_OK) {
//                checkNotificationPermission();
                checkDeviceOwner();
            } else {
                Toast.makeText(this, "Admin permission required to lock the device", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showLockScreen() {
        if(!lockDevice){
            Intent intent = new Intent(this, LockScreenActivity.class);
            startActivity(intent);
        }

    }

    boolean validateEmail(String email) {
//        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    void getUnrestrictedUsagePermission(){
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
            Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            startActivity(intent);
        }
    }
}





