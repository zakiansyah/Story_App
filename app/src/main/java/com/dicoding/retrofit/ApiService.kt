package com.dicoding.retrofit

import com.dicoding.response.DetailResponse
import com.dicoding.response.FileUploadResponse
import com.dicoding.response.LoginResponse
import com.dicoding.response.RegisterResponse
import com.dicoding.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): RegisterResponse

    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") auth: String
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getDetail(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): DetailResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): FileUploadResponse
}