package com.example.testingproject.boundariescallbacks

import androidx.paging.*
import com.example.testingproject.Utils
import com.example.testingproject.models.NewsItemRemoteKeys
import com.example.testingproject.mvvm.MainViewModel
import com.example.testingproject.newsmodels.Article
import com.example.testingproject.webauth.ApiResponse
import java.io.InvalidObjectException


@ExperimentalPagingApi
class BoundaryCallBackHeadlines(
    var country : String,
    var mainViewModel: MainViewModel,
    var apiResponse: ApiResponse) :
    RemoteMediator<Int,Article>() {

    private var initialPage : Int = 1
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Article>): MediatorResult {
        // TODO : Pushing Data Into Database
        val page = when(loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: initialPage
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(true)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                    ?: throw InvalidObjectException("Result Is Empty")
                remoteKeys.nextKey ?: return MediatorResult.Success(true)
            }
        }
        val response = apiResponse.getHeadlines(country, Utils.API_KEY, page).articles
        val endOfPaginationReached = response!!.size < state.config.pageSize
        // TODO : Saving Response Into Database
        if(loadType == LoadType.REFRESH){
            mainViewModel.clearHeadlinesRemoteKey()
            mainViewModel.deleteAllHeadlines()
        }

        val prevKey = if (page == initialPage) null else page - 1
        val nextKey = if (endOfPaginationReached) null else page + 1
        val keys = response?.map {
            NewsItemRemoteKeys(
                newsId = it.headlinesId,
                prevKey = prevKey!!,
                nextKey = nextKey!!
            )

        }

        mainViewModel.insertHeadlinesRemoteKeys(keys!!)
        response.map {
            mainViewModel.insertHeadlines(it)
        }

        return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
    }
    private  fun getRemoteKeyForLastItem(state: PagingState<Int, Article>): NewsItemRemoteKeys? {
      return state.lastItemOrNull()?.let { mainViewModel.getHeadlinesItemPerId(it.headlinesId!!) }
    }
    private  fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Article>): NewsItemRemoteKeys? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(anchorPosition = it)?.headlinesId?.let {
                mainViewModel.getHeadlinesItemPerId(it)
            }
       }
    }
}
