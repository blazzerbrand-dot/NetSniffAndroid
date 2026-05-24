package com.example.netsniffandroid.network.capture

enum class CaptureState {
    IDLE,
    STARTING,
    RUNNING,
    STOPPING,
    STOPPED,
    ERROR
}