package com.alobha.sample.myapplication;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


//public class PaymentCheckService extends Service {
//
//    private DevicePolicyManager dpm;
//    private ComponentName adminComponent;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//        adminComponent = new ComponentName(this, AdminReceiver.class);
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        // Start checking payment status
//        checkPaymentStatus();
//        return START_STICKY; // Ensures the service continues running
//    }
//
//    private void checkPaymentStatus() {
//        // Mock method for checking payment status
//        boolean isPaymentDue = checkPaymentStatusFromServer();
//
//        if (isPaymentDue) {
//            lockDevice();
//        }
//    }
//
//    private boolean checkPaymentStatusFromServer() {
//        // Replace with your actual payment check logic
//        return true; // Simulating overdue payment for demo purposes
//    }
//
//    private void lockDevice() {
//        if (dpm.isAdminActive(adminComponent)) {
//            dpm.lockNow(); // Lock the device immediately
//            Toast.makeText(this, "Device locked due to overdue payment.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null; // Not using binding
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        // Cleanup if needed
//    }
//}

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

//public class PaymentCheckService extends Service {
//
//    private DevicePolicyManager dpm;
//    private ComponentName adminComponent;
//    private static final String CHANNEL_ID = "PaymentCheckServiceChannel";
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//        adminComponent = new ComponentName(this, AdminReceiver.class);
//
//        // Create a notification channel for devices running Android O and above
//        createNotificationChannel();
//
//        // Start the service in the foreground
//        startForeground(1, createNotification());
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        // Start checking payment status
//        checkPaymentStatus();
//        return START_STICKY; // Ensures the service continues running
//    }
//
//    private void checkPaymentStatus() {
//        // Mock method for checking payment status
//        boolean isPaymentDue = checkPaymentStatusFromServer();
//
//        if (isPaymentDue) {
//            lockDevice();
//        }
//    }
//
//    private boolean checkPaymentStatusFromServer() {
//        // Replace with your actual payment check logic
//        return true; // Simulating overdue payment for demo purposes
//    }
//
//    private void lockDevice() {
//        if (dpm.isAdminActive(adminComponent)) {
//            dpm.lockNow(); // Lock the device immediately
//            Toast.makeText(this, "Device locked due to overdue payment.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null; // Not using binding
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        // Cleanup if needed
//    }
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel serviceChannel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "Payment Check Service Channel",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            if (manager != null) {
//                manager.createNotificationChannel(serviceChannel);
//            }
//        }
//    }
//
//    private Notification createNotification() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            return new Notification.Builder(this, CHANNEL_ID)
//                    .setContentTitle("Payment Check Service")
//                    .setContentText("Checking payment status...")
//                    .setSmallIcon(R.drawable.ic_launcher_background) // Replace with your app's notification icon
//                    .build();
//        }
//        return null;
//    }
//}

//import android.app.DevicePolicyManager;
//
//import androidx.core.app.NotificationCompat;
//public class PaymentCheckService extends Service {
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        createNotificationChannel(); // Create the notification channel when the service is created
//    }
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    "YOUR_CHANNEL_ID",
//                    "Channel Name",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        // Build and start the foreground service notification here
//        Notification notification = new NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
//                .setContentTitle("Service Running")
//                .setContentText("Your service is running in the foreground")
//                .setSmallIcon(R.drawable.ic_launcher_background) // Ensure this is a valid drawable
//                .build();
//
//        startForeground(1, notification); // Pass the notification to startForeground
//
//        // Your service code...
//
//        return START_STICKY;
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null; // Return null if not binding to an activity
//    }
//}


/*
public class PaymentCheckService extends Service {

    private DevicePolicyManager dpm;
    private ComponentName adminComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(this, AdminReceiver.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start the foreground service with a notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundServiceWithNotification();
        } else {
            // For lower API levels
            startPaymentCheck();
        }
        return START_STICKY; // Ensures the service continues running
    }

    private void startForegroundServiceWithNotification() {
        // Create a notification and start the service in the foreground
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            startForeground(1, createNotification(), FOREGROUND_SERVICE_TYPE_DATA_SYNC);
        } else {
            startForeground(1, createNotification());
        }

        startPaymentCheck();
    }

    private Notification createNotification() {
        // Create a notification for the foreground service
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
                .setContentTitle("Payment Check Service")
                .setContentText("Monitoring payment status...")
                .setSmallIcon(R.drawable.ic_launcher_background) // Your notification icon
                .setPriority(NotificationCompat.PRIORITY_LOW);

        return builder.build();
    }

    private void startPaymentCheck() {
        // Mock method for checking payment status
        boolean isPaymentDue = checkPaymentStatusFromServer();

        if (isPaymentDue) {
            lockDevice();
        }
    }

    private boolean checkPaymentStatusFromServer() {
        // Replace with your actual payment check logic
        return true; // Simulating overdue payment for demo purposes
    }

    private void lockDevice() {
        if (dpm.isAdminActive(adminComponent)) {
            dpm.lockNow(); // Lock the device immediately
            Log.d("PaymentCheckService", "Device locked due to overdue payment.");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not using binding
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cleanup if needed
    }
}
*/


//attempt 10

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*import androidx.core.app.NotificationCompat;



public class PaymentCheckService extends Service {
    private DevicePolicyManager dpm;
    private ComponentName adminComponent;

    private static final String CHANNEL_ID = "YOUR_CHANNEL_ID";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
       adminComponent = new ComponentName(this, AdminReceiver.class);// Create the notification channel when the service is created
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Build and start the foreground service notification here
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Service Running")
                .setContentText("Your service is running in the foreground")
                .setSmallIcon(R.drawable.ic_launcher_background) // Ensure this is a valid drawable
                .build();

        startForeground(1, notification); // Pass the notification to startForeground

         startPaymentCheck();

        // Add your implementation here, e.g., checking payment status, syncing data, etc.
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Simulate checking payment status
                while (true) {
                    try {
                        // Simulate a delay for the task
                        Thread.sleep(5000); // Check every 5 seconds
                        checkPaymentStatus(); // Call your method to check payment status
                    } catch (InterruptedException e) {
                        // Handle exception
                        break;
                    }
                }
            }
        }).start();

        return START_STICKY;
    }

    private void startPaymentCheck() {
        // Mock method for checking payment status
        boolean isPaymentDue = checkPaymentStatusFromServer();
        Log.d(">>>>", "startPaymentCheck: "+isPaymentDue);

        if (isPaymentDue) {
            lockDevice();
        }
    }

    private void checkPaymentStatus() {
        // Mock method for checking payment status
        boolean isPaymentDue = checkPaymentStatusFromServer();


        if (isPaymentDue) {
            lockDevice();
        }
    }

    private boolean checkPaymentStatusFromServer() {
        // Replace with your actual payment check logic
        return true; // Simulating overdue payment for demo purposes
    }

    private void lockDevice() {
        if (dpm.isAdminActive(adminComponent)) {
            dpm.lockNow(); // Lock the device immediately
            // Show the Toast on the main thread
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(PaymentCheckService.this, "Device locked due to overdue payment.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not using binding
    }
}*/

//attempt 11 working
//public class PaymentCheckService extends Service {
//    private DevicePolicyManager dpm;
//    private ComponentName adminComponent;
//    private static final String CHANNEL_ID = "YOUR_CHANNEL_ID";
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        createNotificationChannel();
//        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//        adminComponent = new ComponentName(this, AdminReceiver.class);
//    }
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "Channel Name",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Service Running")
//                .setContentText("Your service is running in the foreground")
//                .setSmallIcon(R.drawable.ic_launcher_background) // Ensure this is a valid drawable
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(createPendingIntent()) // Set the intent for notification click
//                .build();
//
//        startForeground(1, notification);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(5000);
//                        checkPaymentStatus();
//                    } catch (InterruptedException e) {
//                        break;
//                    }
//                }
//            }
//        }).start();
//
//        return START_STICKY;
//    }
//
//    private PendingIntent createPendingIntent() {
//        Intent intent = new Intent(this, LockScreenActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE); // Use FLAG_IMMUTABLE
//    }
//
//    private void checkPaymentStatus() {
//        boolean isPaymentDue = checkPaymentStatusFromServer();
//        if (isPaymentDue) {
//            lockDevice();
//        }
//    }
//
//    private boolean checkPaymentStatusFromServer() {
//        return true; // Simulating overdue payment
//    }
//
//    private void lockDevice() {
//        if (dpm.isAdminActive(adminComponent)) {
//            dpm.lockNow();
//            new Handler(Looper.getMainLooper()).post(() -> {
//                Toast.makeText(PaymentCheckService.this, "Device locked due to overdue payment.", Toast.LENGTH_SHORT).show();
//            });
//        }
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//}

//attempt 12 working f9
//public class PaymentCheckService extends Service {
//    private DevicePolicyManager dpm;
//    private ComponentName adminComponent;
//    private static final String CHANNEL_ID = "YOUR_CHANNEL_ID";
//
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        createNotificationChannel();
//        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//        adminComponent = new ComponentName(this, AdminReceiver.class);
//    }
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "Channel Name",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        startForegroundNotification(); // Call method to start the foreground notification
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(9000);
//                        checkPaymentStatus();
//                    } catch (InterruptedException e) {
//                        break;
//                    }
//                }
//            }
//        }).start();
//
//        return START_STICKY;
//    }
//
//    private void startForegroundNotification() {
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Service Running")
//                .setContentText("Your service is running in the foreground")
//                .setSmallIcon(R.drawable.ic_launcher_background) // Ensure this is a valid drawable
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(createPendingIntent())
//                .build();
//
//        startForeground(1, notification);
//    }
//
//    private PendingIntent createPendingIntent() {
//        Intent intent = new Intent(this, LockScreenActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//    }
//
//    private void checkPaymentStatus() {
//        boolean isPaymentDue = checkPaymentStatusFromServer();
//        if (isPaymentDue) {
//            lockDevice();
//        }
//    }
//
//    private boolean checkPaymentStatusFromServer() {
//        return true; // Simulating overdue payment
//    }
//
//    private void lockDevice() {
//        if (dpm.isAdminActive(adminComponent)) {
//            dpm.lockNow();
//            new Handler(Looper.getMainLooper()).post(() -> {
//                Toast.makeText(PaymentCheckService.this, "Device locked due to overdue payment.", Toast.LENGTH_SHORT).show();
//            });
//        }
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//}

//attempt 13
/*public class PaymentCheckService extends Service {
    private DevicePolicyManager dpm;
    private ComponentName adminComponent;
    private static final String CHANNEL_ID = "YOUR_CHANNEL_ID";
    private boolean isPaymentCheckEnabled = false; // Flag to control service operation

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(this, AdminReceiver.class);
    }

        private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Method to enable or disable payment checks
    public void setPaymentCheckEnabled(boolean enabled) {
        isPaymentCheckEnabled = enabled;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Service Running")
                .setContentText("Your service is running in the foreground")
                .setSmallIcon(R.drawable.ic_launcher_background) // Ensure this is a valid drawable
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(createPendingIntent()) // Set the intent for notification click
                .build();

        startForeground(1, notification);

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    if (isPaymentCheckEnabled) { // Check flag before proceeding
                        checkPaymentStatus();
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();

        return START_STICKY;
    }
        private void checkPaymentStatus() {
        boolean isPaymentDue = checkPaymentStatusFromServer();
        if (isPaymentDue) {
            lockDevice();
        }
    }
        private void lockDevice() {
        if (dpm.isAdminActive(adminComponent)) {
            dpm.lockNow();
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(PaymentCheckService.this, "Device locked due to overdue payment.", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private boolean checkPaymentStatusFromServer() {
        return true; // Simulating overdue payment
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, LockScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }
}*/
//attempt 14
/*public class PaymentCheckService extends Service {

    // Device Policy Manager and admin component for locking the device
    private DevicePolicyManager dpm;
    private ComponentName adminComponent;

    // Notification channel ID and SharedPreferences constants
    private static final String CHANNEL_ID = "YOUR_CHANNEL_ID";
    private static final String PREFS_NAME = "app_prefs";
    private static final String PREF_KEY_PAYMENT_CHECK_ENABLED = "isPaymentCheckEnabled";

    // Flag to track if payment check is enabled
    private boolean isPaymentCheckEnabled = false;

    // Executor service for background tasks
    private ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create notification channel for foreground service
        createNotificationChannel();

        // Initialize Device Policy Manager and admin component
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(this, AdminReceiver.class);

        // Load payment check status from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isPaymentCheckEnabled = prefs.getBoolean(PREF_KEY_PAYMENT_CHECK_ENABLED, false);

        // Log the current status of payment check
        Log.e("PaymentCheckService", "isPaymentCheckEnabled: " + isPaymentCheckEnabled);

        // Initialize executor service for background payment checks
        executorService = Executors.newSingleThreadExecutor();
    }

    // Create a notification channel for Android O and above
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Payment Check Service",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Load payment check status from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isPaymentCheckEnabled = prefs.getBoolean(PREF_KEY_PAYMENT_CHECK_ENABLED, false);

        // Create a notification for foreground service
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Payment Service Running")
                .setContentText("Monitoring payment status.")
                .setSmallIcon(R.drawable.ic_launcher_background)  // Replace with valid icon
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(createPendingIntent())  // Pending intent for notification click
                .build();

        // Start foreground service with the notification immediately
        startForeground(1, notification);

        // Log the current status of payment check
        Log.e("PaymentCheckService", "isPaymentCheckEnabled: " + isPaymentCheckEnabled);

        // Start background task to periodically check payment status
        executorService.execute(() -> {
            while (true) {
                try {
                    // Check payment status every 5 seconds
                    Thread.sleep(50000);

                    // Reload the payment check status from SharedPreferences
//                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//                    isPaymentCheckEnabled = prefs.getBoolean(PREF_KEY_PAYMENT_CHECK_ENABLED, false);
                    Log.d("onStartCommand: ", String.valueOf(isPaymentCheckEnabled));
                    checkPaymentStatus();


                    // If payment check is enabled, check payment status
//                    if (isPaymentCheckEnabled) {
//                        checkPaymentStatus();
//                    } else {
//                        // Stop the service if payment is confirmed
//                        stopSelf();
//                        break;
//                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupt status
                    break;
                }
            }
        });

        return START_STICKY; // Keep the service running
    }



    // Method to check the payment status from the server
    private void checkPaymentStatus() {
        boolean isPaymentDue = checkPaymentStatusFromServer();

        // Lock the device if payment is due
        if (isPaymentDue) {
            lockDevice();
        }
    }

    // Simulated method to check payment status from server
    private boolean checkPaymentStatusFromServer() {
        // In a real implementation, you would make an API call here
        return true; // Simulate overdue payment for now
    }

    // Lock the device if payment is overdue
    private void lockDevice() {
        if (dpm.isAdminActive(adminComponent)) {
            dpm.lockNow(); // Lock the device immediately

            // Show a Toast on the main thread indicating the device is locked
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(PaymentCheckService.this, "Device locked due to overdue payment.", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // This service doesn't support binding
    }

    // Create a PendingIntent for notification click to open LockScreenActivity
    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, LockScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdownNow(); // Terminate background task
        }
    }

    // Method to enable or disable payment check and save the status to SharedPreferences
    public void setPaymentCheckEnabled(boolean enabled) {
        isPaymentCheckEnabled = enabled;

        // Save the updated status to SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_PAYMENT_CHECK_ENABLED, enabled);
        editor.apply();
    }
}*/
//attempt 15 fine

/*public class PaymentCheckService extends Service {

    private DevicePolicyManager dpm;
    private ComponentName adminComponent;

    private static final String CHANNEL_ID = "YOUR_CHANNEL_ID";
    private static final String PREFS_NAME = "PaymentPrefs";
    private static final String PREF_KEY_PAYMENT_CHECK_ENABLED = "isPaymentCheckEnabled";

    private boolean isPaymentCheckEnabled = false;

    private ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();

        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(this, AdminReceiver.class);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isPaymentCheckEnabled = prefs.getBoolean(PREF_KEY_PAYMENT_CHECK_ENABLED, false);

        Log.e("PaymentCheckService", "isPaymentCheckEnabled: " + isPaymentCheckEnabled);

        executorService = Executors.newSingleThreadExecutor();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Payment Check Service",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isPaymentCheckEnabled = prefs.getBoolean(PREF_KEY_PAYMENT_CHECK_ENABLED, false);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Payment Service Running")
                .setContentText("Monitoring payment status.")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(createPendingIntent())
                .build();

        startForeground(1, notification);

        Log.e("PaymentCheckService", "isPaymentCheckEnabled: " + isPaymentCheckEnabled);

        executorService.execute(() -> {
            while (isPaymentCheckEnabled) {
                try {
                    Thread.sleep(100000);
                    checkPaymentStatus();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        return START_STICKY;
    }

    private void checkPaymentStatus() {
        boolean isPaymentDue = checkPaymentStatusFromServer();

        if (isPaymentDue) {
            lockDevice();
        }
    }

    private boolean checkPaymentStatusFromServer() {
        return true; // Simulating payment due for testing
    }

    private void lockDevice() {
        if (dpm.isAdminActive(adminComponent)) {
            dpm.lockNow();
            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(PaymentCheckService.this, "Device locked due to overdue payment.", Toast.LENGTH_SHORT).show()
            );
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, LockScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}*/
//attempt 20
//public class PaymentCheckService extends Service {
//
//    private DevicePolicyManager dpm;
//    private ComponentName adminComponent;
//
//    private static final String CHANNEL_ID = "YOUR_CHANNEL_ID";
//    private static final String PREFS_NAME = "PaymentPrefs";
//    private static final String PREF_KEY_PAYMENT_CHECK_ENABLED = "isPaymentCheckEnabled";
//
//    private boolean isPaymentCheckEnabled = false;
//    private ExecutorService executorService;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        createNotificationChannel();
//
//        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//        adminComponent = new ComponentName(this, AdminReceiver.class);
//
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        isPaymentCheckEnabled = prefs.getBoolean(PREF_KEY_PAYMENT_CHECK_ENABLED, false);
//
//        executorService = Executors.newSingleThreadExecutor();
//    }
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "Payment Check Service",
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        isPaymentCheckEnabled = prefs.getBoolean(PREF_KEY_PAYMENT_CHECK_ENABLED, false);
//
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Payment Service Running")
//                .setContentText("Monitoring payment status.")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(createPendingIntent())
//                .build();
//
//        startForeground(1, notification);
//
//        executorService.execute(() -> {
//            while (isPaymentCheckEnabled) {
//                try {
//                    Thread.sleep(3000);
//                    checkPaymentStatus();
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                    break;
//                }
//            }
//        });
//
//        return START_STICKY;
//    }
//
//    private void checkPaymentStatus() {
//        boolean isPaymentDue = checkPaymentStatusFromServer();
//
//        if (isPaymentDue) {
//            lockDevice();
//        }
//    }
//
//    private boolean checkPaymentStatusFromServer() {
//        return true; // Simulating payment due for testing
//    }
//
//    private void lockDevice() {
//        if (dpm.isAdminActive(adminComponent)) {
//            dpm.lockNow();
//        }
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    private PendingIntent createPendingIntent() {
//        Intent intent = new Intent(this, LockScreenActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (executorService != null) {
//            executorService.shutdownNow();
//        }
//    }
//}

//attempt 21

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

//public class PaymentCheckService extends Service {
//    private static final String CHANNEL_ID = "PaymentCheckChannel";
//    private static final int NOTIFICATION_ID = 1;
//    private ComponentName adminComponent;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        createNotificationChannel();
//        startForeground(NOTIFICATION_ID, getNotification());
//        adminComponent = new ComponentName(this, AdminReceiver.class);
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    private Notification getNotification() {
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
//
//        return new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Payment Check Service")
//                .setContentText("Checking payment status...")
//                .setSmallIcon(R.drawable.ic_launcher_background) // Use your own icon here
//                .setContentIntent(pendingIntent)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .build();
//    }
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel serviceChannel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "Payment Check Service Channel",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            if (manager != null) {
//                manager.createNotificationChannel(serviceChannel);
//            }
//        }
//    }
//
//    // Method to check payment status periodically
//    private void checkPaymentStatus() {
//        boolean paymentConfirmed = checkPayment();
//        if (!paymentConfirmed) {
//            lockDevice();
//        }
//    }
//
//    private boolean checkPayment() {
//        // Simulate a payment check
//        return false; // Assume payment is not confirmed for demo
//    }
//
//    private void lockDevice() {
//        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//        if (dpm.isAdminActive(adminComponent)) {
//            Log.d("PaymentCheckService", "Locking device due to unpaid EMI.");
//            dpm.lockNow();
//        } else {
//            Log.e("PaymentCheckService", "Admin permission not active.");
//        }
//    }
//}

// last and fine
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.alobha.sample.myapplication.model.GetStatusData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentCheckService extends Service {
    private static final String PREFS_NAME = "PaymentPrefs"; // Use the same name as LockScreenActivity
    private static final String PREF_KEY_PAYMENT_CHECK_ENABLED = "isPaymentCheckEnabled";
    private static final String CHANNEL_ID = "PaymentCheckServiceChannel";

    private boolean isPaymentCheckEnabled;
    private ExecutorService executorService;
    private ComponentName adminComponent;

    private String imeiNo, deviceID;
    ApiService apiService;
    boolean paymentConfirmed = false;



    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel(); // Create the notification channel for API 26+
        executorService = Executors.newSingleThreadExecutor(); // Initialize the executor service
        adminComponent = new ComponentName(this, AdminReceiver.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isPaymentCheckEnabled = prefs.getBoolean(PREF_KEY_PAYMENT_CHECK_ENABLED, false);
        imeiNo = prefs.getString("imei_no","");
        Log.d("ImeiValue","IMEI NO >> "+ imeiNo);
        deviceID = DeviceUtil.getAndroidId(this);
        Log.d("PaymentCheckService", "isPaymentCheckEnabled: " + isPaymentCheckEnabled);
        apiService = RetrofitInstance.getApiService();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Payment Service Running")
                .setContentText("Monitoring payment status.")
                .setSmallIcon(R.drawable.ic_launcher_background) // Replace with your icon
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(createPendingIntent())
                .build();

        startForeground(1, notification);

        if (isPaymentCheckEnabled) {
            executorService.execute(() -> {
                while (isPaymentCheckEnabled) {
                    try {
                        Thread.sleep(3000); // Sleep for 3 seconds
                        checkPaymentStatus(); // Call your payment status check logic here
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore interrupt status
                        break;
                    }
                }
            });
        }

        return START_STICKY;
    }

    private PendingIntent createPendingIntent() {
        Intent notificationIntent = new Intent(this, LockScreenActivity.class); // Change to your activity
        return PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
    }

    // Method to check payment status periodically
    private void checkPaymentStatus() {
        paymentConfirmed = true;
        Log.d("PaymentCheckService","IMEI NO >> "+imeiNo);


        apiService.getPaymentStatus(imeiNo).enqueue(new Callback<GetStatusData>() {
            @Override
            public void onResponse(@NonNull Call<GetStatusData> call, @NonNull Response<GetStatusData> response) {
                if(response.isSuccessful() && response.body() != null){
                    GetStatusData getStatusData = response.body();
                    String paymentStatus = getStatusData.getDevice().getPayment_status();
                    if(paymentStatus != null && !paymentStatus.isEmpty()){
                        paymentConfirmed = paymentStatus.equals("1");
                    }

                    Log.d("PaymentCheckService","Msg >> "+getStatusData.getMessage()+" Payment Status >>>"+getStatusData.getDevice().getPayment_status());
//                    Toast.makeText(PaymentCheckService.this, getStatusData.getMessage(), Toast.LENGTH_SHORT).show();
                    if (!paymentConfirmed) {
                        lockDevice();
                    }
                } else {
                    Log.d("PaymentCheckService","API ERROR : ERROR_CODE >> "+response.code());
//                    Toast.makeText(PaymentCheckService.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

//                if (!paymentConfirmed) {
//                    lockDevice();
//                } else{
////                    executorService.shutdownNow();
////                    unlockDevice();
//
//                }
                Log.d("PaymentCheckService", "Payment confirmed: " + paymentConfirmed);
            }

            @Override
            public void onFailure(@NonNull Call<GetStatusData> call, @NonNull Throwable t) {
                Log.d("PaymentCheckService","API ERROR : MSG >> "+t.getMessage());
//                Toast.makeText(PaymentCheckService.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
//                if (!paymentConfirmed) {
//                    lockDevice();
//                }
                Log.d("PaymentCheckService", "Payment confirmed: " + paymentConfirmed);
            }
        });




//        if (!paymentConfirmed) {
//            lockDevice();
//        }
    }

    private boolean checkPayment() {
        // Simulate a payment check
        return false; // Assume payment is not confirmed for demo
    }

    private void lockDevice() {
        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (dpm.isAdminActive(adminComponent)) {
            Log.d("PaymentCheckService", "Locking device due to unpaid EMI.");
            dpm.lockNow();

        } else {
            Log.e("PaymentCheckService", "Admin permission not active. Attempting to activate admin.");
            requestAdminPermission();
        }
    }

    private void requestAdminPermission() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Admin permission required to lock device.");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Required when starting activity from service
        startActivity(intent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Payment Check Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // Return null if not binding to a client
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdownNow(); // Clean up the executor service
    }

//    private void unlockDevice() {
//        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//        if (dpm.isAdminActive(adminComponent)) {
//            Toast.makeText(this, "Device unlocked successfully.", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "Admin permission is not active.", Toast.LENGTH_SHORT).show();
//        }
//    }
}






