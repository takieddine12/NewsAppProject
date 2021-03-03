package com.example.testingproject.paging.NewsSource

import android.util.Log
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.example.testingproject.newsmodels.NewsModel
import com.example.testingproject.mvvm.MainViewModel
import com.example.testingproject.newsmodels.ArticleX
import com.example.testingproject.webauth.ApiResponse
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class NewsDataSource(
    var apiResponse: ApiResponse,
    var query: String,
    var apikey: String
) : PagingSource<Int, ArticleX>()  {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleX> {
        return try {

            val nextPageNumber = params.key ?: 1
            val response = apiResponse.getNews(query,apikey,nextPageNumber)

            LoadResult.Page(
                data = response.articles!!,
                prevKey = if(nextPageNumber == 1) null else nextPageNumber -1,
                nextKey =  nextPageNumber + 1
            )
        }catch (ex : Exception){
            Timber.d("Paging Exception ${ex.message}")
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleX>): Int? {
        return -1
    }


}

