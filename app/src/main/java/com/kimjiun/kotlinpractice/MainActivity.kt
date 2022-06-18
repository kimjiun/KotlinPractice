package com.kimjiun.kotlinpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import com.kimjiun.kotlinpractice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val TAG:String = "JIUNKIM"
    lateinit var bind:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.imageView.setOnClickListener {
            Log.d(TAG, "${it.x}, ${it.y}")
        }

        bind.imageView.setOnLongClickListener {
            Log.d(TAG, "${it.x}, ${it.y}")
            true
        }
    }

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed")
        super.onBackPressed()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d(TAG, "onKeyDown")
        when(keyCode){
            KeyEvent.KEYCODE_BACK -> Log.d(TAG, "KEYCODE_BACK")
            KeyEvent.KEYCODE_VOLUME_UP -> Log.d(TAG, "KEYCODE_VOLUME_UP")
            KeyEvent.KEYCODE_VOLUME_DOWN -> Log.d(TAG, "KEYCODE_VOLUME_DOWN")
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d(TAG, "onKeyUp")
        return super.onKeyUp(keyCode, event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "ACTION_DOWN / x : ${event.x}, rawx : ${event.rawX} ")
            }

            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "ACTION_UP")
            }
        }

        return super.onTouchEvent(event)
    }
}