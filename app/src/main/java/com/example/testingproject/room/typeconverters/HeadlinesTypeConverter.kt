package com.example.testingproject.room.typeconverters

import androidx.room.TypeConverter
import com.example.testingproject.newsmodels.Article
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class HeadlinesTypeConverter {

    private var gson = Gson()

    @TypeConverter
    fun fromStringToList(data : String?) : List<Article>? {
        val type = object : TypeToken<List<Article>>(){}.type
        return gson.fromJson(data,type)
    }

    @TypeConverter
    fun fromListToString(data : List<Article>): String? {
        return gson.toJson(data)
    }
}