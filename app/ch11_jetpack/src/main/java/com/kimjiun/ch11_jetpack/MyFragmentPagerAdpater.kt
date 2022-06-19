package com.kimjiun.ch11_jetpack

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyFragmentPagerAdpater(activity: MainActivity2): FragmentStateAdapter(activity) {
    val fragments : List<Fragment>
    init {
        fragments = listOf(OneFragment(), TwoFragment(), ThirdFragment())
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}