package com.example.testingproject.newsmodels

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "newsTable",indices = [Index(value = ["newsid"],unique = true)])
data class ArticleX(
    val author: String,
    val description: String,
    val publishedAt: String,
    val title: String,
    val urlToImage: String
) {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var newsid : Int? = null

}