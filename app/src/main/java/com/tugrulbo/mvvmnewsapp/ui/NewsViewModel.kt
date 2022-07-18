package com.tugrulbo.mvvmnewsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.tugrulbo.mvvmnewsapp.models.NewsResponse
import com.tugrulbo.mvvmnewsapp.repository.NewsRepository
import com.tugrulbo.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private val newsRepository: NewsRepository
):ViewModel() {


    val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val breakingNewsPage = 1

    init {
        getBreakingNews("tr")
    }

    fun getBreakingNews(countryCode:String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
        breakingNews.postValue(handlingBreakingNewsResponse(response))
    }

    private fun handlingBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}