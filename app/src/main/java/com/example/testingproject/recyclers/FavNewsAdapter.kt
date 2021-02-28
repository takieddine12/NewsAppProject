package com.example.testingproject.recyclers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.testingproject.ui.NewsDetailsActivity
import com.example.testingproject.R
import com.example.testingproject.mvvm.MainViewModel
import com.example.testingproject.databinding.FavRowLayoutBinding
import com.example.testingproject.models.FavNewsModel
import com.example.testingproject.newslistener.FavOnListener

class FavNewsAdapter(var context: Context,var list : List<FavNewsModel>) :
    RecyclerView.Adapter<FavNewsAdapter.ViewHolder>() {

    private var mainViewModel : MainViewModel? = null
    class ViewHolder(var favRowLayoutBinding: FavRowLayoutBinding) : RecyclerView.ViewHolder(favRowLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val favRowLayoutBinding = DataBindingUtil.inflate<FavRowLayoutBinding>(LayoutInflater.from(context),
        R.layout.fav_row_layout,parent,false)
        return ViewHolder(favRowLayoutBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.favRowLayoutBinding.news = model
        mainViewModel = ViewModelProvider(context as FragmentActivity)[MainViewModel::class.java]
        holder.favRowLayoutBinding.favlistener  = object : FavOnListener {
            override fun openFav(favNewsModel: FavNewsModel) {
                Intent(context, NewsDetailsActivity::class.java).apply {
                    putExtra("author",favNewsModel.author)
                    putExtra("title",favNewsModel.title)
                    putExtra("description",favNewsModel.description)
                    putExtra("imgurl",favNewsModel.urlToImage)
                    putExtra("date",favNewsModel.publishedAt)
                    context.startActivity(this)
                }
            }
        }
    }
}