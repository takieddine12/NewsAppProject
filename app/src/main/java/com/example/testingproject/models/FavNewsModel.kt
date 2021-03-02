package com.example.testingproject.models

import androidx.room.*
import org.jetbrains.annotations.NotNull

@Entity(tableName = "fav_table",indices = [Index(value = ["favnewsId"],unique = true)])
class FavNewsModel(
     var author: String? = null,
     var title: String? = null,
     var urlToImage: String? = null,
     var publishedAt: String? = null,
     var description : String? = null,
     var isSaved : Boolean? = false
)
{
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var favnewsId: Long? = null


}