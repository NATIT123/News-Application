package com.example.newsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.newsapplication.Database.ArticleDatabase
import com.example.newsapplication.ViewModel.NewsViewModel
import com.example.newsapplication.ViewModel.NewsViewModelFactory
import com.example.newsapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    val viewModel: NewsViewModel by lazy {
        val newsDatabase = ArticleDatabase.getInstance(this)
        val newsViewModelProviderFactory = NewsViewModelFactory(newsDatabase)
        ViewModelProvider(this, newsViewModelProviderFactory)[NewsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_frame) as NavHostFragment

        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNav, navController)


    }
}