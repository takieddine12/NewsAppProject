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
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private var newsRepository: NewsRepository)
    : ViewModel() {

    @ExperimentalPagingApi
    fun getNews(query: String, apiKey: String): Flow<PagingData<NewsModel>> {
       return Pager(config = getConfig(),
           initialKey = 1,
           BoundaryCallBackNews(initialPage = 1,query = query,mainViewModel = this)){
            NewsDataSource(apiResponse = newsRepository.apiResponse,query = query,apikey = apiKey)
        }.flow
            .cachedIn(viewModelScope)

    }
    @ExperimentalPagingApi
    fun getHeadLines(country: String, apiKey: String): Flow<PagingData<HeadlinesModel>> {
           return Pager(config = getConfig(),
               initialKey = 1,
               BoundaryCallBackHeadlines(initialPage = 1,country = country,mainViewModel = this)){
               HeadlinesSourceFactory(apiResponse = newsRepository.apiResponse
               ,country = country,apiKey = apiKey)
           }.flow
               .cachedIn(viewModelScope)
    }


//    // TODO : ROOM DATABASE OPERATIONS
//    fun observeChanges(): LiveData<PagedList<ArticleX>> {
//        val config = PagedList.Config.Builder()
//            .setPageSize(Utils.CACHE_PAGE_SIZE)
//            .build()
//
//        return  LivePagedListBuilder(newsRepository.restoreNews(), config).build()
//
//    }
//    fun observeHeadlines(): LiveData<PagedList<Article>> {
//        val config = PagedList.Config.Builder()
//            .setPageSize(Utils.CACHE_PAGE_SIZE)
//            .build()
//
//        return  LivePagedListBuilder(newsRepository.restoreHeadlines(), config).build()
//
//    }


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

