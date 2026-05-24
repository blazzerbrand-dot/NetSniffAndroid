package com.example.netsniffandroid.network.parser

object PacketClassifier {
    fun getProtocol(protocol: Int):String {
        return when(protocol) {
            Protocol.TCP -> "TCP"
            Protocol.UDP -> "UDP"
            Protocol.ICMP -> "ICMP"


            else -> "UNKNOWN"
         }
    }
}