package com.androidcourse.leaguewiki.data

import android.util.Log
import com.androidcourse.leaguewiki.model.ChampionDetail
import com.androidcourse.leaguewiki.model.ChampionInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChampionsRepository @Inject constructor(
    private val championsDataSource: ChampionsDataSource,
    private val versionDatasource: VersionDatasource,
    private val localChampionsDatasource: LocalChampionsDatasource
) {

    val championsList: Flow<List<ChampionInfo>?> get() = localChampionsDatasource.championInfoFlow

    fun championDetailById(id: String): Flow<ChampionDetail?> {
        return localChampionsDatasource.championDetailByIdFlow(id)
    }

    suspend fun fetchChampionDetail(champId: String) {
        championsDataSource.getChampionDetail(champId, versionDatasource.getVersion()).let {
            it?.getChampionDetail()
        }?.let {
            localChampionsDatasource.insertChampionDetail(it)
        }
    }

    suspend fun setFavorite(idChamp: String, isFavorite: Boolean) {
        localChampionsDatasource.insertFavorite(idChamp, isFavorite)
    }

    suspend fun onAppForeground() {
        fetchNewData()
    }

    private suspend fun fetchNewData() {
        val version = versionDatasource.fetchLastVersion()
        version?.let { versionDatasource.setNewVersion(it) }
        val listInfo = championsDataSource.getChampionList(version)?.data?.toList()?.map {
            ChampionInfo(
                it.second.id, it.second.name, it.second.title, it.second.version
            )
        }
        listInfo?.toTypedArray()?.let { insertNewChampionsInfo(*it) }
    }

    private suspend fun insertNewChampionsInfo(vararg listChampInfo: ChampionInfo) {
        localChampionsDatasource.insertChampionInfo(*listChampInfo)
    }
}