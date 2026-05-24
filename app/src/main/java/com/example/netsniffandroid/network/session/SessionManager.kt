package com.example.netsniffandroid.network.session
//import the logging
import android.health.connect.datatypes.units.Length
import com.example.netsniffandroid.core.logging.NetSniffLogger
//import the concurrent hashmap
import java.util.concurrent.ConcurrentHashMap
object SessionManager {

    //creating private hashmap for finding active sessions
    //key:sessionKey,
    private val activeSessions =
        ConcurrentHashMap<SessionKey, SessionInfo>()

    //function process Session
    fun processSession(
        sessionKey: SessionKey,
        packetLength: Int,
        isOutgoing: Boolean
    ) {
        val session = activeSessions.getOrPut(sessionKey){
            NetSniffLogger.i(
                "New session created $sessionKey"
            )
            SessionInfo(sessionKey)
        }
        //incrementing the packetCount in a session

        session.packetCount++

        //if packets are outgoing then need to update the uploadBytes
        if(isOutgoing) {
            session.uploadBytes += packetLength


        }
        //else it must be the downloading ,then update the downloadBytes
        else{
            session.downloadBytes += packetLength

        }
        //assigning the last seen
        session.lastSeenTimestamp =
            System.currentTimeMillis()

        //log stats
           logSessionStats(session)


    }

    // function for logSessionStats
    private fun logSessionStats(
        session: SessionInfo
    ) {
        NetSniffLogger.d(
            """
            Active Session
            
            ${session.sessionKey.sourceIp}:${session.sessionKey.sourcePort}
            →
            ${session.sessionKey.destinationIp}:${session.sessionKey.destinationPort}
            
            Protocol: ${session.sessionKey.protocol}
            
            Packets: ${session.packetCount}
            
            Upload: ${session.uploadBytes} bytes
            
            Download: ${session.downloadBytes} bytes
            """.trimIndent()
        )
    }

    // function for getting active session count
    fun getActiveSessionCount():Int {
        return activeSessions.size
    }

}
