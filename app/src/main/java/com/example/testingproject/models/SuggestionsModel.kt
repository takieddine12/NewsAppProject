package com.example.testingproject.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "suggestions_table",indices = [Index(value = ["suggestionId"],unique = true)])
class SuggestionsModel(
    var suggestion : String? = null
) {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var suggestionId : Int? = null
}