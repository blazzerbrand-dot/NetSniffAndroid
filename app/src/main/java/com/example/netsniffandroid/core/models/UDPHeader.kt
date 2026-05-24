package com.example.netsniffandroid.core.models

data class UDPHeader(
    val sourcePort:Int,
    val destinationPort:Int,
    val length:Int
)