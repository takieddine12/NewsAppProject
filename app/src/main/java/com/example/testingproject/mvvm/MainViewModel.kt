package com.example.testingproject.mvvm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.*
import com.example.testingproject.Utils
import com.example.testingproject.boundariescallbacks.BoundaryCallBackHeadlines
import com.example.testingproject.boundariescallbacks.BoundaryCallBackNews
import com.example.testingproject.models.FavNewsModel
import com.example.testingproject.models.NewsItemRemoteKeys
import com.example.testingproject.models.SuggestionsModel
import com.example.testingproject.newsmodels.Article
import com.example.testingproject.newsmodels.ArticleX
import com.example.testingproject.newsmodels.HeadlinesModel
import com.example.testingproject.newsmodels.NewsModel
import com.example.testingproject.paging.HeadlinesSource.HeadlinesSourceFactory
import com.example.testingproject.paging.NewsSource.NewsDataSource
import com.example.testingproject.webauth.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
@ExperimentalPagingApi
class MainViewModel @Inject constructor(
    private var apiResponse: ApiResponse,
    private var newsRepository: NewsRepository)
    : ViewModel() {


    ///-----Online
    fun getNews(query: String, apiKey: String): Flow<PagingData<NewsModel>> {
      return Pager(
          config = getConfig(),
          remoteMediator = BoundaryCallBackNews(apiResponse,query,this)){
          NewsDataSource(apiResponse,query,apiKey)
      }.flow
          .cachedIn(viewModelScope)
    }
    fun getHeadLines(country: String, apiKey: String): Flow<PagingData<HeadlinesModel>> {
         return Pager(
             config = getConfig(),
             remoteMediator = BoundaryCallBackHeadlines(country,this,apiResponse)){
             HeadlinesSourceFactory(apiResponse,country,apiKey)
         }.flow.cachedIn(viewModelScope)
    }

    //-----Offline
    fun getOfflineNews() : Flow<PagingData<NewsModel>> {
        return Pager(
            config = getConfig()){
             newsRepository.getAllNews()
        }.flow.cachedIn(viewModelScope)
    }

    fun getOfflineHeadlines() : Flow<PagingData<HeadlinesModel>> {
        return Pager(config = getConfig()){
            newsRepository.getAllHeadlines()
        }.flow.cachedIn(viewModelScope)
    }




    // TODO : Dao Headlines Section

    suspend fun insertHeadlines(headlinesModel: Article) = newsRepository.insertHeadlines(headlinesModel)

    fun deleteAllHeadlines() = viewModelScope.launch {
            newsRepository.deleteAllHeadlines()
    }
    // TODO : Dao News Section

    fun deleteAllNews()  = viewModelScope.launch {
            newsRepository.deleteAllNews()
    }
    fun insertAllNews(model : ArticleX) = viewModelScope.launch {
           newsRepository.insertAllNews(model)
    }

    // TODO : Dao Favs Section
    fun insertNews(favNewsModel: FavNewsModel)  = viewModelScope.launch {
        newsRepository.insertFavNews(favNewsModel)
    }

    fun deletePerFavNews(favNewsModel: FavNewsModel) = viewModelScope.launch {
        newsRepository.deleteFavPerNews(favNewsModel)
    }

    fun getLiveData(): LiveData<List<FavNewsModel>> = newsRepository.getLiveData()
    fun getSuggestions() : LiveData<List<SuggestionsModel>>  = newsRepository.getAllSuggestions()
    fun insertSuggestions(suggestionsModel: SuggestionsModel) = viewModelScope.launch{
        newsRepository.insertSuggestions(suggestionsModel)
    }


    // TODO : Remote Keys Dao Section
   suspend fun insertHeadlinesRemoteKeys(list : List<NewsItemRemoteKeys>)
     =  newsRepository.insertHeadlinesRemoteKey(list)

    suspend fun insertNewsRemoteKeys(list : List<NewsItemRemoteKeys>)
     = newsRepository.insertNewsRemoteKey(list)

     fun clearNewsRemoteKey() = newsRepository.clearNewsKeys()
     fun clearHeadlinesRemoteKey() = newsRepository.clearHeadlinesKeys()
     fun getNewsItemPerId(key : Long) = newsRepository.getNewsPerId(key)
     fun getHeadlinesItemPerId(key : Long) = newsRepository.getHeadlinesItemPerId(key)
     private fun getConfig() : PagingConfig {
        return PagingConfig(
            pageSize = 10,
            prefetchDistance = 2,
            enablePlaceholders = true,
            initialLoadSize = 5
        )
    }


}

