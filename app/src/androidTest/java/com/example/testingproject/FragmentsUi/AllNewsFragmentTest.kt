package com.example.testingproject.FragmentsUi

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.testingproject.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AllNewsFragmentTest{

    @Test
    fun isAllNewsFragmentVisible(){
//
//        onView(withId(R.id.newslayouty)).check(matches(isDisplayed()))
//        onView(withId(R.id.newslinearlayout)).check(matches(isDisplayed()))
//        onView(withId(R.id.newsRecycler)).check(matches(isDisplayed()))
//        onView(withId(R.id.newsProgressBar)).check(matches(isDisplayed()))
    }

    /*
    @Test
    fun TestMenu(){
        var scenario = launchFragmentInContainer<AllNewsFragment>()
        onView(withId(R.id.searchitem)).perform(click())
        onView(withId(com.google.android.material.R.id.search_src_text).matches(typeText("Italy"))
    }

     */

}