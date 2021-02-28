package com.example.testingproject.mvvm


import androidx.lifecycle.viewModelScope
import com.example.testingproject.webauth.ApiResponse
import com.example.testingproject.room.FavNewsDao
import com.example.testingproject.room.HeadlinesDao
import com.example.testingproject.room.NewsDao
import com.example.testingproject.models.FavNewsModel
import com.example.testingproject.models.NewsItemRemoteKeys
import com.example.testingproject.models.SuggestionsModel
import com.example.testingproject.newsmodels.Article
import com.example.testingproject.newsmodels.ArticleX
import com.example.testingproject.room.SuggestionsDao
import com.example.testingproject.room.pagingationDao.HeadlinesRemoteKeyDao
import com.example.testingproject.room.pagingationDao.NewsRemoteKeyDao
import kotlinx.coroutines.launch
import javax.inject.Inject

class  NewsRepository @Inject constructor(
    var apiResponse: ApiResponse,
    var dao: FavNewsDao,
    var newsDao: NewsDao,
    var headlinesDao: HeadlinesDao,
    private val newsRemoteKeyDao: NewsRemoteKeyDao,
    private val headlinesRemoteKeyDao: HeadlinesRemoteKeyDao,
    var suggestionsDao: SuggestionsDao) {


     // TODO :  FavNewsDao Section

    suspend fun insertFavNews(favNewsModel: FavNewsModel) = dao.insertFavNews(favNewsModel)
    suspend fun deleteFavPerNews(favNewsModel: FavNewsModel) = dao.deletePerFavNews(favNewsModel)
    suspend fun deleteAllFavNews() = dao.deleteAllFavNews()
    fun getLiveData() = dao.getAllFavNews()

    // TODO : Headlines Dao Section
     fun getAllHeadlines() = headlinesDao.getAllHeadlines()
     suspend fun insertHeadlines(list : Article){
        headlinesDao.insertHeadlines(list)
    }
    fun deleteAllHeadlines() = headlinesDao.deleteAllHeadlines()

    // TODO : News Dao Section
     fun getAllNews() = newsDao.getAllNews()
     fun deleteAllNews() = newsDao.deleteAllNews()
     suspend fun insertAllNews(list : ArticleX)   = newsDao.persistNews(list)


    // Suggestions Dao
    suspend fun insertSuggestions(suggestionsModel: SuggestionsModel) = suggestionsDao.insertSuggestion(suggestionsModel)
    fun getAllSuggestions() = suggestionsDao.getAllSuggestions()

    // TODO : Remote Keys Dao Section
    suspend fun insertHeadlinesRemoteKey(list : List<NewsItemRemoteKeys>){
        headlinesRemoteKeyDao.insertRemoteKey(list)
    }
     fun getHeadlinesItemPerId(key : Long){
        headlinesRemoteKeyDao.getItemPerId(key)
    }
     fun clearHeadlinesKeys(){
        headlinesRemoteKeyDao.clearHeadlinesRemoteKey()
    }
     fun getNewsPerId(key : Long){
       newsRemoteKeyDao.getItemPerId(key)
    }
     fun clearNewsKeys(){
        newsRemoteKeyDao.clearRemoteKey()
    }
     suspend fun insertNewsRemoteKey(list : List<NewsItemRemoteKeys>){
        newsRemoteKeyDao.insertRemoteKey(list)
    }
}