package com.kimjiun.ch15_service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.Toast

class MyService : Service() {
    lateinit var messenger:Messenger
    inner class IncomingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext)
        :Handler(Looper.getMainLooper())
    {
        override fun handleMessage(msg: Message) {
            when(msg.what){
                10 -> Toast.makeText(applicationContext, "${msg.obj}", Toast.LENGTH_SHORT).show()
                20 -> Toast.makeText(applicationContext, "${msg.obj}", Toast.LENGTH_SHORT).show()
                else -> super.handleMessage(msg)
            }
        }
    }

    class MyBinder : Binder(){
        fun funA(arg: Int){

        }
        fun funB(arg: Int): Int{
            return arg * arg
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        messenger = Messenger(IncomingHandler(this))
        return messenger.binder
    }

}