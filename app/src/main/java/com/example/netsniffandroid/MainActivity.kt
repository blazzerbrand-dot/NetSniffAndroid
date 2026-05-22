package com.example.netsniffandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.netsniffandroid.core.logging.NetSniffLogger
import com.example.netsniffandroid.core.nativebridge.NativeBridge
import com.example.netsniffandroid.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        //binding.sampleText.text = stringFromJNI()
        val nativeVersion = NativeBridge.getNativeVersion()
        binding.sampleText.text = "NETSNIFF ENGINE READY\n$nativVersion"

        NetSniffLogger.i("MainActivity initialized")


    }

    /**
     * A native method that is implemented by the 'netsniffandroid' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'netsniffandroid' library on application startup.
        init {
            System.loadLibrary("netsniffandroid")
        }
    }
}