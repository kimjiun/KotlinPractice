package com.kimjiun.ch11_jetpack

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kimjiun.ch11_jetpack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            startActivity(Intent(this, MainActivity3::class.java))
        }

        //setSupportActionBar(binding.toolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            binding.textView.lineHeight = 20
        }
        binding.appcompatTextView.lineHeight = 50

        // 프래그먼트 동적 호출
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        val fragment = MyFragment()
        transaction.add(R.id.fragment_layout, fragment) // 프래그먼트가 출력될 뷰의 id 값
        transaction.commit()

        // 리사이클러 뷰 호출
        val datas = mutableListOf<String>()
        for(i in 1..10){
            datas.add("ITEM : $i")
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.HORIZONTAL, true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = MyAdapter(datas)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        datas.add("ITEM LAST")
        (binding.recyclerView.adapter as MyAdapter).notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 코드로 추가하는 방식
        //val menuItem1: MenuItem? = menu?.add(0, 0, 0, "menu1")
        //val menuItem2: MenuItem? = menu?.add(0, 1, 0, "menu2")

        // xml로 추가하는 방식
        menuInflater.inflate(R.menu.mymenu, menu)

        val menuItem = menu?.findItem(R.id.menusearch)
        val searchView = menuItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                Log.d("JIUn", "$p0")
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                Log.d("JIUn", "$p0")
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        0 -> {
            Log.d("JIUN", "${item.itemId} menu1")
            true
        }
        1 -> {
            Log.d("JIUN", "${item.itemId} menu2")
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}