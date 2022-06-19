package com.kimjiun.ch10_dialog

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput

class MyReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val d = Log.d("MyReceiver", "onReceive")
        val manager = context!!.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder

        val replyTxt = RemoteInput.getResultsFromIntent(intent)?.getCharSequence("key_text_reply")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "KIMJIUN"
            val channelName = "JIUN Channel"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)

            //채널을 이용하여 builder 생성
            builder = NotificationCompat.Builder(context, channelId)
        } else {
            builder = NotificationCompat.Builder(context)
        }

        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("Content Title")
        builder.setContentText("Content Massage $replyTxt ")

        manager.notify(9, builder.build())

    }
}