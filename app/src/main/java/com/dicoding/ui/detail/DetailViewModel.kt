package com.dicoding.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.data.UserRepository
import com.dicoding.response.Story
import kotlinx.coroutines.launch
import com.dicoding.data.Result

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    private val _detailResult = MutableLiveData<Result<Story>>()
    val detailResult: LiveData<Result<Story>> = _detailResult

    fun getSession(){
        repository.getSession().asLiveData()
    }
    fun getDetail(token: String, id: String) {
        viewModelScope.launch {
            repository.detail(token, id).collect{ result ->
                _detailResult.value = result
            }
        }
    }
}