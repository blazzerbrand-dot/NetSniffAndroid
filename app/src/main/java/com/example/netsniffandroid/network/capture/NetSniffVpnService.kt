package com.example.netsniffandroid.network.capture

//imoprts for the for the first real analysis pipeline
import com.example.netsniffandroid.network.parser.IPv4Parser
import com.example.netsniffandroid.network.parser.PacketClassifier

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

    override fun onCreate() {
        super.onCreate()
        NetSniffLogger.i("VPN Service created")

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
                         val iPv4Header =
                             IPv4Parser.parse(packet)

                         if(iPv4Header != null) {
                             val protocolName =
                                 PacketClassifier.getProtocol(
                                     iPv4Header.protocol
                                 )
                             NetSniffLogger.d(
                                 """
                                    Packet Captured
                                  Source IP: ${iPv4Header.sourceIp}
                                  Destination IP: ${iPv4Header.destinationIp}
                                  Protocol: $protocolName
                                  Length: ${iPv4Header.totalLength}
                                   """.trimIndent()
                             )
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