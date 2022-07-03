package com.kimjiun.ch17_savedata

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.kimjiun.ch17_savedata.databinding.ActivityMainBinding

class SettingActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    lateinit var binding: ActivityMainBinding
    val TAG = "JIUNKIM"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = getSupportFragmentManager();
        val frag = SettingFragment()
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        val transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, frag).commitAllowingStateLoss();

        binding.button.setOnClickListener {
            val id = sharedPref.getString("id", "")
            Log.d(TAG, "ID : $id")
        }
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        val args = pref.extras
        val fragment =
            pref.fragment?.let {
                supportFragmentManager.fragmentFactory.instantiate(classLoader,
                    it
                )
            }

        fragment?.arguments = args

        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit()
        }

        return true
    }
}