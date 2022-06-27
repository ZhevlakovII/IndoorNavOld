package com.example.indoornavigationar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.indoornavigationar.adapters.HistoryAdapter
import com.example.indoornavigationar.adapters.SearchedAdapter
import com.example.indoornavigationar.database.search.SearchItemsRepo
import com.example.indoornavigationar.databinding.SearchFragmentBinding
import com.example.indoornavigationar.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    @Inject
    lateinit var searchItemsRepo: SearchItemsRepo
    private val searchViewModel: SearchViewModel by viewModels()
    private val args: SearchFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SearchFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val historyAdapter = HistoryAdapter(searchItemsRepo, args.isOpenFromMap)
        val searchedAdapter = SearchedAdapter(searchItemsRepo, args.isOpenFromMap)

        binding.rvHistory.adapter = historyAdapter
        binding.rvSearch.adapter = searchedAdapter

        binding.rvHistory.visibility = View.VISIBLE
        binding.rvSearch.visibility = View.GONE

        searchViewModel.searchItems.observe(viewLifecycleOwner) { searchItems ->
            historyAdapter.submitList(searchItems)
        }

        binding.textInputLayout.setEndIconOnClickListener {
            val searchString: String = binding.textInputLayout.editText?.text.toString()
            searchViewModel.getSearchedLocations(searchString)

            binding.rvHistory.visibility = View.GONE
            binding.rvSearch.visibility = View.VISIBLE

            searchViewModel.searchedLocations.observe(viewLifecycleOwner) {
                searchNavLocation -> searchedAdapter.submitList(searchNavLocation)
            }
        }

        binding.backButton.setOnClickListener { view?.findNavController()?.navigateUp() }

        return binding.root
    }
}