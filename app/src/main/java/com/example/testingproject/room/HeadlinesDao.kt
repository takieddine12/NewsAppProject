package com.example.testingproject.room

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
     fun restoreHeadlines() : androidx.paging.DataSource.Factory<Int, Article>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun storeHeadlines(list: Article)

}