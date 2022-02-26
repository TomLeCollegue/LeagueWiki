package com.androidcourse.leaguewiki.data

import com.androidcourse.leaguewiki.model.ChampionDetail
import com.androidcourse.leaguewiki.model.ChampionInfo
import com.androidcourse.leaguewiki.model.DataResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChampionsRepository @Inject constructor(
    private val championsDataSource: ChampionsDataSource,
    private val versionDatasource: VersionDatasource,
    private val localChampionsDatasource: LocalChampionsDatasource
) {

    private val synchroStateChampionList: MutableStateFlow<DataResult<Unit>> = MutableStateFlow(DataResult.Loading(null))
    val championsList: Flow<DataResult<List<ChampionInfo>?>>
        get() = combine(localChampionsDatasource.championInfoFlow, synchroStateChampionList) { list, state ->
            when {
                !list.isNullOrEmpty() -> DataResult.Success(list.sortedBy { it.name })
                list.isNullOrEmpty() && state is DataResult.Loading -> DataResult.Loading(null)
                else -> DataResult.Failure()
            }
        }

    private val synchroStateChampionDetail: MutableStateFlow<DataResult<Unit>> = MutableStateFlow(DataResult.Loading())
    fun championDetailById(id: String): Flow<DataResult<ChampionDetail?>> {
        return combine(localChampionsDatasource.championDetailByIdFlow(id), synchroStateChampionDetail) { champion, state ->
            when {
                champion != null -> DataResult.Success(champion)
                state is DataResult.Loading -> DataResult.Loading(null)
                else -> DataResult.Failure()
            }
        }
    }

    suspend fun fetchChampionDetail(champId: String) {
        synchroStateChampionDetail.value = DataResult.Loading()
        championsDataSource.getChampionDetail(champId, versionDatasource.getVersion()).let {
            it?.getChampionDetail()
        }?.let {
            localChampionsDatasource.insertChampionDetail(it)
        }
        delay(DELAY_STATE)
        synchroStateChampionDetail.value = DataResult.Success(Unit)
    }

    suspend fun setFavorite(idChamp: String, isFavorite: Boolean) {
        localChampionsDatasource.insertFavorite(idChamp, isFavorite)
    }

    suspend fun onAppForeground() {
        fetchNewData()
    }

    private suspend fun fetchNewData() {
        synchroStateChampionList.value = DataResult.Loading()
        val version = versionDatasource.fetchLastVersion()
        version?.let { versionDatasource.setNewVersion(it) }
        val listInfo = championsDataSource.getChampionList(version)?.data?.toList()?.map {
            ChampionInfo(
                it.second.id, it.second.name, it.second.title, it.second.version
            )
        }
        listInfo?.toTypedArray()?.let { insertNewChampionsInfo(*it) }
        delay(DELAY_STATE)
        synchroStateChampionList.value = DataResult.Success(Unit)
    }

    private suspend fun insertNewChampionsInfo(vararg listChampInfo: ChampionInfo) {
        localChampionsDatasource.insertChampionInfo(*listChampInfo)
    }

    companion object {
        const val DELAY_STATE: Long = 2000
    }

}