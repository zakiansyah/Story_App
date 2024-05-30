package com.dicoding.di

import android.content.Context
import com.dicoding.data.UserRepository
import com.dicoding.data.pref.UserPreference
import com.dicoding.data.pref.dataStore
import com.dicoding.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, pref)
    }
}