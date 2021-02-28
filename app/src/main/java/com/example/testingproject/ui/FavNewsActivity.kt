package com.example.testingproject.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testingproject.*
import com.example.testingproject.recyclers.FavNewsAdapter
import com.example.testingproject.mvvm.MainViewModel
import com.example.testingproject.databinding.ActivityFavNewsBinding
import com.example.testingproject.models.FavNewsModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_fav_news.*
import java.util.*

@AndroidEntryPoint
class FavNewsActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel? by viewModels()
    private var list: List<FavNewsModel>? = null
    private var adapter: FavNewsAdapter? = null

    lateinit var binding : ActivityFavNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.favToolbar)
        supportActionBar?.setHomeButtonEnabled(true)

        binding.favRecycler.layoutManager = LinearLayoutManager(this)
        binding.favRecycler.setHasFixedSize(true)

        mainViewModel!!.getLiveData().observe(this, Observer {
            list = it
            adapter = FavNewsAdapter(this, list!!)
            binding.favRecycler.adapter = adapter
        })
        deleteOnSwipe()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favmenu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteall -> {
                if(adapter!!.itemCount == 0) {
                    this@FavNewsActivity.showWarningToast(getString(R.string.nodataddedyet))
                }else {
                    AlertDialog.Builder(this)
                        .setTitle(getString(R.string.deletenews))
                        .setMessage(getString(R.string.areyousureyouwanttodeleteall))
                        .setPositiveButton(getString(R.string.yes)){ dialog, which ->
                            mainViewModel!!.deleteAllNews()
                            this@FavNewsActivity.showToast(getString(R.string.allarticlesdeleted))
                        }
                        .setNegativeButton(getString(R.string.no)){ dialog, which ->
                            dialog.dismiss()
                        }.create().show()
                }
            }
            android.R.id.home -> {
                Intent(this, MainActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return true
    }
    private fun deleteOnSwipe() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT,ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mainViewModel!!.deletePerNews(list!![viewHolder.adapterPosition])
                this@FavNewsActivity.showToast(getString(R.string.articlessuccessfullydeleted))
            }
        }
        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(binding.favRecycler)
        }
    }
}
