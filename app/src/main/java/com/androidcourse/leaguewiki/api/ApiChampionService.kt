package com.androidcourse.leaguewiki.api

import com.androidcourse.leaguewiki.model.ApiModelChampionDetail
import com.androidcourse.leaguewiki.model.ApiModelChampionList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiChampionService {

    @GET("cdn/{version}/data/{language}/champion.json")
    suspend fun getChampions(
        @Path("language") language: String,
        @Path("version") version: String = "12.3.1"
    ): Response<ApiModelChampionList>

    @GET("cdn/{version}/data/{language}/champion/{champId}.json")
    suspend fun getDetailChamp(
        @Path("language") language: String,
        @Path("champId") champId: String,
        @Path("version") version: String = "12.3.1"
    ): Response<ApiModelChampionDetail>

}