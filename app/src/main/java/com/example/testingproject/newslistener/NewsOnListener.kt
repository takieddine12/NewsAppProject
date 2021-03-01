package com.example.testingproject.newslistener

import com.example.testingproject.newsmodels.ArticleX
import com.example.testingproject.newsmodels.NewsModel

interface NewsOnListener {

    fun onNewsClicked(allNewsModel: ArticleX)
}