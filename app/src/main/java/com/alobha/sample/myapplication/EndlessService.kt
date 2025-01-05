package com.alobha.sample.myapplication

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.alobha.sample.myapplication.model.GetStatusData
import com.alobha.sample.myapplication.utils.Actions
import com.alobha.sample.myapplication.utils.ServiceState
import com.alobha.sample.myapplication.utils.setServiceState
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class EndlessService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false

    private var LOG_TAG = "EndlessService";

    private val apiService = RetrofitInstance.getApiService()
    var imeiNo = "";
    private var adminComponent: ComponentName? = null

    override fun onBind(intent: Intent): IBinder? {
//        log("Some component want to bind with the service")
        Log.d(LOG_TAG, "onBind: >>>Some component want to bind with the service")
        // We don't provide binding, so return null
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        log("onStartCommand executed with startId: $startId")
        Log.d(LOG_TAG, "onStartCommand: >>> executed with startId: $startId")
        imeiNo = applicationContext.getSharedPreferences("PaymentPrefs", MODE_PRIVATE).getString("imei_no","")
            .toString()
        adminComponent = ComponentName(this, AdminReceiver::class.java)
        if (intent != null) {
            val action = intent.action
//            log("using an intent with action $action")
            Log.d(LOG_TAG, "onStartCommand: using an intent with action $action")
            when (action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
                else -> Log.d(LOG_TAG, "onStartCommand: This should never happen. No action in the received intent")
//                    log("This should never happen. No action in the received intent")
            }
        } else {
//            log(
//                "with a null intent. It has been probably restarted by the system."
//            )
            Log.d(LOG_TAG, "onStartCommand: >>>with a null intent. It has been probably restarted by the system. ")
        }
        // by returning this we make sure the service is restarted if the system kills the service
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
//        log("The service has been created".toUpperCase())
        Log.d(LOG_TAG, "onCreate: >>> The service has been created")
        val notification = createNotification()
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
//        log("The service has been destroyed".toUpperCase())
        Log.d(LOG_TAG, "onDestroy: The service has been destroyed")
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show()
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext, EndlessService::class.java).also {
            it.setPackage(packageName)
        };
        val restartServicePendingIntent: PendingIntent = PendingIntent.getService(this, 1, restartServiceIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE);
        applicationContext.getSystemService(Context.ALARM_SERVICE);
        val alarmService: AlarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager;
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);
    }

    private fun startService() {
        if (isServiceStarted) return
//        log("Starting the foreground service task")
        Log.d(LOG_TAG, "startService: >>>Starting the foreground service task")
        Toast.makeText(this, "Service starting its task", Toast.LENGTH_SHORT).show()
        isServiceStarted = true
        setServiceState(this, ServiceState.STARTED)

        // we need this lock so our service gets not affected by Doze Mode
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                    acquire()
                }
            }

        // we're starting a loop in a coroutine
        GlobalScope.launch(Dispatchers.IO) {
            while (isServiceStarted) {
                val delayTime = if (isInternetAvailable()) {
                    3 * 1000 // 3 seconds when internet is available
                } else {
                    20 * 1000 // 20 seconds when internet is unavailable
                }
                launch(Dispatchers.IO) {
//                    pingFakeServer()
                    if(isInternetAvailable()){
                        getPaymentStatus(imeiNo)

                    } else {
                        compareYesterdayDateWithCurrentTime()
                    }

                }
                delay(delayTime.toLong())
            }
            Log.d(LOG_TAG, "startService: >>>End of the loop for the service")
//            log("End of the loop for the service")
        }
    }

    private fun stopService() {
//        log("Stopping the foreground service")
        Log.d(LOG_TAG, "stopService: Stopping the foreground service")
        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show()
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
//            log("Service stopped without being started: ${e.message}")
            Log.d(LOG_TAG, "stopService: exc >>>Service stopped without being started: ${e.message}")
        }
        isServiceStarted = false
        setServiceState(this, ServiceState.STOPPED)
    }

    private fun pingFakeServer() {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmmZ")
        val gmtTime = df.format(Date())

        val deviceId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)

        val json =
            """
                {
                    "deviceId": "$deviceId",
                    "createdAt": "$gmtTime"
                }
            """
        try {
            Fuel.post("https://jsonplaceholder.typicode.com/posts")
                .jsonBody(json)
                .response { _, _, result ->
                    val (bytes, error) = result
                    if (bytes != null) {
                        Log.d(LOG_TAG, "pingFakeServer: >>>[response bytes] ${String(bytes)}")

//                        log("[response bytes] ${String(bytes)}")
                    } else {
                        Log.d(LOG_TAG, "pingFakeServer: >>> [response error] ${error?.message}")

//                        log("[response error] ${error?.message}")
                    }
                }
        } catch (e: Exception) {
//            log("Error making the request: ${e.message}")
            Log.d(LOG_TAG, "pingFakeServer: >>> Error making the request: ${e.message}")
        }
    }


    private fun getPaymentStatus(imeiNo: String) {
        apiService.getPaymentStatus(imeiNo).enqueue(object : Callback<GetStatusData> {
            override fun onResponse(call: Call<GetStatusData>, response: Response<GetStatusData>) {
                if (response.isSuccessful) {
                    val statusData = response.body()
                    Log.d("MyApiService", "Payment Status: ${statusData?.device?.payment_status}")
                    if(statusData?.device?.payment_status.equals("0")){
                        Log.d("MyApiService", "Coming here: ${statusData?.device?.payment_status}")
                        val calendar = Calendar.getInstance();
                        calendar.add(Calendar.DATE, -1);
                        val dueDate = calendar.time
                        applicationContext.getSharedPreferences("PaymentPrefs", MODE_PRIVATE).edit().putLong("due_date",dueDate.time).commit()
//                        launchLockScreen()
                        lockDevice()
                    } else {
                        Log.d("MyApiService", "Skipping: ${statusData?.device?.payment_status}")

                    }
                } else {
                    Log.e("MyApiService", "Failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GetStatusData>, t: Throwable) {
                Log.e("MyApiService", "Error: ${t.message}")
            }
        })}

    private fun createNotification(): Notification {
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                "Endless Service notifications channel",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Endless Service channel"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        }

        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
            this,
            notificationChannelId
        ) else Notification.Builder(this)

        return builder
            .setContentTitle("Endless Service")
            .setContentText("This is your favorite endless service working")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()
    }

    private fun launchLockScreen() {
        Log.d("MyApiService", "Coming in Launch Method:")

        try{
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
//            applicationContext.startActivity(intent)
        } catch (e : Exception){
            Log.d("MyApiService", "launchLockScreen: exception ${e.message}")
        }


    }

    private fun lockDevice() {
        Log.d("MyApiService", "Coming in Lock Method:")

        val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        if (dpm.isAdminActive(adminComponent!!)) {
            Log.d("PaymentCheckService", "Locking device due to unpaid EMI.")
            dpm.lockNow()
        } else {
            Log.e(
                "PaymentCheckService",
                "Admin permission not active. Attempting to activate admin."
            )
            requestAdminPermission()
        }
    }

    private fun requestAdminPermission() {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent)
        intent.putExtra(
            DevicePolicyManager.EXTRA_ADD_EXPLANATION,
            "Admin permission required to lock device."
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Required when starting activity from service
        startActivity(intent)
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            activeNetwork != null && activeNetwork.isConnected
        }
    }

    fun compareYesterdayDateWithCurrentTime() {
        // Fetch yesterday's date from SharedPreferences
        val yesterdayTime = applicationContext.getSharedPreferences("PaymentPrefs", MODE_PRIVATE).getLong("due_date",-1)


        val yesterdayDate = Date(yesterdayTime)
        if (yesterdayDate != null) {
            // Get the current date
            val currentDate = Date()

            // Compare the current date with yesterday's date
            if (currentDate.after(yesterdayDate)) {
                Log.d("DateComparison", "Yesterday's date is in the past.")
                lockDevice()
                // Perform some action if yesterday's date has passed
            } else {
                Log.d("DateComparison", "Yesterday's date is today (unlikely here).")
                // Perform some action if yesterday's date is today (unlikely)

            }
        } else {
            Log.d("DateComparison", "No stored yesterday date found.")
        }
    }


}