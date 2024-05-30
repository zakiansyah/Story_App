package com.dicoding.data

import androidx.lifecycle.liveData
import com.dicoding.data.pref.UserModel
import com.dicoding.data.pref.UserPreference
import com.dicoding.response.Story
import com.dicoding.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody



class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
    ){
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }
    suspend fun logout() {
        userPreference.logout()
    }

    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }
    fun signup(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }
    fun story(token: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStory("Bearer $token")
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }
    suspend fun detail(token: String, id: String): Flow<Result<Story>?> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getDetail("Bearer $token", id)
            val story = response.story
            emit(Result.Success(story))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }

    fun getUpload(token: String, file: MultipartBody.Part, description: RequestBody) = liveData {
        emit(Result.Loading)

        try {
            val successResponse = apiService.postStory("Bearer $token", file, description)
            emit(Result.Success(successResponse))
        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }
    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}