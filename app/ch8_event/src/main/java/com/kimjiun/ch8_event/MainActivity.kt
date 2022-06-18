package com.kimjiun.ch8_event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.kimjiun.ch8_event.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var bind:ActivityMainBinding

    var initTime = 0L
    var pauseTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.startButton.setOnClickListener {
            bind.startButton.isEnabled = false
            bind.stopButton.isEnabled = true
            bind.resetButton.isEnabled = true

            bind.chronometer.base = SystemClock.elapsedRealtime() + pauseTime
            bind.chronometer.start()
        }

        bind.stopButton.setOnClickListener {
            bind.startButton.isEnabled = true
            bind.stopButton.isEnabled = false
            bind.resetButton.isEnabled = true

            pauseTime = bind.chronometer.base - SystemClock.elapsedRealtime()
            bind.chronometer.stop()
        }

        bind.resetButton.setOnClickListener {
            bind.startButton.isEnabled = true
            bind.stopButton.isEnabled = false
            bind.resetButton.isEnabled = false

            pauseTime = 0L
            bind.chronometer.base = SystemClock.elapsedRealtime()
            bind.chronometer.stop()
        }

        val st:String = getString(android.R.string.paste_as_plain_text)
    }
}