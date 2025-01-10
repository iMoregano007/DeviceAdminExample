package com.alobha.sample.myapplication.utils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

public class FactoryResetManager {
    private DevicePolicyManager devicePolicyManager;
    private ComponentName adminComponent;

    public FactoryResetManager(Context context, ComponentName componentName) {
        devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = componentName;
    }

    public void initiateFactoryReset() {
        if (devicePolicyManager != null) {
            try {
                // Wipe all data
                devicePolicyManager.wipeData(DevicePolicyManager.WIPE_RESET_PROTECTION_DATA);
//                System.out.println("Factory reset initiated successfully.");
                Log.d("FactoryReset", "initiateFactoryReset: Factory reset initiated successfully.");
            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("Failed to initiate factory reset: " + e.getMessage());
                Log.d("FactoryReset", "initiateFactoryReset: exc>>"+e.getMessage());
            }
        }
    }
}
