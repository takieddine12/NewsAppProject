package com.example.testingproject.ui


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.DisplayMetrics
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.testingproject.R
import com.example.testingproject.Utils
import com.example.testingproject.databinding.ActivityMainBinding
import com.example.testingproject.extras.Common
import com.example.testingproject.showWarningToast
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var viewPagerAdapter: ViewPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        updateResources()
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewPagerAdapter = ViewPagerAdapter(this)
        binding.mainViewPager.adapter = viewPagerAdapter
        binding.mainViewPager.offscreenPageLimit = 2
        TabLayoutMediator(binding.tabLayout, binding.mainViewPager) { tab, position ->
            when(position) {
                0 -> {
                    tab.text = getString(R.string.allnews)
                }
                    else -> {
                        tab.text = getString(R.string.topheadlines)
                    }
            }
        }.attach()

        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(Common.dpToPx(8), 0, Common.dpToPx(8), 0)
            tab.requestLayout()
        }

        binding.refreshLayout.setOnRefreshListener {
                if(Utils.checkConnectivity(this)) {
                Intent(this@MainActivity, MainActivity::class.java).apply {
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                }
                 }else {
                    binding.refreshLayout.isRefreshing  = false
                    this@MainActivity.showWarningToast(getString(R.string.networkerror))
                }
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
    override fun onBackPressed() {
        moveTaskToBack(true)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
