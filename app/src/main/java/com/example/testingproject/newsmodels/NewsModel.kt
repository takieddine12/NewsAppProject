package com.example.testingproject.newsmodels

import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

data class NewsModel(
    val articles: MutableList<ArticleX>

)