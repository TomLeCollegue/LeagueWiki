package com.androidcourse.leaguewiki.data

import android.util.Log
import com.androidcourse.leaguewiki.api.ApiChampionService
import com.androidcourse.leaguewiki.model.ApiModelChampionList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChampionsDataSource(
    retrofitClient: RetrofitClient
) {

    private val apiChampionService = retrofitClient.retrofit.create(ApiChampionService::class.java)

    fun getChampionList(): Flow<ApiModelChampionList?> {
        return flow {
            try {
                val result = apiChampionService.getChampions()
                if (result.isSuccessful) {
                    emit(result.body())
                } else {
                    Log.d("Log", "error fetching champs")
                }
            } catch (e: Exception) {
                Log.d("Log", "error fetching champs")
            }
        }
    }
}