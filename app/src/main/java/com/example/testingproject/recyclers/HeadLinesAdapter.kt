package com.example.testingproject.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testingproject.R
import com.example.testingproject.databinding.HeadlineRowLayoutBinding
import com.example.testingproject.newslistener.HeadlinesOnListener
import com.example.testingproject.newsmodels.Article
import com.example.testingproject.newsmodels.HeadlinesModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HeadLinesAdapter(private var headlinesOnListener: HeadlinesOnListener) : PagingDataAdapter<Article, HeadLinesAdapter.ViewHolder>(DiffutilCallBack()) {

    class ViewHolder(var headlinesRowLayoutBinding: HeadlineRowLayoutBinding) : RecyclerView.ViewHolder(headlinesRowLayoutBinding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val headlinesRowLayoutBinding  = DataBindingUtil.inflate<HeadlineRowLayoutBinding>(
            LayoutInflater.from(parent.context), R.layout.headline_row_layout,parent,false)
        return ViewHolder(headlinesRowLayoutBinding)
    }
    class DiffutilCallBack : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
           return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val headline = getItem(position)
//        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
//        val date = simpleDateFormat.parse(headline!!.publishedAt!!)
//        val formatttedDate = simpleDateFormat.format(date!!)
//        headline.publishedAt = formatttedDate

        CoroutineScope(Dispatchers.Main).launch {
           delay(2000)
            holder.headlinesRowLayoutBinding.headlinesProgressBar.visibility = View.INVISIBLE
        }

        holder.headlinesRowLayoutBinding.headlines = headline
        holder.headlinesRowLayoutBinding.listener = headlinesOnListener
    }

}