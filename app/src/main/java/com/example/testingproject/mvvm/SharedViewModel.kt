package com.example.testingproject.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private var liveDataQuery : MediatorLiveData<String>? = null

    init {
        liveDataQuery = MediatorLiveData()
    }

    fun setQuery(query : String){
        liveDataQuery?.value = query
    }

    fun getQuery()  : LiveData<String> {
        return liveDataQuery!!
    }


}