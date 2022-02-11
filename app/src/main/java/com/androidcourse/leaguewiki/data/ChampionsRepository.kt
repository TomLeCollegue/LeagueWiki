package com.androidcourse.leaguewiki.data

import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.androidcourse.leaguewiki.model.ChampionDetail
import com.androidcourse.leaguewiki.model.ChampionInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChampionsRepository @Inject constructor(
    private val championsDataSource: ChampionsDataSource,
    private val favoriteDataStore: DataStoreManager
) {

    private val championsList: MutableStateFlow<List<ChampionInfo>?> = MutableStateFlow(null)

    val favorites: Flow<List<Pair<String, Boolean>>> =
        favoriteDataStore.dataStore.data.map { pref ->
            pref.asMap().toList().map {
                Pair(it.first.name, it.second as Boolean)
            }
        }

    val championsWithFavorites = championsList.combine(
        favorites
    ) { champions, favorites ->
        Log.d("observe", "refresh repo")
        champions?.map { champion ->
            champion.isFavorite = favorites.firstOrNull { it.first == champion.id }?.second ?: false
            champion
        }
    }


    suspend fun getChampionsList() {
        championsDataSource.getChampionList().collect { apiChampionList ->
            championsList.value = apiChampionList?.data?.toList()?.map {
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

    suspend fun setFavorite(idChamp: String, isFavorite: Boolean) {
        val pref = booleanPreferencesKey(idChamp)
        favoriteDataStore.dataStore.edit {
            it[pref] = isFavorite
        }
    }

}