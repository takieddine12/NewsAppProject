package com.example.testingproject.apirequest

import com.example.testingproject.newsmodels.Article
import com.example.testingproject.newsmodels.ArticleX
import com.example.testingproject.newsmodels.HeadlinesModel
import com.example.testingproject.newsmodels.NewsModel
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*


interface ApiResponse {

    @GET("everything")
     fun getNews(@Query("q") query: String, @Query("apiKey") apiKey : String
    ,@Query("page")page : Int) : Flowable<NewsModel>

    @GET("top-headlines")
     fun getHeadlines(@Query("country")country : String, @Query("apikey")apiKey: String,
    @Query("page") page : Int) : Flowable<HeadlinesModel>

}