package com.example.testingproject.favSection

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.testingproject.R
import com.example.testingproject.ui.FavNewsActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class favNewsTest{

    @get: Rule
    var activityScenarioRule = ActivityScenarioRule(FavNewsActivity::class.java)

    @Test
    fun isActivityInView(){
        onView(withId(R.id.favlayout)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun TestViewsAreDisplayed(){

        onView(withId(R.id.favtoolbar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.fav_recycler)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

}