package com.alobha.sample.myapplication.model

data class GetStatusDevice(
    val email: String,
    val id: String,
    val imei_no: String,
    val name: String,
    val payment_status: String,
    val phone_no: String,
    val status: String
)