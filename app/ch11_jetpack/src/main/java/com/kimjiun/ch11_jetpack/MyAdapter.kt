package com.kimjiun.ch11_jetpack

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kimjiun.ch11_jetpack.databinding.ItemMainBinding

class MyAdapter(val datas: MutableList<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        binding.itemData.text = "POSITION : ${datas[position]}"
        binding.itemRoot.setOnClickListener{ Log.d("KIMJIUN", "$position Clicked")}
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}