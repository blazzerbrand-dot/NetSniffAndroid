package com.example.netsniffandroid.network.parser

import com.example.netsniffandroid.core.models.IPv4Header
object IPv4Parser {
    fun parse(packet: ByteArray): IPv4Header? {
        if(packet.isEmpty()) {
            return null
        }
        val version = (packet[0].toInt() shr 4) and 0x0F

        if (version !=4) {
            return null
        }
        val headerLength =
            (packet[0].toInt() and 0x0F)*4
        val totalLength =
            ((packet[2].toInt() and 0xFF) shl 8 ) or
                    (packet[3].toInt() and 0xFF)

        val protocol =
            packet[9].toInt() and 0xFF

        val sourceIp =
            parseIpAddress(packet,12)
        val destinationIp =
            parseIpAddress(packet,16)

        return IPv4Header(
            version = version,
            headerLength = headerLength,
            totalLength =  totalLength,
            protocol = protocol,
            sourceIp= sourceIp,
            destinationIp = destinationIp
        )
    }
    private fun parseIpAddress(
        packet: ByteArray,
        offest: Int
    ): String {
        return listOf(
            packet[offest].toInt() and 0xFF,
            packet[offest + 1].toInt() and 0xFF,
            packet[offest + 2].toInt() and 0xFF,
            packet[offest + 3].toInt() and 0xFF

        ).joinToString(".")
    }
}