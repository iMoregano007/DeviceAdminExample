package com.alobha.sample.myapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class BatteryOptimizationHelper {

    public static void checkAndRequestBatteryOptimization(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if (powerManager != null && !powerManager.isIgnoringBatteryOptimizations(context.getPackageName())) {
            // Battery optimization is not ignored, prompt the user to disable it
            Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            context.startActivity(intent);
        } else {
            // Battery optimization is already ignored
            Toast.makeText(context, "Battery optimization permission already granted.", Toast.LENGTH_SHORT).show();
            Log.d("BatteryOptimization", "checkAndRequestBatteryOptimization: Battery optimization permission already granted.");
            System.out.println("Battery optimization permission already granted.");
        }
    }
}