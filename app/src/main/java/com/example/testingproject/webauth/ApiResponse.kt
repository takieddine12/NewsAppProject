package com.example.testingproject.webauth

import com.example.testingproject.newsmodels.HeadlinesModel
import com.example.testingproject.newsmodels.NewsModel
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiResponse {

    @GET("everything")
     suspend fun getNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey : String
       ,@Query("page")page : Int) : NewsModel

    @GET("top-headlines")
    suspend fun getHeadlines(
        @Query("country") country : String,
        @Query("apikey")apiKey: String,
        @Query("page") page : Int) : HeadlinesModel

}