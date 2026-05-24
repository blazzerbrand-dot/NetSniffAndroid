package com.example.netsniffandroid.network.session

data class SessionInfo(
    val sessionKey: SessionKey,
    var packetCount : Long = 0,
    var uploadBytes: Long = 0,
    var downloadBytes:Long=0,
    var lastSeenTimestamp:Long = System.currentTimeMillis()
)