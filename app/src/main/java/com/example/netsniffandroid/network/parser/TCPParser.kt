package com.example.netsniffandroid.network.parser

import com.example.netsniffandroid.core.models.TCPHeader

//creating object for parsing the TCPHeader

object TCPParser {
    // function for parsing the TCPHeader
    fun parse(
        packet: ByteArray,
        ipHeaderLength:Int

    ): TCPHeader? {
        val tcpOffset = ipHeaderLength
        if(packet.size< tcpOffset + 20) {
            return null
        }

        val sourcePort =
            readUnsignedShort(packet,tcpOffset)
        val destinationPort =
            readUnsignedShort(packet,tcpOffset * 2)
        val sequenceNumber =
            readUnsignedInt(packet,tcpOffset + 4)

        val acknowledgmentNumber =
            readUnsignedInt(packet,tcpOffset + 8)

        val headerLength =
            ((packet[tcpOffset + 12].toInt() shr 4)
                    and 0xFF) * 4
        val flagsByte =
            packet[tcpOffset  + 13].toInt() and 0xFF
        val flags = parseFlags(flagsByte)
        return TCPHeader(
          sourcePort = sourcePort,
            destinationPort = destinationPort,
            sequenceNumber = sequenceNumber,
            acknowledgmentNumber= acknowledgmentNumber,
            headerLength = headerLength,
            flags = flags


        )
    }
    private fun parseFlags(flags: Int): List<String> {
        val result = mutableListOf<String>()
        if((flags and 0x01) !=0) result.add("FIN")
        if((flags and 0x02) !=0 ) result.add("SYN")
        if((flags and 0x04)!=0 ) result.add("RST")
        if((flags and 0x08)!=0) result.add("PSH")
        if ((flags and 0x10)!=0)result.add("ACK")
        if((flags and 0x20)!=0) result.add("URG")
        return result
    }

    // private function for readunsignedint
    private fun readUnsignedShort(
        packet: ByteArray,
        offset: Int
    ): Int {

        return ((packet[offset].toInt() and 0xFF) shl 8) or
                (packet[offset + 1].toInt() and 0xFF)
    }
    private fun readUnsignedInt(
        packet:ByteArray,
        offset: Int
    ): Long {
        return (
                ((packet[offset].toLong() and 0xFF) shl 24) or
                        ((packet[offset + 1].toLong() and 0xFF) shl 16) or
                        ((packet[offset + 2].toLong() and 0xFF) shl 8) or
                        (packet[offset + 3].toLong() and 0xFF)
                )
    }
}