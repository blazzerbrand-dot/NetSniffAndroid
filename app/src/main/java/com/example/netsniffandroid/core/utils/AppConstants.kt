package com.example.netsniffandroid.core.utils

object AppConstants {
    const val TAG = "NETSNIFF"
    const val MAX_PACKET_SIZE = 65536
    const val PACKET_BUFFER_POOL_SIZE = 256
    const val NATIVE_LIBRARY = "netsniffandroid"
    const val VPN_MTU = 1500

}
object NotificationConstants {
    const val CHANNEL_ID=
        "netsniff_vpn_channel"
    const val CHANNEL_NAME =
        "NETSNIFF VPN"
    const val NOTIFICATION_ID = 1001

}