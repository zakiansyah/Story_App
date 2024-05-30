package com.dicoding.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.data.UserRepository
import com.dicoding.data.pref.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UploadViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getUpload(token: String, file: MultipartBody.Part, description: RequestBody) =
        repository.getUpload(token, file, description)


}