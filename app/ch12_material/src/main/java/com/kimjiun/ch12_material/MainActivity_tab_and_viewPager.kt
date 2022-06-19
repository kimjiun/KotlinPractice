package com.kimjiun.ch12_material

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kimjiun.ch12_material.databinding.ActivityMainTabAndViewpagerBinding

class MainActivity_tab_and_viewPager : AppCompatActivity() {
    lateinit var binding : ActivityMainTabAndViewpagerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainTabAndViewpagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tab1: TabLayout.Tab = binding.tabs.newTab()
        tab1.text = "Tab1"
        binding.tabs.addTab(tab1)

        val tab2: TabLayout.Tab = binding.tabs.newTab()
        tab2.text = "Tab2"
        binding.tabs.addTab(tab2)

        val tab3: TabLayout.Tab = binding.tabs.newTab()
        tab3.text = "Tab3"
        binding.tabs.addTab(tab3)

        binding.viewPager.adapter = MyFragmentPagerAdapter(this)

        TabLayoutMediator(binding.tabs, binding.viewPager){ tab, position ->
            tab.text = "TAB${position + 1}"
        }.attach()

        /*
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.tabContent, OneFragment())
        transaction.commit()

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val transaction = supportFragmentManager.beginTransaction()
                when(tab?.text){
                    "Tab1" -> transaction.replace(R.id.tabContent, OneFragment())
                    "Tab2" -> transaction.replace(R.id.tabContent, TwoFragment())
                    "Tab3" -> transaction.replace(R.id.tabContent, ThirdFragment())
                }
                transaction.commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d("kkang", "onTabUnselected........")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d("kkang", "onTabReselected........")
            }
        })

         */
    }

    class MyFragmentPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity){
        val fragments: List<Fragment>
        init {
            fragments= listOf(OneFragment(), TwoFragment(), ThirdFragment())
        }
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}