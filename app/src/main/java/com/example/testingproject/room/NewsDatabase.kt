package com.example.testingproject.room

import androidx.room.*
import com.example.testingproject.models.FavNewsModel
import com.example.testingproject.models.SuggestionsModel
import com.example.testingproject.newsmodels.Article
import com.example.testingproject.newsmodels.ArticleX
import com.example.testingproject.newsmodels.NewsModel

@Database(entities = [FavNewsModel::class,SuggestionsModel::class,Article::class,ArticleX::class],version = 1,exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun favNewsDao() : FavNewsDao
    abstract fun suggestionsDao() : SuggestionsDao
    abstract fun headlinesDao() : HeadlinesDao
    abstract fun newsDao() : NewsDao

}