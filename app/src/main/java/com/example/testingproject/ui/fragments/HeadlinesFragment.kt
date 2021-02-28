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
import com.example.testingproject.ui.NewsDetailsActivity
import com.example.testingproject.newslistener.HeadlinesOnListener
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

        headlinesAdapter = HeadLinesAdapter(object : HeadlinesOnListener {
                override fun headlinesOnClick(headlinesModel: HeadlinesModel,position : Int) {
                        Intent(requireContext(), NewsDetailsActivity::class.java).apply {
                            putExtra("author",headlinesModel.articles[position].author)
                            putExtra("title",headlinesModel.articles[position].title)
                            putExtra("description",headlinesModel.articles[position].description)
                            putExtra("imgUrl",headlinesModel.articles[position].urlToImage)
                            putExtra("date",headlinesModel.articles[position].publishedAt)
                            binding.fabMenu.collapse()
                            startActivity(this)
                    }

                }
            })
        if(Utils.checkConnectivity(requireContext())){
            fetchData()
        } else {
            lifecycleScope.launchWhenStarted {
                mainViewModel.getOfflineHeadlines().collect {
                    binding.headlinesNoPost.visibility = View.INVISIBLE
                    headlinesAdapter.submitData(it)
                   binding.headlinesRecycler.adapter = headlinesAdapter
                }
            }


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

    private fun fetchData() {
           lifecycleScope.launchWhenStarted {
               mainViewModel.getHeadLines("us",Utils.API_KEY).collect { pagedList ->
                   binding.headlinesNoPost.visibility = View.INVISIBLE
                   headlinesAdapter.submitData(pagedList)
                   binding.headlinesRecycler.adapter = headlinesAdapter
                   }
           }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.filter_menu,menu)
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
                ///fetchData(selection)
                return false
            }

            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }
        })
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
        return true
    }

    private fun showFilterDialog() {

        FilterDialog(activity).apply {
            searchBoxHint = "Search"
            toolbarTitle  = "Countries"
            setList(countriesList())

            show("code","name",
                DialogListener.Single { selectedItem ->
                    lifecycleScope.launch {
                        mainViewModel.getHeadLines(selectedItem!!.name, Utils.API_KEY).collect {
                            binding.headlinesNoPost.visibility = View.INVISIBLE
                            headlinesAdapter.submitData(it)
                            binding.headlinesRecycler.adapter = headlinesAdapter
                        }
                    }
                })
        }
    }
    private fun countriesList() : List<String> {
       arrayListOf<String>().apply {
           add("ae")
           add("ar")
           add("at")
           add("au")
           add("be")
           add("bg")
           add("br")
           add("ca")
           add("ch")
           add("cn")
           add("co")
           add("cu")
           add("cz")
           add("de")
           add("en")
           return this
       }
    }

    private fun fetchData(query: String) {

        lifecycleScope.launchWhenStarted {
            mainViewModel.getHeadLines(query,Utils.API_KEY).collect { pagedList ->
                binding.headlinesNoPost.visibility = View.INVISIBLE
                headlinesAdapter.submitData(pagedList)
                binding.headlinesRecycler.adapter = headlinesAdapter
            }
        }
    }
    private fun voiceSearch() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pass a search")
        startActivityForResult(intent, SEARCH_REQUEST_CODE)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SEARCH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
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