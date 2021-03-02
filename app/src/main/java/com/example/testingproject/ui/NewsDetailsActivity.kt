package com.example.testingproject.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.ExperimentalPagingApi
import com.example.testingproject.showToast
import com.example.testingproject.Utils
import com.example.testingproject.mvvm.MainViewModel
import com.example.testingproject.databinding.ActivityNewsDetailsBinding
import com.example.testingproject.models.FavNewsModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_news__details_.*

@AndroidEntryPoint
@ExperimentalPagingApi
class NewsDetailsActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel? by viewModels()
    lateinit var binding: ActivityNewsDetailsBinding

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        intent?.let {
                binding.apply {
                    setAuthor.text = StringBuilder().append("Author").append(" ").append(it.getStringExtra("author")).toString()
                    favdescription.text = it.getStringExtra("description")
                    favtitle.text = it.getStringExtra("title")
                    textDate.text = it.getStringExtra("date")
                    Picasso.get().load(it.getStringExtra("imgUrl")).fit().into(favImage)
                }
            }


        // TODO : Get Saved Boolean Value
        mainViewModel?.getLiveData()?.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                binding.checkBox.isChecked = false
            } else {
                for (element in it) {
                    if(element.author.equals(intent.getStringExtra("author"))){
                        if (element.isSaved!!) {
                            binding.checkBox.isChecked = element.isSaved!!
                        } else {
                            binding.checkBox.isChecked = element.isSaved!!
                        }
                    } else {
                        binding.checkBox.isChecked = false
                    }
                }
            }
        })
        if (Utils.checkConnectivity(this)) {
            binding.checkBox.setOnCheckedChangeListener {
                    buttonView, isChecked ->
                if(buttonView.isPressed){

                    val favNewsModel = FavNewsModel(
                        intent.getStringExtra("author"),
                        intent.getStringExtra("title"),
                        intent.getStringExtra("imgUrl"),
                        intent.getStringExtra("date"),
                        intent.getStringExtra("description"),
                        isChecked)

                    if(isChecked){
                        mainViewModel!!.insertNews(favNewsModel)
                    } else {
                        mainViewModel?.deletePerFavNews(favNewsModel)
                    }
                }
            }
        }
    }
}

