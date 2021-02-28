package com.example.testingproject.room.pagingationDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testingproject.models.NewsItemRemoteKeys


@Dao
interface HeadlinesRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(remoteKey : List<NewsItemRemoteKeys>)

    @Query("SELECT * FROM RemoteKeysTable Where newsId = :remoteNewsId")
     fun getItemPerId(remoteNewsId : Long)

    @Query("DELETE FROM RemoteKeysTable")
     fun clearHeadlinesRemoteKey()
}