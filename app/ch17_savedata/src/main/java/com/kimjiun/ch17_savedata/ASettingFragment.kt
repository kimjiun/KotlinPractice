package com.kimjiun.ch17_savedata

import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.preference.*

class ASettingFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_a, rootKey)

        val a1 : SwitchPreferenceCompat? = findPreference("a1")
        val idPreference: EditTextPreference? = findPreference("id")
        val colorPreference: ListPreference? = findPreference("color")

        // 설정제어
        idPreference?.isVisible = true
        //idPreference?.summary = "code Summary"
        //idPreference?.title = "code title"

        // 서머리를 알아서 변경해줌
        idPreference?.summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()
        colorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

        idPreference?. summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference: EditTextPreference ->
            val text = preference.text

            if(TextUtils.isEmpty(text)){
                "설정이 되지 않았습니다."
            }
            else{
                "설정된 ID : $text"
            }
        }

        idPreference?.setOnPreferenceClickListener { pref ->
            Log.d("JIUNKIM", "PREF : ${pref.key}")
            true
        }

        idPreference?.setOnPreferenceChangeListener{preference, newValue ->
            Log.d("JIUNKIM", "CHANGE : ${preference.key}, / $newValue")
            true
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(preference: SharedPreferences?, key: String?) {
        if(key == "id") {
            Log.d("JIUNKIM", "CHANGE : ${key}, / ${preference?.getString(key, "")}")
        }
        else if(key == "a1") {
            Log.d("JIUNKIM", "CHANGE : ${key}, / ${preference?.getBoolean(key, false)}")
        }
    }
}