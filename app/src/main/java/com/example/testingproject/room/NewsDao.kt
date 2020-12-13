package com.example.testingproject.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testingproject.newsmodels.ArticleX
import com.example.testingproject.newsmodels.NewsModel

@Dao
interface NewsDao {

    @Query("SELECT * FROM newstable ORDER BY newsid ")
    fun getNews() : androidx.paging.DataSource.Factory<Int,ArticleX>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun persistNews(list: ArticleX)

}