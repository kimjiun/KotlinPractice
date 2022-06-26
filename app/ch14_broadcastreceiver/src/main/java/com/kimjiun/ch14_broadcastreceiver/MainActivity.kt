package com.kimjiun.ch14_broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 동적 등록
        val receiver = object : BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                when(p1?.action){
                    Intent.ACTION_SCREEN_ON -> Log.d("JIUN", "screen on")
                    Intent.ACTION_SCREEN_OFF -> Log.d("JIUN", "screen off")
                }
            }
        }

        val filter = IntentFilter(Intent.ACTION_SCREEN_ON).apply {
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(receiver, filter)

        //unregisterReceiver(receiver)

        val intent = Intent(this, MyReceiver::class.java)
        sendBroadcast(intent)
    }
}