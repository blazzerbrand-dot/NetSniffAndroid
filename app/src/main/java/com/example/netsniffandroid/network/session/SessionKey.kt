package com.example.netsniffandroid.network.session

data class  SessionKey(
    val sourceIp:String,
    val sourcePort:Int,
    val destinationIp:String,
    val destinationPort:Int,
    val protocol: String
)