package com.example.testingproject.splash

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class Splash_ScreenTest{

    @get: Rule
    var activityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

}