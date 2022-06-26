package com.kimjiun.test_outter

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyAIDLService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        Log.d("JIUNKIM", "MyAIDLService onBind")
        return object : IMyAidlInterface.Stub(){
            override fun funA(data: String?) {
                Log.d("JIUNKIM", "funA ${data}")
            }

            override fun funB(): Int {
                return 10
            }
        }
    }

}