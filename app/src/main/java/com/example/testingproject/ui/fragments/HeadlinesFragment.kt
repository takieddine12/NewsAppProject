package com.example.testingproject.ui.fragments

import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.graphics.Color
import android.os.Bundle
import android.provider.BaseColumns
import android.speech.RecognizerIntent
import android.view.*
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import com.barisatalay.filterdialog.FilterDialog
import com.barisatalay.filterdialog.model.DialogListener
import com.example.testingproject.*
import com.example.testingproject.recyclers.HeadLinesAdapter
import com.example.testingproject.mvvm.MainViewModel
import com.example.testingproject.databinding.TopheadlinesLayoutBinding
import com.example.testingproject.ui.FavNewsActivity
import com.example.testingproject.models.SuggestionsModel
import com.example.testingproject.mvvm.SharedViewModel
import com.example.testingproject.ui.NewsDetailsActivity
import com.example.testingproject.newslistener.HeadlinesOnListener
import com.example.testingproject.newsmodels.Article
import com.example.testingproject.newsmodels.HeadlinesModel
import com.example.testingproject.ui.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
@ExperimentalPagingApi
class HeadlinesFragment : Fragment() {
     private  var selectedLanguage = "us"
     private lateinit var sharedViewModel: SharedViewModel
     private lateinit var headlinesAdapter: HeadLinesAdapter
     private val mainViewModel: MainViewModel by viewModels()
     lateinit var binding : TopheadlinesLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TopheadlinesLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headlinesRecycler.layoutManager = GridLayoutManager(requireContext(),2)
        binding.headlinesRecycler.setHasFixedSize(true)

        sharedViewModel = ViewModelProviders.of(this)[SharedViewModel::class.java]
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
        if(Utils.checkConnectivity(requireContext())){
            fetchData(selectedLanguage)
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
        sharedViewModel.getQuery().observe(viewLifecycleOwner, Observer {
             selectedLanguage = it
             fetchData(selectedLanguage)
        })
    }

    private fun fetchData(selectedLanguage : String) {
        lifecycleScope.launchWhenStarted {
               mainViewModel.getHeadLines(selectedLanguage,Utils.API_KEY).collect {
                   headlinesAdapter.submitData(pagingData = it)
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