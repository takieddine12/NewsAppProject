package com.example.testingproject.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.paging.ExperimentalPagingApi
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.testingproject.R
import com.example.testingproject.databinding.ActivitySettingsBinding
import java.util.*

@ExperimentalPagingApi
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setHomeButtonEnabled(true)
        supportFragmentManager.beginTransaction().replace(R.id.framelayout, PreferenceSettings())
            .commit()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                Intent(this, MainActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return true
    }
        class PreferenceSettings : PreferenceFragmentCompat() {
            @SuppressLint("CommitPrefEdits")
            override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
                setPreferencesFromResource(R.xml.preferencesettings, rootKey)
                val frenchCheckBox: CheckBoxPreference =
                    preferenceManager.findPreference("french")!!
                val spanishCheckBox: CheckBoxPreference =
                    preferenceManager.findPreference("spanish")!!
                val englishCheckBox: CheckBoxPreference =
                    preferenceManager.findPreference("english")!!

                val sharedPreferences =
                    requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                frenchCheckBox.onPreferenceChangeListener =
                    Preference.OnPreferenceChangeListener { preference, newValue ->
                        if (newValue as Boolean) {
                            spanishCheckBox.isChecked = false
                            englishCheckBox.isChecked = false
                            editor.apply {
                                putString("language", "fr")
                                putString("languageCode", "fr-FR")
                                apply()
                            }
                            Intent(requireContext(), MainActivity::class.java).apply {
                                startActivity(this)
                            }
                        }
                        true
                    }
                spanishCheckBox.onPreferenceChangeListener =
                    Preference.OnPreferenceChangeListener { preference, newValue ->
                        if (newValue as Boolean) {
                            frenchCheckBox.isChecked = false
                            englishCheckBox.isChecked = false
                            editor.apply {
                                putString("language", "es")
                                putString("languageCode", "es-ES")
                                apply()
                            }

                            Intent(requireContext(), MainActivity::class.java).apply {
                                startActivity(this)
                            }
                        }
                        true
                    }
                englishCheckBox.onPreferenceChangeListener =
                    Preference.OnPreferenceChangeListener { preference, newValue ->
                        if (newValue as Boolean) {
                            frenchCheckBox.isChecked = false
                            spanishCheckBox.isChecked = false
                            editor.apply {
                                putString("language", "en")
                                putString("languageCode", "en-US")
                                apply()
                            }
                            Intent(requireContext(), MainActivity::class.java).apply {
                                startActivity(this)
                            }
                        }
                        true
                    }
            }
        }
    }

