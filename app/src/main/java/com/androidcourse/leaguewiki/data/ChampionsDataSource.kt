package com.androidcourse.leaguewiki.data

import android.content.Context
import android.util.Log
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.api.ApiChampionService
import com.androidcourse.leaguewiki.model.ApiModelChampionDetail
import com.androidcourse.leaguewiki.model.ApiModelChampionList
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.security.AccessControlContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChampionsDataSource @Inject constructor(
    retrofitClient: RetrofitClient,
    @ApplicationContext val context: Context
) {

    private val apiChampionService = retrofitClient.retrofit.create(ApiChampionService::class.java)

    fun getChampionList(): Flow<ApiModelChampionList?> {
        return flow {
            try {
                val result = apiChampionService.getChampions(context.getString(R.string.api_language))
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

    fun getChampionDetail(champId: String): Flow<ApiModelChampionDetail?> {
        return flow {
            try {
                val result = apiChampionService.getDetailChamp(context.getString(R.string.api_language),champId)
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