package com.androidcourse.leaguewiki.model

data class ChampionInfo (
    val id: String?,
    val name: String?,
    val title: String?,
    val version: String?,
    var isFavorite: Boolean = false
)