package com.example.newsapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.MainActivity
import com.example.newsapplication.Models.Article
import com.example.newsapplication.R
import com.example.newsapplication.ViewModel.NewsViewModel
import com.example.newsapplication.adapters.NewsAdapter
import com.example.newsapplication.databinding.FragmentSavedNewsBinding
import com.google.android.material.snackbar.Snackbar


class SavedNewsFragment : Fragment(), NewsAdapter.onClickItem {

    private lateinit var binding: FragmentSavedNewsBinding
    private lateinit var mSavedNewsAdapter: NewsAdapter
    private var listSavedNews = mutableListOf<Article>()
    private lateinit var newsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsViewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSavedNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSavedNewsAdapter = NewsAdapter(this)
        prepareRecyclerView()
        observerArticleSaved()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = mSavedNewsAdapter.differ.currentList[position]
                newsViewModel.deleteNews(article)
                Snackbar.make(view, "Successfully delete Article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        newsViewModel.addNews(article)
                    }
                    show()
                }
            }

        }

        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(binding.rcvFragmentSavedNews)
        }
    }

    private fun prepareRecyclerView() {
        binding.rcvFragmentSavedNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mSavedNewsAdapter
        }
    }

    private fun observerArticleSaved() {
        newsViewModel.observerArticlesSaved().observe(viewLifecycleOwner) { it ->
            this.listSavedNews = it.toMutableList()
            mSavedNewsAdapter.differ.submitList(this.listSavedNews)
        }
    }

    override fun onClick(position: Int) {
        val news = mSavedNewsAdapter.differ.currentList[position]
        val bundle = Bundle().apply {
            putSerializable("article", news)
        }
        findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment, bundle)
    }


}