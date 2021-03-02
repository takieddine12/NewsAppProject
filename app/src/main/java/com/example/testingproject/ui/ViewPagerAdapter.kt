package com.example.testingproject.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.testingproject.ui.fragments.NewsFragment
import com.example.testingproject.ui.fragments.HeadlinesFragment
import kotlinx.coroutines.newFixedThreadPoolContext


class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val tabs = 2
    override fun getItemCount(): Int {
        return tabs
    }

    @ExperimentalPagingApi
    override fun createFragment(position: Int): Fragment {
        if(position == 0){
            NewsFragment()
        } else {
            HeadlinesFragment()
        }
        return NewsFragment()
    }

}