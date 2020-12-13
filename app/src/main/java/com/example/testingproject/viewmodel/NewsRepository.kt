package com.example.testingproject.viewmodel


import com.example.testingproject.apirequest.ApiResponse
import com.example.testingproject.room.FavNewsDao
import com.example.testingproject.room.HeadlinesDao
import com.example.testingproject.room.NewsDao
import com.example.testingproject.models.FavNewsModel
import com.example.testingproject.models.SuggestionsModel
import com.example.testingproject.room.SuggestionsDao
import javax.inject.Inject

class  NewsRepository @Inject constructor(
    var apiResponse: ApiResponse,
    var dao: FavNewsDao,
    var newsDao: NewsDao,
    var headlinesDao: HeadlinesDao,
    var suggestionsDao: SuggestionsDao) {


    // TODO : Network Operations
    fun getAllNews(Query : String ,ApiKey : String , page : Int) = apiResponse.getNews(Query,ApiKey,page)
    fun getAllHeadlines(Query : String ,ApiKey : String , page : Int) = apiResponse.getHeadlines(Query,ApiKey,page)

     // FavNewsDao

    suspend fun insertNews(favNewsModel: FavNewsModel) = dao.insertNews(favNewsModel)
    suspend fun deletePerNews(favNewsModel: FavNewsModel) = dao.deletePerNews(favNewsModel)
    suspend fun deleteAllNews() = dao.deleteAllNews()
    fun getLiveData() = dao.getAllNews()

     //
     fun restoreHeadlines() = headlinesDao.restoreHeadlines()
     fun restoreNews() = newsDao.getNews()

    // Suggestions Dao

    suspend fun insertSuggestions(suggestionsModel: SuggestionsModel) = suggestionsDao.insertSuggestion(suggestionsModel)
    fun getAllSuggestions() = suggestionsDao.getAllSuggestions()


}