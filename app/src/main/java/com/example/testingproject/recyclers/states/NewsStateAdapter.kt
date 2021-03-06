package com.example.testingproject.recyclers.states

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testingproject.R
import kotlinx.android.synthetic.main.stateslayout.view.*

class NewsStateAdapter (
    private val retry : () -> Unit
): LoadStateAdapter<NewsStateAdapter.NewsViewHolder>(){

    class NewsViewHolder(private var itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): NewsViewHolder {
        val view  = LayoutInflater.from(parent.context)
            .inflate(R.layout.stateslayout,parent,false)
        return NewsViewHolder(view)
    }


    override fun onBindViewHolder(holder: NewsViewHolder, loadState: LoadState) {

        val progress = holder.itemView.load_state_progress
        val btnRetry = holder.itemView.load_state_retry
        val errorTextMessage  = holder.itemView.load_state_errorMessage

        btnRetry.isVisible = loadState !is LoadState.Loading
        errorTextMessage.isVisible = loadState !is LoadState.Loading
        progress.isVisible = loadState is LoadState.Loading

        if (loadState is LoadState.Error){
            errorTextMessage.text = loadState.error.localizedMessage
        }

        btnRetry.setOnClickListener {
            retry.invoke()
        }
    }

}