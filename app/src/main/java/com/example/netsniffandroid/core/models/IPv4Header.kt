package com.example.netsniffandroid.core.models

data class IPv4Header(
    val version: Int,
    val headerLength:Int,
    val totalLength:Int,
    val protocol:Int,
    val sourceIp: String,
    val destinationIp: String
)