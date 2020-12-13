package com.example.testingproject.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.testingproject.Utils
import com.example.testingproject.boundariescallbacks.BoundaryCallBackHeadlines
import com.example.testingproject.boundariescallbacks.BoundaryCallBackNews
import com.example.testingproject.models.FavNewsModel
import com.example.testingproject.models.SuggestionsModel
import com.example.testingproject.newsmodels.Article
import com.example.testingproject.newsmodels.ArticleX
import com.example.testingproject.newsmodels.HeadlinesModel
import com.example.testingproject.newsmodels.NewsModel
import com.example.testingproject.paging.HeadlinesSource.HeadlinesSource
import com.example.testingproject.paging.NewsSource.DataFactory
import io.reactivex.Flowable
import kotlinx.coroutines.launch


class MainViewModel @ViewModelInject constructor(
    private  var newsRepository: NewsRepository) : ViewModel() {


     // TODO : Network Operations
    fun getAllNews(Query : String , ApiKey : String , page : Int) : Flowable<NewsModel>{
        return newsRepository.getAllNews(Query,ApiKey,page)
    }
    fun getAllHeadlines(Query : String, ApiKey : String, page : Int) : Flowable<HeadlinesModel>{
        return newsRepository.getAllHeadlines(Query,ApiKey,page)
    }
    fun getAll(query: String, apiKey: String, page: Int): LiveData<PagedList<NewsModel>> {
        val dataFactory = DataFactory(this,viewModelScope,query, apiKey, page)

        val config = PagedList.Config.Builder()
            .setPageSize(Utils.NETWORK_PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()
        return LivePagedListBuilder(dataFactory, config)
            .setBoundaryCallback(BoundaryCallBackNews(newsRepository.newsDao,query,this)).build()
    }
    fun AllHeadlines(country: String, apiKey: String, page: Int): LiveData<PagedList<HeadlinesModel>> {
        val headlinesSource = HeadlinesSource(this,viewModelScope, country, apiKey, page)
        val config = PagedList.Config.Builder()
            .setPageSize(Utils.NETWORK_PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()

        return LivePagedListBuilder(headlinesSource, config)
            .setBoundaryCallback(BoundaryCallBackHeadlines(newsRepository.headlinesDao,country,this)).build()
    }


    // TODO : ROOM DATABASE OPERATIONS


    fun observeChanges(): LiveData<PagedList<ArticleX>> {
        val config = PagedList.Config.Builder()
            .setPageSize(Utils.CACHE_PAGE_SIZE)
            .build()

        return  LivePagedListBuilder(newsRepository.restoreNews(), config).build()

    }
    fun observeHeadlines(): LiveData<PagedList<Article>> {
        val config = PagedList.Config.Builder()
            .setPageSize(Utils.CACHE_PAGE_SIZE)
            .build()

        return  LivePagedListBuilder(newsRepository.restoreHeadlines(), config).build()

    }
    fun insertNews(favNewsModel: FavNewsModel) {
        viewModelScope.launch {
            newsRepository.insertNews(favNewsModel)
        }
    }
    fun deletePerNews(favNewsModel: FavNewsModel) {
        viewModelScope.launch {
            newsRepository.deletePerNews(favNewsModel)
        }
    }
    fun deleteAllNews() {
        viewModelScope.launch {
            newsRepository.deleteAllNews()
        }
    }
    fun getLiveData(): LiveData<List<FavNewsModel>> {
        return newsRepository.getLiveData()
    }
    fun getSuggestions() : LiveData<List<SuggestionsModel>> {
        return newsRepository.getAllSuggestions()
    }
    fun insertSuggestions(suggestionsModel: SuggestionsModel) {
        viewModelScope.launch {
            newsRepository.insertSuggestions(suggestionsModel)
        }
    }

}

