package com.example.testingproject.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testingproject.Utils
import com.example.testingproject.databinding.AllnewsLayoutBinding
import com.example.testingproject.models.EventBusModel
import com.example.testingproject.mvvm.MainViewModel
import com.example.testingproject.newslistener.NewsOnListener
import com.example.testingproject.newsmodels.ArticleX
import com.example.testingproject.recyclers.NewsAdapter
import com.example.testingproject.recyclers.states.NewsStateAdapter
import com.example.testingproject.ui.NewsDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
@ExperimentalPagingApi
class NewsFragment : Fragment() {
    private lateinit var newsAdapter: NewsAdapter
    private  val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding : AllnewsLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AllnewsLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.newsRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)

            newsAdapter = NewsAdapter(object : NewsOnListener {
                override fun onNewsClicked(allNewsModel: ArticleX) {
                    Intent(requireContext(), NewsDetailsActivity::class.java).apply {
                        putExtra("author", allNewsModel.author)
                        putExtra("title", allNewsModel.title)
                        putExtra("description", allNewsModel?.description)
                        putExtra("imgUrl", allNewsModel.urlToImage)
                        putExtra("date", allNewsModel.publishedAt)
                        startActivity(this)
                    }
                }
            })
            binding.newsRecycler.adapter = newsAdapter.withLoadStateFooter(
                footer = NewsStateAdapter { newsAdapter.retry() })

        }

        if(Utils.checkConnectivity(requireContext())) {
            fetchData("algeria".toLowerCase(Locale.ROOT))
        } else {
            lifecycleScope.launchWhenStarted {
                mainViewModel.getOfflineNews().collect {
                    newsAdapter.submitData(it)
                }
            }
            binding.newsRecycler.adapter = newsAdapter
            if(newsAdapter.itemCount == 0){
                binding.noNews.visibility = View.VISIBLE
                Timber.d("Executed Here..visible")
            } else {
                binding.noNews.visibility = View.INVISIBLE
                Timber.d("Executed Here..invisible")
            }
        }

        binding.newsRecycler.adapter = newsAdapter



    }
    private fun fetchData(query: String) {

        lifecycleScope.launch {
            mainViewModel.getNews(query, Utils.API_KEY).collectLatest {
                newsAdapter.submitData(it)
            }
        }

        if(newsAdapter.itemCount == -1){
            binding.noNews.visibility = View.VISIBLE
        } else {
            binding.noNews.visibility = View.INVISIBLE
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
     fun onMessageEventMain(messageEvent: EventBusModel){
        if(messageEvent.query.isNotEmpty()){
            fetchData(messageEvent.query)
        }
        // we receive data here
    }



    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}















        /*
          //val menuItem = menu.findItem(R.id.newsSearchView).actionView as SearchView
        menuItem.findViewById<ImageView>(androidx.appcompat.R.id.search_button).apply {
            setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_search_black_24dp))
        }
        menuItem.findViewById<SearchView.SearchAutoComplete>(androidx.appcompat
            .R.id.search_src_text).apply {
                setHintTextColor(Color.WHITE)
                setTextColor(Color.WHITE)
        }
        menuItem.queryHint = "search"
        menuItem.findViewById<AutoCompleteTextView>(R.id.search_src_text).threshold = 1
        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.item_label)
        val cursorAdapter = SimpleCursorAdapter(
            requireContext(),
            R.layout.suggestions_layout,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )
        menuItem.suggestionsAdapter = cursorAdapter
        menuItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                fetchData(query!!)
                CoroutineScope(Dispatchers.IO).launch {
                    mainViewModel.insertSuggestions(SuggestionsModel(query))
                }

                val cursor = MatrixCursor(
                    arrayOf(
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1
                    )
                )
                CoroutineScope(Dispatchers.IO).launch {
                    mainViewModel.getSuggestions().observe(
                        viewLifecycleOwner,
                        androidx.lifecycle.Observer { list ->
                            query.let {
                                list.forEachIndexed { index, suggestionsModel ->
                                    if (suggestionsModel.suggestion?.contains(it!!, true)!!) {
                                        cursor.addRow(arrayOf(index, suggestionsModel.suggestion))
                                    }
                                }
                            }
                        })
                }
                cursorAdapter.changeCursor(cursor)
                return true
            }
        })
        menuItem.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = menuItem.suggestionsAdapter.getItem(position) as Cursor
                val selection =
                    cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                menuItem.setQuery(selection, false)
                fetchData(selection)
                return false
            }

            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }
         */