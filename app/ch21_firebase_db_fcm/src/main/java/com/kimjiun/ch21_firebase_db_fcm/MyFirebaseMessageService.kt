package com.kimjiun.ch21_firebase_db_fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessageService : FirebaseMessagingService() {
    val TAG = "JIUNKIM"
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "FCM TOKEN : $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "FCM MSG : ${message.notification}")
        Log.d(TAG, "FCM MSG : ${message.data}")
    }
}