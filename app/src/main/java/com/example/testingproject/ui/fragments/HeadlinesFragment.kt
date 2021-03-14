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
import androidx.recyclerview.widget.RecyclerView
import com.example.testingproject.Utils
import com.example.testingproject.databinding.TopheadlinesLayoutBinding
import com.example.testingproject.models.EventBusModel
import com.example.testingproject.mvvm.MainViewModel
import com.example.testingproject.newslistener.HeadlinesOnListener
import com.example.testingproject.newsmodels.Article
import com.example.testingproject.recyclers.HeadLinesAdapter
import com.example.testingproject.recyclers.states.NewsStateAdapter
import com.example.testingproject.ui.NewsDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber


@AndroidEntryPoint
@ExperimentalPagingApi
class HeadlinesFragment : Fragment() {


     private lateinit var headlinesAdapter: HeadLinesAdapter
     private val mainViewModel: MainViewModel by viewModels()
     lateinit var binding : TopheadlinesLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TopheadlinesLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.headlinesRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            setHasFixedSize(true)
            headlinesAdapter = HeadLinesAdapter(object : HeadlinesOnListener {
                override fun headlinesOnClick(headlinesModel: Article) {
                    Intent(requireContext(), NewsDetailsActivity::class.java).apply {
                        putExtra("author", headlinesModel.author)
                        putExtra("title", headlinesModel.title)
                        putExtra("description", headlinesModel.description)
                        putExtra("imgUrl", headlinesModel.urlToImage)
                        putExtra("date", headlinesModel.publishedAt)
                        startActivity(this)
                    }

                }
            })
            binding.headlinesRecycler.adapter = headlinesAdapter.withLoadStateFooter(
                footer = NewsStateAdapter {headlinesAdapter.retry()})

        }

        if(Utils.checkConnectivity(requireContext())){
            fetchData("us")
        } else {
            lifecycleScope.launchWhenStarted {
                mainViewModel.getOfflineHeadlines().collect {
                    headlinesAdapter.submitData(it)
                }
            }
            binding.headlinesRecycler.adapter = headlinesAdapter
            if(headlinesAdapter.itemCount == 0){
                binding.noNews.visibility = View.VISIBLE
            } else {
                binding.noNews.visibility = View.INVISIBLE
            }

        }

    }
    private fun fetchData(selectedLanguage : String) {

        lifecycleScope.launch {
            mainViewModel.getHeadLines(selectedLanguage,Utils.API_KEY).collect {
                headlinesAdapter.submitData(pagingData = it)
            }
        }

        // TODO : First Method Checking Adapter count
        binding.headlinesRecycler.adapter = headlinesAdapter
        if(headlinesAdapter.itemCount == -1){
            binding.noNews.visibility = View.VISIBLE
        } else {
            binding.noNews.visibility = View.INVISIBLE
        }

        // TODO : Second Method setting recycler with observer and check for itemcount
//        headlinesAdapter.registerAdapterDataObserver(object :RecyclerView.AdapterDataObserver(){
//            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
//                super.onItemRangeInserted(positionStart, itemCount)
//                val adapterCount = headlinesAdapter.itemCount
//                if(adapterCount == 0 &&  itemCount == 0){
//                    // list is  empty
//                } else {
//                    // list is not empty
//                }
//            }
//        })

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
  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.language -> {
                showFilterDialog()
            }
            R.id.headlinesVoice -> {
                voiceSearch()
            }
        }
        return false
    }
 */







/*
 val menuItem = menu.findItem(R.id.headlinesSearchView)?.actionView as SearchView
        menuItem.findViewById<ImageView>(androidx.appcompat.R.id.search_button).apply {
            setImageDrawable(ContextCompat.getDrawable(requireContext(),
            R.drawable.ic_search_black_24dp))
        }
        menuItem.findViewById<SearchView.SearchAutoComplete>(androidx.appcompat.R.id.search_src_text).apply {
            setHintTextColor(Color.WHITE)
            setTextColor(Color.WHITE)
        }
        menuItem.queryHint = "search"
        menuItem.findViewById<AutoCompleteTextView>(R.id.search_src_text).threshold = 1
        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.item_label)
        val cursorAdapter = SimpleCursorAdapter(requireContext(), R.layout.suggestions_layout, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
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

                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                CoroutineScope(Dispatchers.IO).launch {
                    mainViewModel.getSuggestions().observe(viewLifecycleOwner, androidx.lifecycle.Observer { list ->
                        query.let {
                            list.forEachIndexed { index, suggestionsModel ->
                                if(suggestionsModel.suggestion?.contains(it!!,true)!!){
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
                val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                menuItem.setQuery(selection, false)
                fetchData(selection)
                return false
            }

            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }
        })
 */