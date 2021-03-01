package com.example.testingproject.newsmodels

import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.testingproject.room.typeconverters.HeadlinesTypeConverter
import org.jetbrains.annotations.NotNull

data class HeadlinesModel(
    @TypeConverters(HeadlinesTypeConverter::class)
    val articles: MutableList<Article>? = null
)