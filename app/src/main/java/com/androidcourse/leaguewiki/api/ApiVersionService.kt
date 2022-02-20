package com.androidcourse.leaguewiki.api

import retrofit2.Response
import retrofit2.http.GET

interface ApiVersionService {

    @GET("api/versions.json")
    suspend fun getVersions(): Response<List<String>>

}