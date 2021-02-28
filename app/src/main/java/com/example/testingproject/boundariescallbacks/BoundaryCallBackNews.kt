package com.example.testingproject.boundariescallbacks

import android.util.Log
import androidx.paging.*
import com.example.testingproject.room.NewsDao
import com.example.testingproject.Utils
import com.example.testingproject.models.NewsItemRemoteKeys
import com.example.testingproject.newsmodels.NewsModel
import com.example.testingproject.mvvm.MainViewModel
import com.example.testingproject.newsmodels.ArticleX
import com.example.testingproject.webauth.ApiResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class BoundaryCallBackNews(
    var apiResponse: ApiResponse,
    var query : String,
    var mainViewModel: MainViewModel) :
    RemoteMediator<Int,ArticleX>() {

    private val initialPage : Int = 1

    override suspend fun load(loadType: LoadType, state: PagingState<Int, ArticleX>): MediatorResult {
        val pageState = when(loadType){
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state = state)
                remoteKeys?.nextKey?.minus(1) ?: initialPage
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(true)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                remoteKeys?.nextKey ?: return MediatorResult.Success(true)
            }
        }


        // TODO : Inserting Response Into DataBase
        val response  = apiResponse.getNews(query,apiKey = Utils.API_KEY,page = pageState).articles
        val endOfPaginationReached = response.size < state.config.pageSize
        if(loadType == LoadType.REFRESH){
            mainViewModel.clearNewsRemoteKey()
            mainViewModel.deleteAllNews()
        }

        val prevKey = if (pageState == initialPage) null else pageState - 1
        val nextKey = if (endOfPaginationReached) null else pageState + 1
        val keys = response.map {
            NewsItemRemoteKeys(
                newsId = it.newsid,
                prevKey = prevKey!!,
                nextKey = nextKey!!
            )
        }

        mainViewModel.insertNewsRemoteKeys(keys)
        response.map {
            mainViewModel.insertAllNews(it)
        }

        return MediatorResult.Success(endOfPaginationReached)
    }


    private fun getRemoteKeyForLastItem(state : PagingState<Int,ArticleX>) : NewsItemRemoteKeys? {
        return state.lastItemOrNull()?.let {
            mainViewModel.getNewsItemPerId(it.newsid!!)
        }
    }


    // TODO : we can get the NewsRemoteKeys which points to the current item at this scroll position,
    //  so the value of (nextKey – 1) will actually be current page that we need to refresh.

    private fun getRemoteKeyClosestToCurrentPosition(state : PagingState<Int,ArticleX>) : NewsItemRemoteKeys? {
       return state.anchorPosition?.let {
           state.closestItemToPosition(it)?.newsid?.let {
               mainViewModel.getNewsItemPerId(it!!)
           }
        }
    }
}

/*

    private var page = 1
    private var  isRequestInProgress = false
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        saveNews(query)
    }
    override fun onItemAtEndLoaded(itemAtEnd: NewsModel) {
        super.onItemAtEndLoaded(itemAtEnd)
        saveNews(query)

    }
    override fun onItemAtFrontLoaded(itemAtFront: NewsModel) {
        super.onItemAtFrontLoaded(itemAtFront)
        saveNews(query)
    }
    private fun saveNews(query : String) = CoroutineScope(Dispatchers.IO).launch {
            page++
            mainViewModel.getAllNews(query, Utils.API_KEY,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(object : Observer<NewsModel>{
                    override fun onSubscribe(d: Disposable) {

                    }
                    override fun onNext(t: NewsModel) {
                        launch(Dispatchers.IO) {
                            t.articles.map {
                                newsDao.persistNews(it)
                                isRequestInProgress = true
                            }
                        }
                    }
                    override fun onError(e: Throwable) {
                        Log.d("CallBack Headlines","Error Headlines${e.localizedMessage}")
                    }
                    override fun onComplete() {

                    }
                })
    }
 */