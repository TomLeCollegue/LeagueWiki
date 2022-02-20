package com.androidcourse.leaguewiki.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.androidcourse.leaguewiki.model.room.ChampionFavoriteRoom

@Dao
interface FavoriteChampionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg championFavoriteRoom: ChampionFavoriteRoom)
}