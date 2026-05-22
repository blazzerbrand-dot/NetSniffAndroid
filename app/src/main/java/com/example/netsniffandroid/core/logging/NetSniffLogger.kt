package com.example.netsniffandroid.core.logging

import android.util.Log
import com.example.netsniffandroid.core.utils.AppConstants

object NetSniffLogger {
    fun d(message : String)  {
        Log.d(AppConstants.TAG,message)

    }
    fun e(message: String) {
        Log.e(AppConstants.TAG,message)
    }
    fun i(message: String) {
        Log.i(AppConstants.TAG,message)
    }
}