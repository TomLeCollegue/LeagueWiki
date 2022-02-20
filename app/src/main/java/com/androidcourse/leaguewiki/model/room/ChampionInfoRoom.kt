package com.androidcourse.leaguewiki.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.androidcourse.leaguewiki.model.ChampionInfo

@Entity
data class ChampionInfoRoom (
    @PrimaryKey val id: String,
    val name: String?,
    val title: String?,
    val version: String?,
    var isFavorite: Boolean = false
) {
    fun toChampionInfo() = ChampionInfo(
        id, name, title, version, isFavorite
    )
    companion object {
        fun fromChampionInfo(championInfo: ChampionInfo) = ChampionInfoRoom(
            championInfo.id,
            championInfo.name,
            championInfo.title,
            championInfo.version,
            championInfo.isFavorite
        )
    }
}