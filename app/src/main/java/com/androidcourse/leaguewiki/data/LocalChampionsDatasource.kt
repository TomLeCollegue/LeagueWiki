package com.androidcourse.leaguewiki.data

import android.content.Context
import com.androidcourse.leaguewiki.AppDataBase
import com.androidcourse.leaguewiki.model.ChampionDetail
import com.androidcourse.leaguewiki.model.ChampionInfo
import com.androidcourse.leaguewiki.model.room.ChampionDetailRoom
import com.androidcourse.leaguewiki.model.room.ChampionInfoRoom
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalChampionsDatasource @Inject constructor(
    @ApplicationContext context: Context
) {

    private val db: AppDataBase = AppDataBase.build(context, "championinforoom")

    val championInfoFlow
        get() = db.championInfoDao().getAll().map {
            it.map { info ->
                info.toChampionInfo()
            }
        }

    fun championDetailByIdFlow(id: String): Flow<ChampionDetail?> {
        return db.championDetailDao().getById(id).map {
            it?.toChampionDetail()
        }
    }

    suspend fun insertChampionDetail(championDetail: ChampionDetail) {
        withContext(Dispatchers.IO) {
            db.championDetailDao().insertAll(ChampionDetailRoom.fromChampionDetail(championDetail))
        }
    }

    suspend fun insertChampionInfo(vararg championInfo: ChampionInfo) {
        withContext(Dispatchers.IO) {
            db.championInfoDao().insertAll(*championInfo.map {
                ChampionInfoRoom.fromChampionInfo(it)
            }.toTypedArray())
        }
    }

}