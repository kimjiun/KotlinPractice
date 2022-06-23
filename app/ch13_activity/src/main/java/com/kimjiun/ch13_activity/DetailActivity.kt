package com.kimjiun.ch13_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kimjiun.ch13_activity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fab.setOnClickListener { v ->
            intent.putExtra("result", "GOOD")
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}