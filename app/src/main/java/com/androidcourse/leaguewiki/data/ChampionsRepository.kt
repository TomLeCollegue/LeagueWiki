package com.androidcourse.leaguewiki.data

import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.androidcourse.leaguewiki.model.ChampionDetail
import com.androidcourse.leaguewiki.model.ChampionInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChampionsRepository @Inject constructor(
    private val championsDataSource: ChampionsDataSource,
    private val favoriteDataStore: DataStoreManager
) {

    fun getChampionWithFavorite(): Flow<List<ChampionInfo>?> {
        return getChampionsList().combine(
            getFavorites()
        ) { champions, favorites ->
            Log.d("observe", "refresh repo")
            champions?.map { champion ->
                champion.isFavorite = favorites.firstOrNull { it.first == champion.id }?.second ?: false
                champion
            }
        }
    }


    private fun getChampionsList(): Flow<List<ChampionInfo>?> {
        return championsDataSource.getChampionList().map { apiChampionList ->
            apiChampionList?.data?.toList()?.map {
                ChampionInfo(
                    it.second.id, it.second.name, it.second.title, it.second.version
                )
            }
        }
    }

    fun getChampionDetail(champId: String): Flow<ChampionDetail?> {
        return championsDataSource.getChampionDetail(champId).map {
            it?.getChampionDetail()
        }
    }

    fun getFavorites(): Flow<List<Pair<String, Boolean>>> {
        return favoriteDataStore.dataStore.data.map { pref ->
            Log.d("observe", "getFav repo")
            pref.asMap().toList().map {
                Pair(it.first.name, it.second as Boolean)
            }
        }
    }

    suspend fun setFavorite(idChamp: String, isFavorite: Boolean) {
        val pref = booleanPreferencesKey(idChamp)
        favoriteDataStore.dataStore.edit {
            it[pref] = isFavorite
        }
    }

}