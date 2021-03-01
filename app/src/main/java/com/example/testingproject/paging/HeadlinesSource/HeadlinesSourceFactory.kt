package com.example.testingproject.paging.HeadlinesSource

import android.util.Log
import android.util.TimeFormatException
import androidx.loader.content.Loader
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.example.testingproject.newsmodels.HeadlinesModel
import com.example.testingproject.mvvm.MainViewModel
import com.example.testingproject.newsmodels.Article
import com.example.testingproject.webauth.ApiResponse
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import timber.log.Timber

class HeadlinesSourceFactory(
    var apiResponse: ApiResponse,
    var country : String ,
    var apiKey : String
) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return -1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val nextPageNumber = params.key ?: 1
            LoadResult.Page(
                data = apiResponse.getHeadlines(country,apiKey,nextPageNumber).articles!!,
                prevKey = if(nextPageNumber == 1) null else nextPageNumber -1,
                nextKey = nextPageNumber + 1
            )
        }catch (ex : Exception){
            Timber.d("Exception ${ex.message}")
            LoadResult.Error(ex)
        }
    }


}

