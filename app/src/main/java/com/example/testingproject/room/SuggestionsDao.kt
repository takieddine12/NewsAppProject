package com.example.testingproject.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.testingproject.models.SuggestionsModel

@Dao
interface SuggestionsDao {

    @Query("SELECT * FROM suggestions_table ORDER BY suggestionid")
    fun getAllSuggestions() : LiveData<List<SuggestionsModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(suggestionsModel: SuggestionsModel) : Long

    @Query("DELETE FROM suggestions_table")
     suspend fun deleteSuggestion()
}