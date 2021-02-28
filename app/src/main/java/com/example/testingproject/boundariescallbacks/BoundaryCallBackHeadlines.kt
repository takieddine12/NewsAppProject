package com.example.testingproject.boundariescallbacks

import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.example.testingproject.room.HeadlinesDao
import com.example.testingproject.Utils
import com.example.testingproject.models.NewsItemRemoteKeys
import com.example.testingproject.newsmodels.HeadlinesModel
import com.example.testingproject.mvvm.MainViewModel
import com.example.testingproject.newsmodels.Article
import com.example.testingproject.room.NewsDatabase
import com.example.testingproject.webauth.ApiResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        val response = apiResponse.getHeadlines(country,apiKey = Utils.API_KEY,page = page).articles
        val endOfPaginationReached = response.size < state.config.pageSize
        // TODO : Saving Response Into Database
        if(loadType == LoadType.REFRESH){
            mainViewModel.clearHeadlinesRemoteKey()
            mainViewModel.deleteAllHeadlines()
        }

        val prevKey = if (page == initialPage) null else page - 1
        val nextKey = if (endOfPaginationReached) null else page + 1
        val keys = response.map {
            NewsItemRemoteKeys(
                newsId = it.headlinesId,
                prevKey = prevKey!!,
                nextKey = nextKey!!
            )

        }

        mainViewModel.insertHeadlinesRemoteKeys(keys)
        response.map {
            mainViewModel.insertHeadlines(it)
        }

        return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
    }

    // outside of load function
    private  fun getRemoteKeyForLastItem(state: PagingState<Int, Article>): NewsItemRemoteKeys? {
      return  state.lastItemOrNull()?.let {
             mainViewModel.getHeadlinesItemPerId(it.headlinesId!!)
      }
    }


    // TODO : we can get the NewsRemoteKeys which points to the current item at this scroll position,
    //  so the value of (nextKey – 1) will actually be current page that we need to refresh.
    private  fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Article>): NewsItemRemoteKeys? {
        return state.anchorPosition?.let {
           state.closestItemToPosition(anchorPosition = it)?.headlinesId?.let {
               mainViewModel.getHeadlinesItemPerId(it)
           }
       }
    }

}


/*

class BoundaryCallBackHeadlines(var headlinesDao: HeadlinesDao,
                                var country : String,
                                var mainViewModel: MainViewModel) :
    PagedList.BoundaryCallback<HeadlinesModel>() {
    private var page = 1
    private var  isRequestInProgress = false
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        saveHeadlines(country)

    }
    override fun onItemAtFrontLoaded(itemAtFront: HeadlinesModel) {
        super.onItemAtFrontLoaded(itemAtFront)
        saveHeadlines(country)
    }

    override fun onItemAtEndLoaded(itemAtEnd: HeadlinesModel) {
        super.onItemAtEndLoaded(itemAtEnd)
        saveHeadlines(country)

    }

    private fun saveHeadlines(country : String) = CoroutineScope(Dispatchers.IO).launch{
        page++
        mainViewModel.getAllHeadlines(country, Utils.API_KEY, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(object : Observer<HeadlinesModel>{
                    override fun onSubscribe(d: Disposable) {

                    }
                    override fun onNext(t: HeadlinesModel) {
                        launch (Dispatchers.IO){
                            t.articles.map {
                                headlinesDao.storeHeadlines(it)
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
}
 */