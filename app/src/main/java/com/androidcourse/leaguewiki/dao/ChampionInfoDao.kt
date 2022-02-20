package com.androidcourse.leaguewiki.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androidcourse.leaguewiki.model.room.ChampionInfoRoom
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Dao
interface ChampionInfoDao {

    @Query("SELECT * FROM championinforoom")
    fun getAll(): Flow<List<ChampionInfoRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg championInfoRoom: ChampionInfoRoom)

}