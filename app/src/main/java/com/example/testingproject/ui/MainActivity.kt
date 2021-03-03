package com.example.testingproject.ui


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.speech.RecognizerIntent
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.barisatalay.filterdialog.FilterDialog
import com.barisatalay.filterdialog.model.DialogListener
import com.example.testingproject.R
import com.example.testingproject.Utils
import com.example.testingproject.databinding.ActivityMainBinding
import com.example.testingproject.extras.Common
import com.example.testingproject.mvvm.SharedViewModel
import com.example.testingproject.ui.fragments.HeadlinesFragment
import com.example.testingproject.ui.fragments.NewsFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {
    private var selectedPosition  = -1
    private lateinit var sharedViewModel: SharedViewModel
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var viewPagerAdapter: ViewPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        updateResources()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        sharedViewModel = ViewModelProviders.of(this)[SharedViewModel::class.java]

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

        // TODO : Query With SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                getCurrentVisibleFragment(newText)
                return true
            }
        })

        binding.nav.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.settings -> {
                    Intent(this@MainActivity, SettingsActivity::class.java).apply {
                        startActivity(this)
                    }
                }
                R.id.fav -> {
                    Intent(this@MainActivity, FavNewsActivity::class.java).apply {
                        startActivity(this)
                    }
                }
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.unique_menu, menu)
        val menuItem = menu?.findItem(R.id.language)
        menuItem?.isVisible = false
        binding.mainViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> {
                        menuItem?.isVisible = false
                        selectedPosition = position
                    }
                    1 -> {
                        menuItem?.isVisible = true
                        selectedPosition = position
                    }
                }
            }
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       when(item.itemId){
           R.id.refresh -> {
               Intent(this, MainActivity::class.java).apply {
                   flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                   startActivity(this)
               }
           }
           R.id.voice -> {
               voiceSearch()
           }
           R.id.language -> {
               showFilterDialog()
           }
       }
        return true
    }
    private fun voiceSearch() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pass a search")
        startActivityForResult(intent, SEARCH_REQUEST_CODE)

    }
    private fun getCurrentVisibleFragment(newText: String?){

        binding.mainViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> {
                        sharedViewModel.setQuery(newText?.toLowerCase()!!)
                    }
                    1 -> {
                        sharedViewModel.setQuery(newText?.toLowerCase()!!)
                    }
                }
            }
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SEARCH_REQUEST_CODE && resultCode == RESULT_OK) {
            val voiceList = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            for (i in 0 until voiceList!!.size) {
                binding.mainViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    }

                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        when(position){
                            0 -> {
                                voiceList[0].toLowerCase(Locale.ROOT).apply {
                                    sharedViewModel.setQuery(this)
                                    binding.searchView.queryHint = this
                                }
                            }
                            1 -> {
                                voiceList[0].toLowerCase(Locale.ROOT).apply {
                                    sharedViewModel.setQuery(this)
                                    binding.searchView.queryHint = this
                                }
                            }
                        }
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        super.onPageScrollStateChanged(state)
                    }
                })

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
    private fun countriesList() : List<String> {
        arrayListOf<String>().apply {
            add("ae")
            add("ar")
            add("at")
            add("au")
            add("be")
            add("bg")
            add("br")
            add("ca")
            add("ch")
            add("cn")
            add("co")
            add("cu")
            add("cz")
            add("de")
            add("en")
            return this
        }
    }
    private fun showFilterDialog() {
        FilterDialog(this).apply {
            searchBoxHint = "Search"
            toolbarTitle  = "Countries"
            setList(countriesList())

            show("code","name",
                DialogListener.Single { selectedItem ->
                    sharedViewModel.setQuery(selectedItem.name)
                    dispose()
                })
        }
    }
    companion object {
        const val  SEARCH_REQUEST_CODE = 1010
    }
}
