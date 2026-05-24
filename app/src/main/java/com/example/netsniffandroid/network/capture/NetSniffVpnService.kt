package com.example.netsniffandroid.network.capture

//imoprts for the for the first real analysis pipeline
import com.example.netsniffandroid.network.parser.IPv4Parser
import com.example.netsniffandroid.network.parser.PacketClassifier

//imports for the second layer transport layer
import com.example.netsniffandroid.network.parser.Protocol
import com.example.netsniffandroid.network.parser.TCPParser
import com.example.netsniffandroid.network.parser.UDPparser
import com.example.netsniffandroid.network.parser.PortClassifier

//import for the sessions tracking
import com.example.netsniffandroid.network.session.SessionKey
import com.example.netsniffandroid.network.session.SessionManager
//imports for the  VPN notifications
import android.content.Intent
import com.example.netsniffandroid.core.utils.NotificationConstants
import com.example.netsniffandroid.core.notification.NotificationHelper


import android.net.VpnService
import android.os.ParcelFileDescriptor
import  com.example.netsniffandroid.core.logging.NetSniffLogger
import com.example.netsniffandroid.core.utils.AppConstants
import com.example.netsniffandroid.core.threading.CaptureThread
import java.io.FileInputStream

class NetSniffVpnService : VpnService() {
    private var vpnInterface: ParcelFileDescriptor? = null
    private var captureThread: CaptureThread? = null
    private var captureState = CaptureState.IDLE

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        NetSniffLogger.i("VPN Service created")

        //creating notification channel
        NotificationHelper.createNotificationChannel(
            this
        )

    }

    override  fun onDestroy() {
        super.onDestroy()

        stopCapture()
        NetSniffLogger.i("VPN Service destroyed")


    }

    fun startCapture() {
        if(captureState == CaptureState.RUNNING) {
            return
        }

        captureState = CaptureState.STARTING
        NetSniffLogger.i("Starting VPN Capture")

        val builder = Builder()
        builder
            .setSession(AppConstants.TAG)
            .addAddress("10.0.0.2",24)
            .addRoute("0.0.0.0",0)
            .setMtu(AppConstants.VPN_MTU)

        vpnInterface = builder.establish()

        if (vpnInterface == null) {
            captureState = CaptureState.ERROR

            NetSniffLogger.e("Failed to establish VPN interface")

            return
        }
        val inputStream =
            FileInputStream(vpnInterface!!.fileDescriptor)
         captureThread = CaptureThread(
             Runnable {
                 try {
                     val packet = ByteArray(AppConstants.MAX_PACKET_SIZE)
                     val length = inputStream.read(packet)
                     if(length > 0 ) {
                         //this is for only with packet size only
                        //   NetSniffLogger.d("Captured packet size : $length bytes")
                     //now integrating the IPv4 parsing into capture pipeline
                         //parsing the ipheader
                         val iPv4Header =
                             IPv4Parser.parse(packet)

                         if(iPv4Header != null) {
                             val protocolName =
                                 PacketClassifier.getProtocol(
                                     iPv4Header.protocol
                                 )
                             when (iPv4Header.protocol) {

                                 //now we are updating the TCP because we have added the sessions tracking
                                 Protocol.TCP -> {

                                     //parsing the tcpheader
                                     val tcpHeader =
                                         TCPParser.parse(
                                             packet,
                                             iPv4Header.headerLength
                                         )
                                     //checking if tcpHeader null??
                                     if(tcpHeader != null) {
                                         //implementing sessionKey
                                         val sessionKey=
                                         SessionKey(
                                             sourceIp = iPv4Header.sourceIp,
                                             sourcePort = tcpHeader.sourcePort,
                                             destinationIp = iPv4Header.destinationIp,
                                             destinationPort = tcpHeader.destinationPort,
                                             protocol ="TCP"
                                         )

                                         //implementing session manager

                                         SessionManager.processSession(
                                             sessionKey = sessionKey,
                                             packetLength = iPv4Header.totalLength,
                                             isOutgoing = true
                                         )
                                         val service =
                                             PortClassifier.classify(
                                                 tcpHeader.destinationPort
                                             )
                                         //displaying on the log cat

                                         NetSniffLogger.d(
                                             """
                                                TCP Packet
                                                ${iPv4Header.sourceIp}:${tcpHeader.sourcePort}
                                                 →
                                               ${iPv4Header.destinationIp}:${tcpHeader.destinationPort}
                        
                                                Service: $service
                                                Flags: ${tcpHeader.flags}
                                                Seq: ${tcpHeader.sequenceNumber}
                                                Ack: ${tcpHeader.acknowledgmentNumber}
                                                """.trimIndent()
                                         )
                                     }

                                 }

                                 Protocol.UDP ->  {
                                     //parsing the udpheader

                                     val udpHeader =
                                         UDPparser.parse(
                                             packet,
                                             iPv4Header.headerLength

                                         )

                                     if(udpHeader != null) {

                                         //implementing the session key
                                         val sessionKey=
                                             SessionKey(
                                                 sourceIp = iPv4Header.sourceIp,
                                                 sourcePort = udpHeader.sourcePort,
                                                 destinationIp = iPv4Header.destinationIp,
                                                 destinationPort = udpHeader.destinationPort,
                                                 protocol = "UDP"
                                             )
                                         //implementing the session manager for udp protocol
                                         SessionManager.processSession(
                                             sessionKey = sessionKey,
                                             packetLength = udpHeader.length,
                                             isOutgoing = true

                                         )
                                         val service =
                                             PortClassifier.classify(
                                                     udpHeader.destinationPort


                                             )
                                         NetSniffLogger.d(
                                             """
                                               UDP Packet
                                               ${iPv4Header.sourceIp}:${udpHeader.sourcePort}
                                               →
                                               ${iPv4Header.destinationIp}:${udpHeader.destinationPort}
                        
                                               Service: $service
                                               Length: ${udpHeader.length}
                                             """.trimIndent()
                                         )
                                     }
                                 }
                             }

                         }
                     }
                 }
                 catch (e:Exception) {
                     NetSniffLogger.e("Packet capture error : ${e.message}")
                 }
             }
         )

        captureThread?.start()
        captureState = CaptureState.RUNNING
        NetSniffLogger.i("VPN capture Started")
    }
    fun stopCapture() {
        captureState = CaptureState.STOPPING
        captureThread?.shutdown()
        captureThread = null
        vpnInterface?.close()
        vpnInterface = null
        captureState = CaptureState.STOPPED
        NetSniffLogger.i("VPN Capture Stopped")
    }

}