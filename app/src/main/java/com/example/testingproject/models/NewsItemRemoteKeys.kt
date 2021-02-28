package com.example.testingproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "RemoteKeysTable")
data class NewsItemRemoteKeys (
    @PrimaryKey
    @NotNull
    val newsId : Long?,
    val prevKey : Int,
    val nextKey : Int
    )