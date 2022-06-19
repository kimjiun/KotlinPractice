package com.kimjiun.ch12_material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kimjiun.ch12_material.databinding.MyFragmentBinding

class ThirdFragment: Fragment() {
    lateinit var binding: MyFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyFragmentBinding.inflate(inflater, container, false)
        binding.fragText.text = "THIRD"
        return binding.root
    }

}