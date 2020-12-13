///---------
package com.example.testingproject.paging.NewsSource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.testingproject.newsmodels.NewsModel
import com.example.testingproject.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope

class DataFactory(var mainViewModel: MainViewModel,var scope : CoroutineScope,var query : String ,var  apikey : String ,var  page : Int) : DataSource.Factory<Int,NewsModel>() {
    var mutabledata : MutableLiveData<DataSourceFactory> = MutableLiveData()
    var dataSourceFactory  : DataSourceFactory? = null
    override fun create(): DataSource<Int, NewsModel> {
        dataSourceFactory = DataSourceFactory(mainViewModel,scope,query,apikey,page)
        mutabledata.postValue(dataSourceFactory)

        return dataSourceFactory!!
    }


}