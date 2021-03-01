package com.example.testingproject.newsdetails

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.testingproject.R
import com.example.testingproject.ui.NewsDetailsActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class News_Details_ActivityTest {
//    @get: Rule
//    var activityScenarioRule = ActivityScenarioRule(NewsDetailsActivity::class.java)
//
//    @Test
//    fun isActivityInView(){
//
//        onView(withId(R.id.newslayout)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
//        onView(withId(R.id.nestedscrollviw)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
//        onView(withId(R.id.linearLayoutactivitynews)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
//        onView(withId(R.id.appbarlayout)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
//        onView(withId(R.id.toolbar_layout)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
//        onView(withId(R.id.fav_image)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
//        onView(withId(R.id.toolbar_my)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
//        onView(withId(R.id.SetHeadline)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
//        onView(withId(R.id.favtitle)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
//        onView(withId(R.id.setAuthor)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
//        onView(withId(R.id.SetPublishedAt)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
//        onView(withId(R.id.favpublishedat)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
//        onView(withId(R.id.setDetails)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
//
//
//    }
}