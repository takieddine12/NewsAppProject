package com.example.testingproject.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.ExperimentalPagingApi
import com.example.testingproject.Utils
import com.example.testingproject.mvvm.MainViewModel
import com.example.testingproject.databinding.ActivityNewsDetailsBinding
import com.example.testingproject.models.FavNewsModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
@ExperimentalPagingApi
class NewsDetailsActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel? by viewModels()
    lateinit var binding: ActivityNewsDetailsBinding
    private lateinit var listOfNews : MutableList<FavNewsModel>
    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listOfNews = mutableListOf()

        val intent = intent
        intent?.let {
                binding.apply {
                    setAuthor.text = StringBuilder().append("Author").append(" ").append(it.getStringExtra("author")).toString()
                    favdescription.text = it.getStringExtra("description")
                    favtitle.text = it.getStringExtra("title")
                    textDate.text = formatDate(it.getStringExtra("date")!!)
                    Picasso.get().load(it.getStringExtra("imgUrl")).fit().into(favImage)

                    val favNewsModel = FavNewsModel(
                        author = it.getStringExtra("author"),
                        title = it.getStringExtra("title"),
                        urlToImage = it.getStringExtra("imgUrl"),
                        publishedAt = formatDate(it.getStringExtra("date")!!),
                        description = it.getStringExtra("description"),
                        isSaved = false
                    )
                    listOfNews.add(favNewsModel)
                }
            }


        // TODO : Get Saved Boolean Value
        mainViewModel?.getLiveData()?.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                binding.checkBox.isChecked = false
            } else {
                for (element in it) {
                    if(element.publishedAt.equals(intent.getStringExtra("date"))){
                        if (element.isSaved!!) {
                            binding.checkBox.isChecked = element.isSaved!!
                            break
                        } else {
                            binding.checkBox.isChecked = element.isSaved!!
                            break
                        }
                    } else {
                        binding.checkBox.isChecked = false
                    }
                }
            }
        })
        if (Utils.checkConnectivity(this)) {
            binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if(buttonView.isPressed){
                    if(isChecked){
                        listOfNews.map {
                            it.isSaved = isChecked
                            mainViewModel!!.insertNews(it)
                        }
                    } else {
                        listOfNews.map {
                            it.isSaved = isChecked
                            mainViewModel?.deletePerAuthor(it.author!!)
                        }
                    }
                }
            }
        }
    }

    private fun formatDate(date : String) : String {
        val simpleDateFormat = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
        val simpleDate = simpleDateFormat.parse(date)

        return simpleDateFormat.format(simpleDate)
    }
}

