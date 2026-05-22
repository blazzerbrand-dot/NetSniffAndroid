package com.example.netsniffandroid.core.models



data class PacketData (

    val timestamp : Long,
    val length : Int,
    val protocol : Int,
    val sourceIp: String,
    val destinationIp: String,
    val payload : ByteArray

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PacketData

        if (timestamp != other.timestamp) return false
        if (length != other.length) return false
        if (protocol != other.protocol) return false
        if (sourceIp != other.sourceIp) return false
        if (destinationIp != other.destinationIp) return false
        if (!payload.contentEquals(other.payload)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = timestamp.hashCode()
        result = 31 * result + length
        result = 31 * result + protocol
        result = 31 * result + sourceIp.hashCode()
        result = 31 * result + destinationIp.hashCode()
        result = 31 * result + payload.contentHashCode()
        return result
    }
}