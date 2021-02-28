package com.example.testingproject.models

import androidx.room.*
import org.jetbrains.annotations.NotNull

@Entity(tableName = "fav_table",indices = [Index(value = ["favnewsId"],unique = true)])
class FavNewsModel(
    @ColumnInfo(name = "author") var author: String? = null,
    @ColumnInfo(name = "title") var title: String? = null,
    @ColumnInfo(name = "urlToImage") var urlToImage: String? = null,
    @ColumnInfo(name = "publishedAt") var publishedAt: String? = null,
    @ColumnInfo(name = "description") var description : String? = null
)
{
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var favnewsId: Long? = null


}