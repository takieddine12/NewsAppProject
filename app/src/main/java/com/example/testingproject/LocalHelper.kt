package com.example.testingproject

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import android.util.DisplayMetrics
import java.util.*
class LocalHelper(base : Context) : ContextWrapper(base) {

    fun contextWrapper(context: Context) : ContextWrapper{
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val language = prefs.getString("language","en")
        val languageCode = prefs.getString("languageCode","en-US")

        val configuration = context.resources.configuration
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                configuration.setLocale(Locale(language,languageCode))
                val localeList = LocaleList(Locale(language,languageCode))
                configuration.setLocales(localeList)
                context.createConfigurationContext(configuration)
            }
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 -> {
//                configuration.setLocale(Locale(language,languageCode))
//                context.createConfigurationContext(configuration)
//            }
//            else -> {
//                configuration.locale = Locale(language,languageCode)
//                context.resources.updateConfiguration(configuration, DisplayMetrics())
//            }
        }

        return contextWrapper(context)

    }


}