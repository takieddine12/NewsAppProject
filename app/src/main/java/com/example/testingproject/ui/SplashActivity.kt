package com.example.testingproject.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.testingproject.R
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntro2Fragment
import com.github.paolorotolo.appintro.model.SliderPage


class SplashActivity : AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val titlesArray = arrayOf(getString(R.string.dailynews),getString(R.string.updateheadlines))
        val headlinesArray = arrayOf(getString(R.string.headlinestitle),
            getString(R.string.headlinetitle2))
        val newsImages = arrayOf(R.drawable.ic_content,R.drawable.ic_newsletter)


        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)

        if (firstStart) {
            showIntro(titlesArray,headlinesArray,newsImages)
        } else
        {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun showIntro(titles_array : Array<String>,headlines_array : Array<String>,news_images : Array<Int>) {
            val sliderPage = SliderPage()
            for(i in titles_array.indices) {
                sliderPage.title = titles_array[i]
                sliderPage.description  = headlines_array[i]
                sliderPage.titleColor  = Color.GRAY
                sliderPage.descColor = Color.GRAY
                sliderPage.imageDrawable = news_images[i]
                sliderPage.bgColor = Color.WHITE
                sliderPage.titleTypeface = "zerofont.ttf"
                addSlide(AppIntro2Fragment.newInstance(sliderPage))

                selectedIndicatorColor  = Color.BLACK
                unselectedIndicatorColor = Color.GRAY

                showSkipButton(true)

                val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putBoolean("firstStart", false)
                editor.apply()
            }
    }
    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }
    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }
}
