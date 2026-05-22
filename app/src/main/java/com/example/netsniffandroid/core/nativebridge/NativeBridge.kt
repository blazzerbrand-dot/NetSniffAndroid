package com.example.netsniffandroid.core.nativebridge

import com.example.netsniffandroid.core.utils.AppConstants
object NativeBridge {
    init {
        System.loadLibrary(AppConstants.NATIVE_LIBRARY)

    }
    external fun getNativeVersion() : String
}