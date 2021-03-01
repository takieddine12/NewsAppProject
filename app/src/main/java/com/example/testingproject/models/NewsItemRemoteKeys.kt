package com.example.testingproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "RemoteKeysTable")
data class NewsItemRemoteKeys (
    @PrimaryKey
    @NotNull
    var newsId : Long?,
    var prevKey : Int,
    var nextKey : Int
    )