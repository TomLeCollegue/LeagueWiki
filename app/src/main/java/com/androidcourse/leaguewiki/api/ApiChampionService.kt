package com.androidcourse.leaguewiki.api

import com.androidcourse.leaguewiki.model.ApiModelChampionDetail
import com.androidcourse.leaguewiki.model.ApiModelChampionList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiChampionService {

    @GET("12.3.1/data/en_US/champion.json")
    suspend fun getChampions(): Response<ApiModelChampionList>

    @GET("12.3.1/data/en_US/champion/{champId}.json")
    suspend fun getDetailChamp(
        @Path("champId") champId: String
    ): Response<ApiModelChampionDetail>

}