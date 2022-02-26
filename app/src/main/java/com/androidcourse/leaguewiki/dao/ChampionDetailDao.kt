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
import kotlinx.coroutines.flow.Flow

@Dao
interface ChampionDetailDao {

    @Transaction
    @Query("SELECT * FROM championdetailroom WHERE id=:id")
    fun getById(id: String): Flow<DetailAndFavorite?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg championDetailRoom: ChampionDetailRoom)

}

data class DetailAndFavorite(
    @Embedded val championDetail: ChampionDetailRoom,
    @Relation(parentColumn = "id", entityColumn = "champId")
    val favorite: ChampionFavoriteRoom?
)