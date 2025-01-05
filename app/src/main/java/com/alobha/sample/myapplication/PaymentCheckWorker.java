package com.alobha.sample.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;

import com.alobha.sample.myapplication.model.GetStatusData;
import com.alobha.sample.myapplication.model.PaymentStatusData;
import com.google.common.util.concurrent.ListenableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.google.common.util.concurrent.ListenableFuture;

//Attempt 1

//public class PaymentCheckWorker extends Worker {
//
//    boolean paymentConfirmed;
//
//    public PaymentCheckWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//        super(context, workerParams);
//    }
//
//    @NonNull
//    @Override
//    public Result doWork() {
//        // Make API call to check payment status
//        String imeiNo = getApplicationContext()
//                .getSharedPreferences("PaymentPrefs", Context.MODE_PRIVATE)
//                .getString("imei_no", "");
//
//        String deviceID = DeviceUtil.getAndroidId(getApplicationContext());
//        paymentConfirmed = true;
//
//        if (deviceID != null) {
//            try {
//                ApiService apiService = RetrofitInstance.getApiService();
//                apiService.getPaymentStatus(deviceID).enqueue(new Callback<GetStatusData>() {
//                    @Override
//                    public void onResponse(@NonNull Call<GetStatusData> call, @NonNull Response<GetStatusData> response) {
//                        if(response.isSuccessful() && response.body() != null){
//                            GetStatusData getStatusData = response.body();
//                            String paymentStatus = getStatusData.getDevice().getPayment_status();
//                            paymentConfirmed = paymentStatus.equals("1");
//                            Log.d("PaymentCheckService","Worker Msg  >> "+getStatusData.getMessage()+" Payment Status >>>"+getStatusData.getDevice().getPayment_status());
////                            Toast.makeText(, getStatusData.getMessage(), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.d("PaymentCheckService","API ERROR Worker : ERROR_CODE >> "+response.code());
////                            Toast.makeText(PaymentCheckService.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<GetStatusData> call, @NonNull Throwable t) {
//                        Log.d("PaymentCheckService","API ERROR : MSG  Worker>> "+t.getMessage());
////                        Toast.makeText(PaymentCheckService.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
//                    }
//                });
////                Response<PaymentStatusData> response = RetrofitInstance.getInstance().getPaymentStatus(imeiNo).execute();
////                if (response.isSuccessful() && response.body() != null) {
////                    PaymentStatusData statusData = response.body();
////                    if (statusData.getStatus() == 0) { // Unpaid
////                        launchLockScreen();
////                    }
////                }
//
//                if(!paymentConfirmed){
//                    launchLockScreen();
//                }
//
//            } catch (Exception e) {
////                e.printStackTrace();
//                Log.d("WorkerException", "doWork: exception>>>"+e.toString());
//                return Result.retry(); // Retry the task in case of failure
//            }
//        }
//        return Result.success();
//    }
//
//    private void launchLockScreen() {
//        Intent intent = new Intent(getApplicationContext(), LockScreenActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getApplicationContext().startActivity(intent);
//    }
//}

public class PaymentCheckWorker extends ListenableWorker {

    public PaymentCheckWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

//    @NonNull
//    @Override
//    public ListenableFuture<Result> startWork() {
//        SettableFuture<Result> future = SettableFuture.create();
//
//        String deviceID = DeviceUtil.getAndroidId(getApplicationContext());
//
//        if (deviceID != null) {
//            ApiService apiService = RetrofitInstance.getApiService();
//            apiService.getPaymentStatus(deviceID).enqueue(new Callback<GetStatusData>() {
//                @Override
//                public void onResponse(@NonNull Call<GetStatusData> call, @NonNull Response<GetStatusData> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        String paymentStatus = response.body().getDevice().getPayment_status();
//                        if ("1".equals(paymentStatus)) {
//                            future.set(Result.success());
//                        } else {
//                            launchLockScreen();
//                            future.set(Result.success());
//                        }
//                    } else {
//                        Log.d("PaymentCheckWorker", "API error: " + response.code());
//                        future.set(Result.retry());
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<GetStatusData> call, @NonNull Throwable t) {
//                    Log.d("PaymentCheckWorker", "API failure: " + t.getMessage());
//                    future.set(Result.retry());
//                }
//            });
//        } else {
//            future.set(Result.failure());
//        }
//
//        return future;
//    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        Log.d("PaymentCheckWorker", "Worker started executing.");

        return CallbackToFutureAdapter.getFuture(completer -> {
            // Initialize the Retrofit call
            ApiService apiService = RetrofitInstance.getApiService();
            String imeiNo = getApplicationContext().getSharedPreferences("PaymentPrefs",Context.MODE_PRIVATE).getString("imei_no","");
//            String deviceID = DeviceUtil.getAndroidId(getApplicationContext());

            apiService.getPaymentStatus(imeiNo).enqueue(new Callback<GetStatusData>() {
                @Override
                public void onResponse(@NonNull Call<GetStatusData> call, @NonNull Response<GetStatusData> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        GetStatusData getStatusData = response.body();
                        String paymentStatus = getStatusData.getDevice().getPayment_status();
                        boolean paymentConfirmed = true;
                        if(paymentStatus != null ){
                            paymentConfirmed= paymentStatus.equals("1");
                        }

                        Log.d("PaymentCheckWorker", "Payment Status: " + paymentStatus);

                        if (!paymentConfirmed) {
                            launchLockScreen();
                        }
                        completer.set(Result.success());
                    } else {
                        Log.d("PaymentCheckWorker", "API Response Error: " + response.code());
                        completer.set(Result.retry());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetStatusData> call, @NonNull Throwable t) {
                    Log.d("PaymentCheckWorker", "API Failure: " + t.getMessage());
                    completer.set(Result.retry());
                }
            });

            // Set debug name (optional, for better debugging)
            return "PaymentCheckWorker";
        });
    }

    private void launchLockScreen() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }
}


