package com.example.testingproject.paging.NewsSource

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.testingproject.newsmodels.NewsModel
import com.example.testingproject.viewmodel.MainViewModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

class DataSourceFactory(
    var mainViewModel: MainViewModel,
    var coroutineScope: CoroutineScope,
    var query: String,
    var apikey: String,
    var page: Int
) : PageKeyedDataSource<Int, NewsModel>()  {

    private  var list: MutableList<NewsModel>?= null

    init {
        list = mutableListOf()
    }
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, NewsModel>) {
        coroutineScope.launch {
            try {
                mainViewModel.getAllNews(query,apikey,page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable()
                    .subscribe(object  : Observer<NewsModel>{
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(t: NewsModel) {
                            list?.add(t)
                            callback.onResult(list!!, null, 1)
                        }

                        override fun onError(e: Throwable) {

                        }

                        override fun onComplete() {

                        }
                    })
            } catch (e: Exception) {
                Log.d("TAG", "Error ...")
            }
        }
    }
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NewsModel>) {
        coroutineScope.launch {
            try {
                page++
                mainViewModel.getAllNews(query,apikey,page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable()
                    .subscribe(object : Observer<NewsModel>{
                        override fun onSubscribe(d: Disposable) {
                        }
                        override fun onNext(t: NewsModel) {
                            list?.add(t)
                            callback.onResult(list!!, params.key + 1)
                        }
                        override fun onError(e: Throwable) {

                        }

                        override fun onComplete() {

                        }
                    })

            } catch (e: Exception) {
                Log.d("TAG", "Error ...")
            }

        }
    }
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NewsModel>) {
        coroutineScope.launch {
            try {
                page--
                mainViewModel.getAllNews(query,apikey,page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable()
                    .subscribe(object : Observer<NewsModel>{
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(t: NewsModel) {
                            list?.add(t)
                            callback.onResult(list!!, if (params.key >= 1) null else params.key - 1)
                        }

                        override fun onError(e: Throwable) {

                        }

                        override fun onComplete() {

                        }
                    })
            } catch (e: Exception) {
                Log.d("TAG", "Error ...")
            }
        }
    }
}