package com.example.testingproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.paging.ExperimentalPagingApi
import com.example.testingproject.databinding.CustomdialogBinding
import com.example.testingproject.mvvm.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalPagingApi
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