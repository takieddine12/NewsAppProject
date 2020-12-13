package com.example.testingproject.imgbindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

object ImgBinding {

    @BindingAdapter("bind:general")
    @JvmStatic
    fun getHeadlinesUrl(view : ImageView , url : String?) {
        Picasso.get().load(url).fit().into(view)
    }
}