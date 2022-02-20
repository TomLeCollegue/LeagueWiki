package com.androidcourse.leaguewiki.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.androidcourse.leaguewiki.model.room.ChampionDetailRoom
import com.androidcourse.leaguewiki.model.room.ChampionFavoriteRoom
import com.androidcourse.leaguewiki.model.room.ChampionInfoRoom
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Dao
interface ChampionInfoDao {

    @Transaction
    @Query("SELECT * FROM championinforoom")
    fun getAll(): Flow<List<InfoAndFavorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg championInfoRoom: ChampionInfoRoom)

}

data class InfoAndFavorite(
    @Embedded val championDetail: ChampionInfoRoom,
    @Relation(parentColumn = "id", entityColumn = "champId")
    val favorite: ChampionFavoriteRoom?
)