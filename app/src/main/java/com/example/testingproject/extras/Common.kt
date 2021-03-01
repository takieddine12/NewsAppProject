package com.example.testingproject.extras

import android.content.Context
import android.content.res.Resources

object Common {

    fun dpToPx(dp: Int): Int {
        return ((dp * Resources.getSystem().displayMetrics.density).toInt());
    }

    fun pxToDp(px: Int, context: Context): Int {
        return ((px / Resources.getSystem().displayMetrics.density).toInt());
    }
}