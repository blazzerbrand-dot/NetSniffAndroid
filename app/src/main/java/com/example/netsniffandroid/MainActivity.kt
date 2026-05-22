package com.example.netsniffandroid

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.netsniffandroid.core.nativebridge.NativeBridge

class MainActivity : AppCompatActivity() {

    // Group view declarations at the top for neatness
    private lateinit var sampleTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Standard layout inflation

        // Initialize views immediately


        // Proceed with your logic and JNI C++ code
        val nativeVersion = NativeBridge.getNativeVersion()
         val sampleTextView = findViewById<TextView>(R.id.sample_text)
        sampleTextView.text = "NETSNIFFF ENGINE READY\nVersion: $nativeVersion"


    }
}
