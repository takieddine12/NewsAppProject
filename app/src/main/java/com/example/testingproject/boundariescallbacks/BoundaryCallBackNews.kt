package com.example.testingproject.boundariescallbacks

import androidx.paging.*
import com.example.testingproject.Utils
import com.example.testingproject.models.NewsItemRemoteKeys
import com.example.testingproject.mvvm.MainViewModel
import com.example.testingproject.newsmodels.ArticleX
import com.example.testingproject.webauth.ApiResponse

@ExperimentalPagingApi
class BoundaryCallBackNews(
    var apiResponse: ApiResponse,
    var query: String,
    var mainViewModel: MainViewModel) :
    RemoteMediator<Int, ArticleX>() {

    private val initialPage: Int = 1
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleX>
    ): MediatorResult {
        val pageState = when (loadType) {
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
        val response = apiResponse.getNews(query, apiKey = Utils.API_KEY, page = pageState).articles
        val endOfPaginationReached = response!!.size < state.config.pageSize
        if (loadType == LoadType.REFRESH) {
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

    private fun getRemoteKeyForLastItem(state: PagingState<Int, ArticleX>): NewsItemRemoteKeys? {
        return state.lastItemOrNull()?.newsid?.let { mainViewModel.getNewsItemPerId(it) }
    }
    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ArticleX>): NewsItemRemoteKeys? {
        return state.anchorPosition.let {
            state.closestItemToPosition(it!!)!!.newsid.let {
                mainViewModel.getNewsItemPerId(it!!)
            }
        }
    }
}



/*
return state.anchorPosition?.let {
        state.closestItemToPosition(anchorPosition = it)?.newsid?.let {
            mainViewModel.getHeadlinesItemPerId(it)
        }
    }
 */
