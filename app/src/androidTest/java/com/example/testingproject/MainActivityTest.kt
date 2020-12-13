package com.example.testingproject

import android.view.Gravity
import android.view.View
import android.widget.ActionMenuView
import android.widget.TableLayout
import androidx.test.InstrumentationRegistry.getInstrumentation
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.AllOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{


    @Test
    fun isMainActivityInView(){
        val activityScenario  = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.layout)).check(matches(isDisplayed()))
    }
    @Test
    fun isAllViewsInView(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.linearLayoutMainActivity)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.toolbar)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.main_tablayout)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.refreshlayout)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.main_view_pager)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

    }


    fun SelectPositionTabLayout(tabIndex : Int) : ViewAction{
        return object : ViewAction {
            override fun getDescription(): String  = "with tab at index$tabIndex"

            override fun getConstraints(): Matcher<View>  = allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))
            override fun perform(uiController: UiController?, view: View?) {
                var tableLayout  = view as TabLayout
                var tabIndexx : TabLayout.Tab = tableLayout.getTabAt(tabIndex)!!
                    ?: throw PerformException.Builder()
                        .withCause(Throwable("No Tab Selected at the at position"))
                        .build()

                tabIndexx.select()
            }

        }

    }

    @Test
    fun TestTabLayout(){
        var scenarioActivity = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.main_tablayout)).perform(SelectPositionTabLayout(0))
        onView(withId(R.id.main_tablayout)).perform(SelectPositionTabLayout(1))
    }

    @Test
    fun TestNavigationDrawer(){
        var activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.layout))
            .check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())

        onView(withId(R.id.nav)).perform(NavigationViewActions.navigateTo(R.id.settings))
        onView(withId(R.id.nav)).perform(NavigationViewActions.navigateTo(R.id.deletesuggestion))

    }

    @Test
    fun TestViewPager(){
        val activityScenario  = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.main_view_pager)).perform(swipeLeft())
    }

}