package com.androidcourse.leaguewiki.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androidcourse.leaguewiki.model.room.ChampionDetailRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface ChampionDetailDao {

    @Query("SELECT * FROM championdetailroom WHERE id=:id")
    fun getById(id: String): Flow<ChampionDetailRoom?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg championDetailRoom: ChampionDetailRoom)

}