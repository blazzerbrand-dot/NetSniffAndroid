package com.example.netsniffandroid.core.models

data class TCPHeader(
    val sourcePort:Int,
    val destinationPort:Int,
    val sequenceNumber:Long,
    val acknowledgmentNumber:Long,
    val headerLength:Int,
    val flags:List<String>
)