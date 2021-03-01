package com.example.testingproject.room.typeconverters

import androidx.room.TypeConverter
import com.example.testingproject.newsmodels.ArticleX
import com.example.testingproject.newsmodels.NewsModel
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class NewsTypeConverter {

    private var gson = Gson()

    @TypeConverter
    fun fromStringToList(data : String?) : List<ArticleX>? {
        val type = object : TypeToken<List<ArticleX>>(){}.type
        return gson.fromJson(data,type)
    }

    @TypeConverter
    fun fromListToString(data : List<ArticleX>): String? {
        return gson.toJson(data)
    }
}