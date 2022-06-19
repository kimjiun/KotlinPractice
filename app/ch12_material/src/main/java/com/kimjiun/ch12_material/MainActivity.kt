package com.kimjiun.ch12_material

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kimjiun.ch12_material.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.floating.setOnClickListener {
            when(binding.floating.isExtended){
                true -> binding.floating.shrink()
                false -> binding.floating.extend()
            }
        }
    }

}