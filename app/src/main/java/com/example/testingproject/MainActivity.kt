package com.example.testingproject


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.DisplayMetrics
import android.view.Gravity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import com.example.flatdialoglibrary.dialog.FlatDialog
import com.example.testingproject.databinding.ActivityMainBinding
import com.example.testingproject.ui.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var viewPagerAdapter: ViewPagerAdapter? = null
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        updateResources()
        setDarkLightMode()
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewPagerAdapter = ViewPagerAdapter(this)
        binding.mainViewPager.adapter = viewPagerAdapter
        binding.mainViewPager.offscreenPageLimit = 2
        TabLayoutMediator(binding.mainTablayout, binding.mainViewPager) { tab, position ->
            when(position) {
                0 -> {
                    tab.text = getString(R.string.allnews)
                }
                    else -> {
                        tab.text = getString(R.string.topheadlines)
                    }
            }
        }.attach()
        binding.refreshlayout.setOnRefreshListener {
                if(Utils.checkConnectivity(this)) {
                Intent(this@MainActivity, MainActivity::class.java).apply {
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                }
            }else {
                    binding.refreshlayout.isRefreshing  = false
                    this@MainActivity.showWarningToast(getString(R.string.networkerror))
                }
        }
        setUpNavigationDrawer()
    }

    private fun setUpNavigationDrawer() {
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.layout,
            binding.toolbar,
            0,
            0
        )
        actionBarDrawerToggle.syncState()
        binding.layout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.drawerArrowDrawable.color = Color.parseColor("#FFA500")
        binding.nav.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.version -> {
                    val flatDialog = FlatDialog(this)
                        flatDialog.setTitle("Application Info")
                        .setSubtitle("This app has a purpose of showing latest news and information which take place daily in different parts of the world..")
                        .setSubtitleColor(Color.GRAY)
                        .setFirstButtonText("Close")
                        .setFirstButtonColor(Color.BLUE)
                        .setFirstButtonTextColor(Color.WHITE)
                        .withFirstButtonListner {
                              flatDialog.dismiss()
                        }.show()
                }
            }
            binding.layout.closeDrawer(GravityCompat.START)
            true
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun updateResources(){
        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val language = prefs.getString("language", "en")
        val languageCode = prefs.getString("languageCode", "en-US")
        val configuration = baseContext.resources.configuration

        language?.let { lang ->
            languageCode?.let { langCode ->
              when {
                    Build.VERSION.SDK_INT  >= Build.VERSION_CODES.N -> {
                        configuration.setLocales(LocaleList(Locale(lang, langCode)))
                        createConfigurationContext(configuration)
                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 -> {
                    configuration.locale  = Locale(lang, langCode)
                    createConfigurationContext(configuration)
                     }
                    else -> {
                    configuration.setLocale(Locale(lang, langCode))
                    resources.updateConfiguration(configuration, DisplayMetrics())
                     }
                }
            }
        }
    }
    private fun setDarkLightMode(){
        val modesPrefs = getSharedPreferences("modes",Context.MODE_PRIVATE)
        val isModeEnabled = modesPrefs.getBoolean("mode",false)
        if(isModeEnabled){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
    override fun onBackPressed() {
        moveTaskToBack(true)
    }
    override fun onDestroy() {
        if(binding.layout.isDrawerOpen(GravityCompat.START))  {
            binding.layout.closeDrawer(GravityCompat.START)
        }else {
            super.onDestroy()
        }
    }
}
