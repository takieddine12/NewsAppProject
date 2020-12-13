package com.example.testingproject.boundariescallbacks

import android.util.Log
import androidx.paging.PagedList
import com.example.testingproject.room.NewsDao
import com.example.testingproject.Utils
import com.example.testingproject.newsmodels.Article
import com.example.testingproject.newsmodels.ArticleX
import com.example.testingproject.newsmodels.NewsModel
import com.example.testingproject.viewmodel.MainViewModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BoundaryCallBackNews(var newsDao: NewsDao,
                           var query : String,
                           var mainViewModel: MainViewModel) :
    PagedList.BoundaryCallback<NewsModel>() {

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
}