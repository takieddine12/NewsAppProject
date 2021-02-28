package com.example.testingproject.splash

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.testingproject.R
import com.example.testingproject.ui.LottieActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LottieSplashTest{

    @Test
    fun TestActivitiyAndview(){
        var scenario = ActivityScenario.launch(LottieActivity::class.java)
        onView(withId(R.id.linearlottie)).check(matches(isDisplayed()))
        onView(withId(R.id.lottieview)).check(matches(isDisplayed()))
    }
}