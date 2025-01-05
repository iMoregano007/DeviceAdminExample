package com.alobha.sample.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

public class DeviceUtil {

    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        try {
            return Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID
            );
        } catch (Exception e) {
//            e.printStackTrace();
            Log.d("deviceIDFetchExc", "getAndroidId: Exception >>>"+e.toString());
            return null; // Return null if any exception occurs
        }
    }



}
