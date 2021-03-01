package com.example.testingproject.newsmodels

import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.testingproject.room.typeconverters.NewsTypeConverter
import org.jetbrains.annotations.NotNull

data class NewsModel(
    @TypeConverters(NewsTypeConverter::class)
    var articles: MutableList<ArticleX>? = null

)