package com.example.newsapplication.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapplication.Database.ArticleDatabase

class NewsViewModelFactory(private val articleDatabase: ArticleDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(articleDatabase) as T
    }
}