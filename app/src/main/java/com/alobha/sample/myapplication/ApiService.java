package com.alobha.sample.myapplication;

import com.alobha.sample.myapplication.model.DeviceCreatedResponse;
import com.alobha.sample.myapplication.model.GetStatusData;
import com.alobha.sample.myapplication.model.PaymentStatusData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("createDevice.php")
    Call<DeviceCreatedResponse> createDevice(
            @Field("name") String name,
            @Field("email") String email,
            @Field("imei_no") String imeiNo,
            @Field("phone_no") String phoneNo,
            @Field("status") String status,
            @Field("dealer_id") String dealerID);


    @FormUrlEncoded
    @POST("activePyamentStatus.php")
    Call<PaymentStatusData> setPaymentStatusPaid(
            @Field("imei_no") String imeiNo
            );

    @FormUrlEncoded
    @POST("inactivePaymentStatus.php")
    Call<PaymentStatusData> setPaymentStatusUnPaid(
            @Field("imei_no") String imeiNo
    );

    @FormUrlEncoded
    @POST("getUserStatus.php")
    Call<GetStatusData> getPaymentStatus(
            @Field("imei_no") String imeiNo
    );


}
