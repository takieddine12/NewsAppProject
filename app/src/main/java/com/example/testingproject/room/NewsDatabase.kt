package com.example.testingproject.room

import androidx.room.*
import com.example.testingproject.models.FavNewsModel
import com.example.testingproject.models.NewsItemRemoteKeys
import com.example.testingproject.models.SuggestionsModel
import com.example.testingproject.newsmodels.Article
import com.example.testingproject.newsmodels.ArticleX
import com.example.testingproject.newsmodels.NewsModel
import com.example.testingproject.room.pagingationDao.HeadlinesRemoteKeyDao
import com.example.testingproject.room.pagingationDao.NewsRemoteKeyDao
import com.example.testingproject.room.typeconverters.HeadlinesTypeConverter
import com.example.testingproject.room.typeconverters.NewsTypeConverter

@Database(entities = [FavNewsModel::class,
    SuggestionsModel::class,
    Article::class,ArticleX::class, NewsItemRemoteKeys::class],
    version = 1,exportSchema = false)
@TypeConverters(
    NewsTypeConverter::class,
    HeadlinesTypeConverter::class
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun favNewsDao() : FavNewsDao
    abstract fun suggestionsDao() : SuggestionsDao
    abstract fun headlinesDao() : HeadlinesDao
    abstract fun newsDao() : NewsDao
    abstract fun headlinesRemoteKey() : HeadlinesRemoteKeyDao
    abstract fun newsRemoteKey() : NewsRemoteKeyDao

}