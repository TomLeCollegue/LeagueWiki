package com.androidcourse.leaguewiki.data

import android.content.Context
import android.util.Log
import com.androidcourse.leaguewiki.Constants
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.api.ApiChampionService
import com.androidcourse.leaguewiki.model.ApiModelChampionDetail
import com.androidcourse.leaguewiki.model.ApiModelChampionList
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChampionsDataSource @Inject constructor(
    retrofitClient: RetrofitClient,
    @ApplicationContext val context: Context
) {

    private val apiChampionService = retrofitClient.retrofit.create(ApiChampionService::class.java)

    suspend fun getChampionList(version: String?): ApiModelChampionList? {
        try {
            val result = apiChampionService.getChampions(context.getString(R.string.api_language), version ?: Constants.Config.DEFAULT_VERSION)
            if (result.isSuccessful) {
               return result.body()
            } else {
                Log.d("Log", "error fetching champs")
            }
        } catch (e: Exception) {
            Log.d("Log", "error fetching champs")
        }
        return null
    }

    suspend fun getChampionDetail(champId: String, version: String): ApiModelChampionDetail? {
        try {
            val result = apiChampionService.getDetailChamp(context.getString(R.string.api_language), champId, version)
            if (result.isSuccessful) {
                return result.body()
            } else {
                Log.d("Log", "error fetching champs")
            }
        } catch (e: Exception) {
            Log.d("Log", "error fetching champs")
        }
        return null
    }
}