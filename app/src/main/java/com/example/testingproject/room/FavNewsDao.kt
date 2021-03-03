package com.example.testingproject.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.testingproject.models.FavNewsModel

@Dao
interface FavNewsDao {

    @Query("SELECT * FROM fav_table")
    fun getAllFavNews() : LiveData<List<FavNewsModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavNews(favNewsModel: FavNewsModel)

    @Delete
    suspend fun deletePerFavNews(favNewsModel: FavNewsModel)

    @Query("DELETE FROM fav_table Where author = :author ")
    suspend fun deletePerAuthor(author : String)

    @Query("DELETE FROM fav_table")
    suspend fun deleteAllFavNews()

    @Query("DELETE FROM fav_table WHERE favnewsId NOT IN (SELECT MIN(favnewsId) FROM fav_table GROUP BY author, title)")
    suspend fun deleteDuplicateEntries()


}