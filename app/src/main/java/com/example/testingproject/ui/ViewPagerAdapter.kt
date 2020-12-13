package com.example.testingproject.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.testingproject.fragments.NewsFragment
import com.example.testingproject.fragments.HeadlinesFragment


class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val tabs = 2
    override fun getItemCount(): Int {
        return tabs
    }

    override fun createFragment(position: Int): Fragment {
        return if(position == 0)
            NewsFragment()
        else HeadlinesFragment()
    }
}