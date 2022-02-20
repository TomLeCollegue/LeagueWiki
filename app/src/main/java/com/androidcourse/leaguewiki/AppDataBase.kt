package com.androidcourse.leaguewiki

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.androidcourse.leaguewiki.dao.ChampionDetailDao
import com.androidcourse.leaguewiki.dao.ChampionInfoDao
import com.androidcourse.leaguewiki.dao.FavoriteChampionDao
import com.androidcourse.leaguewiki.model.room.ChampionDetailRoom
import com.androidcourse.leaguewiki.model.room.ChampionFavoriteRoom
import com.androidcourse.leaguewiki.model.room.ChampionInfoRoom
import com.androidcourse.leaguewiki.model.room.SkinRoom
import com.androidcourse.leaguewiki.model.room.SpellRoom
import com.google.gson.Gson

@TypeConverters(Converters::class)
@Database(
    entities = [
        ChampionDetailRoom::class,
        ChampionInfoRoom::class,
        ChampionFavoriteRoom::class
    ],
    version = 1,
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun championInfoDao(): ChampionInfoDao
    abstract fun championDetailDao(): ChampionDetailDao
    abstract fun championFavoriteDao(): FavoriteChampionDao

    companion object {
        fun build(context: Context, name: String): AppDataBase =
            Room.databaseBuilder(context, AppDataBase::class.java, name).build()
    }
}

class Converters {

    @TypeConverter
    fun listStringToJson(value: List<String>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()

    @TypeConverter
    fun listSkinToJson(value: List<SkinRoom>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonSkinToList(value: String) = Gson().fromJson(value, Array<SkinRoom>::class.java).toList()

    @TypeConverter
    fun listSpellToJson(value: List<SpellRoom>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonSpellToList(value: String) = Gson().fromJson(value, Array<SpellRoom>::class.java).toList()
}
