package com.androidcourse.leaguewiki.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChampionFavoriteRoom(
    @PrimaryKey val champId: String,
    val isFavorite: Boolean
)