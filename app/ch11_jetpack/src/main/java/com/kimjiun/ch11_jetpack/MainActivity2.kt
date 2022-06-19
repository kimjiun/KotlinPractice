package com.kimjiun.ch11_jetpack

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.kimjiun.ch11_jetpack.databinding.ActivityMain2Binding
import com.kimjiun.ch11_jetpack.databinding.ItemPagerBinding

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val datas = mutableListOf<String>()
        for(i in 1..10){
            datas.add("ITEM : $i")
        }
        // 리사이클러뷰 어댑터
        // binding.viewPager.adapter = MyViewPagerAdapter(datas)

        // 프래그먼트 어댑터 - 주로 사용됨
        binding.viewPager.adapter = MyFragmentPagerAdpater(this)
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d("JIUN", "onSupportNavigateUp ")
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    class MyViewPagerHolder(val binding: ItemPagerBinding): RecyclerView.ViewHolder(binding.root)

    class MyViewPagerAdapter(val datas: MutableList<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            MyViewPagerHolder(ItemPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val binding = (holder as MyViewPagerHolder).binding
            binding.itemData.text = "POSITION : ${datas[position]}"
            when(position % 3){
                0 -> binding.itemRoot.setBackgroundColor(Color.RED)
                1 -> binding.itemRoot.setBackgroundColor(Color.GREEN)
                2 -> binding.itemRoot.setBackgroundColor(Color.BLUE)
            }


            binding.itemRoot.setOnClickListener{ Log.d("KIMJIUN", "$position Clicked")}
        }

        override fun getItemCount(): Int {
            return datas.size
        }
    }
}