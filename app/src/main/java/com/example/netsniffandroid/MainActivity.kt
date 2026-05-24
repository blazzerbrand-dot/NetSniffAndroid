package com.example.netsniffandroid

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.netsniffandroid.core.logging.NetSniffLogger
import com.example.netsniffandroid.databinding.ActivityMainBinding
import com.example.netsniffandroid.network.capture.NetSniffVpnService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.sampleText.text =
            "NETSNIFF VPN ENGINE"

        prepareVpn()
    }

    private fun prepareVpn() {

        val intent = VpnService.prepare(this)

        if (intent != null) {

            startActivityForResult(intent, 100)

        } else {

          val serviceIntent = Intent(this, NetSniffVpnService::class.java).apply {
              action = NetSniffVpnService.ACTION_START
          }
            startService(serviceIntent)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (
            requestCode == 100 &&
            resultCode == RESULT_OK
        ) {

            NetSniffLogger.i("VPN permission granted")

           //launch the service cleanly using the system intent
            val serviceIntent = Intent(this, NetSniffVpnService::class.java).apply{
                action = NetSniffVpnService.ACTION_START
            }
            startService(serviceIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
          // request the service to stop via intent command
        val serviceIntent = Intent(this, NetSniffVpnService::class.java).apply{
            action = NetSniffVpnService.ACTION_STOP
        }
        startService(serviceIntent)
    }
}