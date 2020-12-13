package com.example.testingproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import com.example.testingproject.databinding.CustomdialogBinding
import com.example.testingproject.room.NewsDatabase
import com.example.testingproject.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class BottomDialog() : BottomSheetDialogFragment() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : CustomdialogBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = CustomdialogBinding.inflate(inflater)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getSuggestions().observe(viewLifecycleOwner, Observer {
            it?.let {
                requireContext().showWarningToast("No Suggestions To Delete")
                dismiss()
            }
            //NewsDatabase.invoke(context!!).suggestionsDao().deleteSuggestion()
            dismiss()
            requireContext().showToast("Suggestions Erased..")
        })
        binding.yes.setOnClickListener {

        }
        binding.no.setOnClickListener {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }
}