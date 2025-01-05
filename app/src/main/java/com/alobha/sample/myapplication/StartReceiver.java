package com.alobha.sample.myapplication;

import static com.alobha.sample.myapplication.utils.ServiceTrackerKt.getServiceState;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.alobha.sample.myapplication.utils.Actions;
import com.alobha.sample.myapplication.utils.ServiceState;

public class StartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) && getServiceState(context) == ServiceState.STARTED) {
            Intent serviceIntent = new Intent(context, EndlessService.class);
            serviceIntent.setAction(Actions.START.name());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("StartReceiver", "onReceive: Starting the service in >=26 Mode from a BroadcastReceiver");
//                log("Starting the service in >=26 Mode from a BroadcastReceiver", context);
                context.startForegroundService(serviceIntent);
                return;
            }
            Log.d("StartReceiver", "onReceive: Starting the service in < 26 Mode from a BroadcastReceiver");
//            log("Starting the service in < 26 Mode from a BroadcastReceiver", context);
            context.startService(serviceIntent);
        }
    }
}
