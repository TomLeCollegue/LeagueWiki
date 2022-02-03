package com.androidcourse.leaguewiki.data

import com.androidcourse.leaguewiki.model.ChampionInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChampionsRepository(
    private val championsDataSource: ChampionsDataSource,
) {

    fun getChampionsList(): Flow<List<ChampionInfo>?> {
        return championsDataSource.getChampionList().map { apiChampionList ->
            apiChampionList?.data?.toList()?.map {
                ChampionInfo(
                    it.second.id,
                    it.second.name,
                    it.second.title,
                    it.second.version
                )
            }
        }
    }


}