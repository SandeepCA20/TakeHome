package com.android.takehome.retrofit

import com.android.takehome.models.User
import com.android.takehome.models.UserRepos
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitApi {
    @GET("users/{username}")
    suspend fun getUser(@Path ("username") username: String): Response<User>

    @GET("users/{username}/repos")
    suspend fun getRepos(@Path ("username") username: String): Response<List<UserRepos>>
}