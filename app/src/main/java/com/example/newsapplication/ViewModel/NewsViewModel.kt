package com.example.newsapplication.ViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapplication.Database.ArticleDatabase
import com.example.newsapplication.Models.Article
import com.example.newsapplication.Models.NewsResponse
import com.example.newsapplication.Retrofit.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel(private val articleDatabase: ArticleDatabase) : ViewModel() {

    private var articlesLiveData = MutableLiveData<List<Article>>()
    private var articlesSearchLiveData = MutableLiveData<List<Article>>()
    private var articleSavedLiveData = articleDatabase.articleDao().getListArticle()


    fun getBreakingNewsApi(page: Int) {
        ApiService.apiService.getBreakingNews(page = page).enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        articlesLiveData.value = body.articles
                    }
                }
            }

            override fun onFailure(p0: Call<NewsResponse>, p1: Throwable) {

            }

        })
    }

    fun searchNewsApi(searchKey: String) {
        ApiService.apiService.searchEverything(searchKey).enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        articlesSearchLiveData.postValue(body.articles)
                    }
                }
            }

            override fun onFailure(p0: Call<NewsResponse>, p1: Throwable) {

            }

        })
    }

    fun addNews(article: Article) {
        viewModelScope.launch {
            articleDatabase.articleDao().upsert(article)
        }
    }

    fun deleteNews(article: Article) {
        viewModelScope.launch {
            articleDatabase.articleDao().deleteArticle(article)
        }
    }

    fun observerBreakingNews(): MutableLiveData<List<Article>> {
        return articlesLiveData
    }

    fun observerSearchNews(): MutableLiveData<List<Article>> {
        return articlesSearchLiveData
    }

    fun observerArticlesSaved(): LiveData<List<Article>> {
        return articleSavedLiveData
    }
}