package com.example.testingproject.newslistener

import com.example.testingproject.newsmodels.Article
import com.example.testingproject.newsmodels.HeadlinesModel

interface HeadlinesOnListener {
    fun headlinesOnClick(headlinesModel: Article)
}