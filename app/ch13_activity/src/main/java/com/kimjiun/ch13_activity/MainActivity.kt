package com.kimjiun.ch13_activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.test13.TwoActivity
import com.kimjiun.ch13_activity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ActivityResultLauncher 사용
        val requestLauncer: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            val resultData = it.data?.getStringExtra("result")
            binding.button.text = "RST : $resultData"
        }


        binding.button.setOnClickListener { v ->
            val intent: Intent = Intent(this, DetailActivity::class.java)
            requestLauncer.launch(intent)
        }

        // 같은 이름의 인텐트 있을경우
        binding.button2.setOnClickListener { v ->
            val intent: Intent = Intent(this, TwoActivity::class.java)
            startActivity(intent)
        }

        // 키보드
        val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        binding.button3.setOnClickListener {
            binding.editView.requestFocus()
            manager.showSoftInput(binding.editView, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.button4.setOnClickListener {
            manager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }

        binding.button5.setOnClickListener {
            manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        // 전체화면
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.setDecorFitsSystemWindows(false)
            val controller = window.insetsController
            if(controller != null){
                controller.hide(WindowInsets.Type.statusBars() or
                WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
        else{
            window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("data1", "hello")
        outState.putInt("data2", 10)
    }

    @SuppressLint("SetTextI18n")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val data1 = savedInstanceState.getString("data1")
        val data2 = savedInstanceState.getInt("data2", 15)

        Log.d("JIUN", "D1 : $data1 / D2 : $data2")

        binding.button2.text = "D1 : $data1 / D2 : $data2"
    }
}