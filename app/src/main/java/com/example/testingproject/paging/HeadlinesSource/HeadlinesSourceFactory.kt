package com.example.testingproject.paging.HeadlinesSource

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.testingproject.newsmodels.HeadlinesModel
import com.example.testingproject.viewmodel.MainViewModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class HeadlinesSourceFactory(var mainViewModel: MainViewModel,var scope: CoroutineScope,var country : String , var apiKey : String,var page : Int = 1) : PageKeyedDataSource<Int, HeadlinesModel>() {

    private  var list : MutableList<HeadlinesModel>? = null

    init {
        list = mutableListOf()
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, HeadlinesModel>) {
       try {
           scope.launch(IO) {
               mainViewModel.getAllHeadlines(country, apiKey, page)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .toObservable()
                   .subscribe(object : Observer<HeadlinesModel>{
                       override fun onSubscribe(d: Disposable) {

                       }

                       override fun onNext(t: HeadlinesModel) {
                           list?.add(t)
                           callback.onResult(list!!, null, page)
                       }

                       override fun onError(e: Throwable) {

                       }

                       override fun onComplete() {

                       }
                   })
           }
       }catch (e : Exception){
           Log.d("TAG","Error ${e.message}")
       }
    }
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, HeadlinesModel>) {
      try {
          scope.launch(IO) {
              page++
              mainViewModel.getAllHeadlines(country, apiKey, page)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .toObservable()
                  .subscribe(object : Observer<HeadlinesModel>{
                      override fun onSubscribe(d: Disposable) {

                      }

                      override fun onNext(t: HeadlinesModel) {
                          list?.add(t)
                          callback.onResult(list!!, if (params.key >= 1) null else params.key - 1)
                      }

                      override fun onError(e: Throwable) {

                      }

                      override fun onComplete() {

                      }
                  })
          }
      }catch (e : Exception){
          Log.d("TAG","Error ${e.message}")
      }
    }
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, HeadlinesModel>) {
        try {
            scope.launch(IO) {
                page--
                mainViewModel.getAllHeadlines(country, apiKey, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable()
                    .subscribe(object : Observer<HeadlinesModel>{
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(t: HeadlinesModel) {
                            list?.add(t)
                            callback.onResult(list!!,if (params.key >= 1) null else params.key - 1)
                        }

                        override fun onError(e: Throwable) {

                        }

                        override fun onComplete() {

                        }
                    })

            }
        }catch (e : Exception){
            Log.d("TAG","Error ${e.message}")
        }
    }

}