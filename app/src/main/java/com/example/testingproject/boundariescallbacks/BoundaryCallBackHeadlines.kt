package com.example.testingproject.boundariescallbacks

import android.util.Log
import androidx.paging.PagedList
import com.example.testingproject.room.HeadlinesDao
import com.example.testingproject.Utils
import com.example.testingproject.newsmodels.Article
import com.example.testingproject.newsmodels.HeadlinesModel
import com.example.testingproject.viewmodel.MainViewModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


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