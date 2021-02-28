package com.example.testingproject.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testingproject.newsmodels.Article
import com.example.testingproject.newsmodels.ArticleX
import com.example.testingproject.newsmodels.HeadlinesModel

@Dao
interface HeadlinesDao {

    @Query("SELECT * FROM  headlinesTable ORDER by headlinesId")
     fun getAllHeadlines() : PagingSource<Int, Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertHeadlines(list: Article)

     @Query("DELETE FROM headlinesTable")
     fun deleteAllHeadlines()

}