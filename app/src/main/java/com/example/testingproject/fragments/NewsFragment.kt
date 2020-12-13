package com.example.testingproject.fragments

import android.app.Activity.RESULT_OK
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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testingproject.*
import com.example.testingproject.databinding.AllnewsLayoutBinding
import com.example.testingproject.favnews.FavNewsActivity
import com.example.testingproject.models.SuggestionsModel
import com.example.testingproject.newsdetails.NewsDetailsActivity
import com.example.testingproject.newslistener.NewsOnListener
import com.example.testingproject.newsmodels.NewsModel
import com.example.testingproject.recyclers.NewsAdapter
import com.example.testingproject.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*


@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var newsAdapter: NewsAdapter
    private  val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding : AllnewsLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AllnewsLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.newsRecycler.setHasFixedSize(true)

        newsAdapter = NewsAdapter(object : NewsOnListener {
            override fun onNewsClicked(allNewsModel: NewsModel, position: Int) {
                Intent(requireContext(), NewsDetailsActivity::class.java).apply {
                    putExtra("author", allNewsModel.articles[position].author)
                    putExtra("title", allNewsModel.articles[position].title)
                    putExtra("description", allNewsModel.articles[position].description)
                    putExtra("imgUrl", allNewsModel.articles[position].urlToImage)
                    putExtra("date", allNewsModel.articles[position].publishedAt)
                    binding.fabMenu.collapse()
                    startActivity(this)
                }
            }
        })

        if(Utils.checkConnectivity(requireContext())) {
            fetchData("italy")
        } else {
        //     mainViewModel.observeChanges().observe(viewLifecycleOwner, Observer {
//                 if(!it.isNullOrEmpty()){
//                     binding.newsNoPost.visibility = View.INVISIBLE
//                     newsAdapter.submitList(it)
//                     binding.newsRecycler.adapter = newsAdapter
//                 } else {
//                     binding.newsNoPost.visibility = View.VISIBLE
//                 }
//                    requireContext().showErrorToast("Error Retreiving Data...")
                //   })
                //   }
        }
        binding.deleteMenu.setOnClickListener {
            BottomDialog().apply {
                show(requireFragmentManager(), "Suggestions Deletion")
                binding.fabMenu.collapse()
            }
        }
        binding.settingsMenu.setOnClickListener {
            Intent(requireContext(), SettingsActivity::class.java).apply {
                startActivity(this)
            }
            binding.fabMenu.collapse()
        }
        binding.favNews.setOnClickListener {
            Intent(requireContext(), FavNewsActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(this)
            }
        }
    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
//    }
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.menu1, menu)
//        val menuItem = menu.findItem(R.id.newsSearchView)?.actionView as SearchView
//        menuItem.findViewById<ImageView>(androidx.appcompat.R.id.search_button).apply {
//            setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_search_black_24dp))
//        }
//        menuItem.findViewById<SearchView.SearchAutoComplete>(androidx.appcompat
//            .R.id.search_src_text).apply {
//                setHintTextColor(Color.WHITE)
//                setTextColor(Color.WHITE)
//        }
//        menuItem.queryHint = "search"
//        menuItem.findViewById<AutoCompleteTextView>(R.id.search_src_text).threshold = 1
//        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
//        val to = intArrayOf(R.id.item_label)
//        val cursorAdapter = SimpleCursorAdapter(
//            requireContext(),
//            R.layout.suggestions_layout,
//            null,
//            from,
//            to,
//            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
//        )
//        menuItem.suggestionsAdapter = cursorAdapter
//        menuItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                fetchData(query!!)
//                CoroutineScope(Dispatchers.IO).launch {
//                    mainViewModel.insertSuggestions(SuggestionsModel(query))
//                }
//
//                val cursor = MatrixCursor(
//                    arrayOf(
//                        BaseColumns._ID,
//                        SearchManager.SUGGEST_COLUMN_TEXT_1
//                    )
//                )
//                CoroutineScope(Dispatchers.IO).launch {
//                    mainViewModel.getSuggestions().observe(
//                        viewLifecycleOwner,
//                        androidx.lifecycle.Observer { list ->
//                            query.let {
//                                list.forEachIndexed { index, suggestionsModel ->
//                                    if (suggestionsModel.suggestion?.contains(it!!, true)!!) {
//                                        cursor.addRow(arrayOf(index, suggestionsModel.suggestion))
//                                    }
//                                }
//                            }
//                        })
//                }
//                cursorAdapter.changeCursor(cursor)
//                return true
//            }
//        })
//        menuItem.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
//            override fun onSuggestionClick(position: Int): Boolean {
//                val cursor = menuItem.suggestionsAdapter.getItem(position) as Cursor
//                val selection =
//                    cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
//                menuItem.setQuery(selection, false)
//                fetchData(selection)
//                return false
//            }
//
//            override fun onSuggestionSelect(position: Int): Boolean {
//                return false
//            }
//        })
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.newsVoice -> {
//                voiceSearch()
//            }
//        }
//        return true
//    }
    private fun fetchData(query: String) {
        mainViewModel.getAll(query, Utils.API_KEY, 1).observe(
            viewLifecycleOwner,
            Observer { pagedList ->
                if (pagedList != null) {
                    binding.newsNoPost.visibility = View.INVISIBLE
                    newsAdapter.submitList(pagedList)
                    binding.newsRecycler.adapter = newsAdapter
                } else {
                    binding.newsNoPost.visibility = View.VISIBLE
                }
            })
    }
    private fun voiceSearch() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pass a search")
        startActivityForResult(intent, SEARCH_REQUEST_CODE)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SEARCH_REQUEST_CODE && resultCode == RESULT_OK) {
            val voiceList = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            for (i in 0 until voiceList!!.size) {
                fetchData(voiceList[0].toLowerCase(Locale.ROOT))
            }
        }
    }
    companion object{
        const val SEARCH_REQUEST_CODE = 1001
    }

}


