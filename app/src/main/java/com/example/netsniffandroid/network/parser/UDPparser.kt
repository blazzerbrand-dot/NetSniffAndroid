package com.example.netsniffandroid.network.parser

import com.example.netsniffandroid.core.models.UDPHeader

//implementing object for UDPParser

object UDPparser {
    //function for parsing
    fun parse(
        packet: ByteArray,
        ipHeaderLength: Int
    ): UDPHeader? {
        val udpOffset = ipHeaderLength

        if(packet.size < udpOffset + 8) {
            return null
        }

        val sourcePort =
            readUnsignedShort(packet,udpOffset)
        val destinationPort =
            readUnsignedShort(packet,udpOffset + 2)
        val length =
            readUnsignedShort(packet,udpOffset + 4)

        return UDPHeader(
            sourcePort=sourcePort,
            destinationPort= destinationPort,
            length= length

        )
    }

    private fun readUnsignedShort(
        packet: ByteArray,
        offset: Int
    ): Int{
        return ((packet[offset].toInt() and 0xFF ) shl 8 ) or
                (packet[offset + 1 ].toInt() and 0xFF)
    }
}