package com.example.testingproject.paging.HeadlinesSource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.testingproject.newsmodels.HeadlinesModel
import com.example.testingproject.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope

class HeadlinesSource(var mainViewModel: MainViewModel,var scope : CoroutineScope,var country : String, var apikey: String,var page : Int) : androidx.paging.DataSource.Factory<Int, HeadlinesModel>() {
    var mutablelivedata  : MutableLiveData<PageKeyedDataSource<Int, HeadlinesModel>> = MutableLiveData()
    private var headlinesSourceFactory : HeadlinesSourceFactory? = null

    override fun create(): DataSource<Int, HeadlinesModel> {
        headlinesSourceFactory = HeadlinesSourceFactory(mainViewModel,scope,country,apikey,page)
        mutablelivedata.postValue(headlinesSourceFactory)
        return headlinesSourceFactory!!
    }
}