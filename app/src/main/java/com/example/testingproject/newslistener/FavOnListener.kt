package com.example.testingproject.newslistener

import com.example.testingproject.models.FavNewsModel

interface FavOnListener {
    fun openFav(favNewsModel: FavNewsModel)
}