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
    RemoteMediator<Int,NewsModel>() {

    private val initialPage : Int = 1
    override suspend fun load(loadType: LoadType, state: PagingState<Int, NewsModel>): MediatorResult {
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
    private fun getRemoteKeyForLastItem(state : PagingState<Int,NewsModel>) : NewsItemRemoteKeys? {
        return state.lastItemOrNull()?.let {
            it.articles.map {
                mainViewModel.getNewsItemPerId(it.newsid!!)
            }
        }
    }
    private fun getRemoteKeyClosestToCurrentPosition(state : PagingState<Int,NewsModel>) : NewsItemRemoteKeys? {
       return state.anchorPosition?.let {
           state.closestItemToPosition(it).articles.map {
               it.newsid?.let { mainViewModel.getNewsItemPerId(it!!) }
           }
        }
    }
}
