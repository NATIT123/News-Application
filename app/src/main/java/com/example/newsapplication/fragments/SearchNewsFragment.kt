package com.example.newsapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapplication.MainActivity
import com.example.newsapplication.Models.Article
import com.example.newsapplication.R
import com.example.newsapplication.Utils.Constants.Companion.DELAY_SEARCH
import com.example.newsapplication.ViewModel.NewsViewModel
import com.example.newsapplication.adapters.NewsAdapter
import com.example.newsapplication.databinding.FragmentSearchNewsBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(), NewsAdapter.onClickItem {

    private lateinit var binding: FragmentSearchNewsBinding
    private var listNewsSearch = listOf<Article>()
    private lateinit var mSearchNewsAdapter: NewsAdapter
    private lateinit var newsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsViewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onClick(position: Int) {
        val news = mSearchNewsAdapter.differ.currentList[position]
        val bundle = Bundle().apply {
            putSerializable("article", news)
        }
        findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment, bundle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSearchNewsAdapter = NewsAdapter(this)
        prepareRecyclerView()

        observerSearchNews()

        searchMeals()
    }


    private fun observerSearchNews() {
        newsViewModel.observerSearchNews().observe(viewLifecycleOwner) { it ->
            this.listNewsSearch = it
            mSearchNewsAdapter.differ.submitList(this.listNewsSearch)

        }
    }

    private fun prepareRecyclerView() {
        binding.rcvFragmentSearch.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mSearchNewsAdapter
        }
    }

    private fun searchMeals() {
        var searchJob: Job? = null
        binding.edtSearch.addTextChangedListener {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(DELAY_SEARCH)
                newsViewModel.searchNewsApi(it.toString())
            }
        }
    }

}