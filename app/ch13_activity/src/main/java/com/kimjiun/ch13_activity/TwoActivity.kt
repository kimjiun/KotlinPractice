package com.example.test13

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kimjiun.ch13_activity.R

class TwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.77449,127.4194"))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }
}