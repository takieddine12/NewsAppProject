package com.example.testingproject.newsmodels

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "headlinesTable",indices = [Index(value = ["headlinesId"],unique = true)])
data class Article(
    val author: String,
    val description: String,
    val publishedAt: String,
    val title: String,
    val urlToImage: String
) {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var headlinesId : Int? = null
}