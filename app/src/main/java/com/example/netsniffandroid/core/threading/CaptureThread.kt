package com.example.netsniffandroid.core.threading

import com.example.netsniffandroid.core.logging.NetSniffLogger

class CaptureThread(
    private val runnable: Runnable
) : Thread() {

    @Volatile
    private var running = true

    init {
        name = "NetSniff-Capture-Thread"
        priority = MAX_PRIORITY

    }

    override fun run() {
        NetSniffLogger.i("Capture thread started")

        while (running && !isInterrupted) {

            runnable.run()
        }

        NetSniffLogger.i("Capture thread stopped")
    }

    fun shutdown() {
        running = false
        interrupt()
    }
}