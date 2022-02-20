package com.androidcourse.leaguewiki.model.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.androidcourse.leaguewiki.model.ChampionDetail
import com.androidcourse.leaguewiki.model.Info
import com.androidcourse.leaguewiki.model.Leveltip
import com.androidcourse.leaguewiki.model.Passive
import com.androidcourse.leaguewiki.model.Skin
import com.androidcourse.leaguewiki.model.Spell
import com.androidcourse.leaguewiki.model.Stats

@Entity
data class ChampionDetailRoom(
    val allytips: List<String>,
    val blurb: String,
    val enemytips: List<String>,
    @PrimaryKey val id: String,
    @Embedded val info: InfoRoom,
    val key: String,
    val lore: String,
    val name: String,
    val partype: String,
    @Embedded val passive: PassiveRoom,
    val skins: List<SkinRoom>,
    val tags: List<String>,
    val title: String,
    val spells: List<SpellRoom>,
    @Embedded val stats: StatsRoom,
    val version: String,
    val isFavorite: Boolean = false
) {
    fun toChampionDetail(isFavorite: Boolean) = ChampionDetail(
        allytips,
        blurb,
        enemytips,
        id,
        info.toInfo(),
        key,
        lore,
        name,
        partype,
        passive.toPassive(),
        skins.map {
            it.toSkin()
        },
        tags,
        title,
        spells.map {
            it.toSpell()
        },
        stats.toStats(),
        version,
        isFavorite
    )
    companion object {
        fun fromChampionDetail(championDetail: ChampionDetail) = ChampionDetailRoom(
            championDetail.allytips,
            championDetail.blurb,
            championDetail.enemytips,
            championDetail.id,
            InfoRoom.fromInfo(championDetail.info),
            championDetail.key,
            championDetail.lore,
            championDetail.name,
            championDetail.partype,
            PassiveRoom.fromPassive(championDetail.passive),
            championDetail.skins.map {
                SkinRoom.fromSkin(it)
            },
            championDetail.tags,
            championDetail.title,
            championDetail.spells.map {
                SpellRoom.fromSpell(it)
            },
            StatsRoom.fromStats(championDetail.stats),
            championDetail.version
        )
    }
}

data class InfoRoom(
    val attack: Double,
    val defense: Double,
    val difficulty: Double,
    val magic: Double
) {
    fun toInfo() = Info(
        attack, defense, difficulty, magic
    )
    companion object {
        fun fromInfo(info: Info) = InfoRoom(
            info.attack, info.defense, info.difficulty, info.magic
        )
    }
}

data class PassiveRoom(
    val description: String,
    val image: String,
    val namePassive: String
) {
    fun toPassive() = Passive(
        description, image, namePassive
    )
    companion object {
        fun fromPassive(passive: Passive) = PassiveRoom(
            passive.description, passive.image, passive.name
        )
    }
}

data class SkinRoom(
    val chromas: Boolean,
    val id: String,
    val nameSkin: String,
    val num: Int
) {
    fun toSkin() = Skin(
        chromas, id, nameSkin, num
    )
    companion object {
        fun fromSkin(skin: Skin) = SkinRoom(
            skin.chromas, skin.id, skin.name, skin.num
        )
    }
}

data class SpellRoom(
    val cooldown: List<Double>,
    val cooldownBurn: String,
    val cost: List<Double>,
    val costBurn: String,
    val costType: String,
    val description: String,
    val effect: List<List<Double>?>,
    val id: String,
    val image: String,
    val leveltip: LeveltipRoom,
    val maxammo: String,
    val maxrank: Int,
    val name: String,
    val range: List<Double>,
    val rangeBurn: String,
    val resource: String,
    val tooltip: String,
) {
    fun toSpell() = Spell(
        cooldown,
        cooldownBurn,
        cost,
        costBurn,
        costType,
        description,
        effect,
        id,
        image,
        leveltip.toLevelTip(),
        maxammo,
        maxrank,
        name,
        range,
        rangeBurn,
        resource,
        tooltip
    )
    companion object {
        fun fromSpell(spell: Spell) = SpellRoom(
            spell.cooldown,
            spell.cooldownBurn,
            spell.cost,
            spell.costBurn,
            spell.costType,
            spell.description,
            spell.effect,
            spell.id,
            spell.image,
            LeveltipRoom.fromLevelTip(spell.leveltip),
            spell.maxammo,
            spell.maxrank,
            spell.name,
            spell.range,
            spell.rangeBurn,
            spell.resource,
            spell.tooltip
        )
    }
}

data class StatsRoom(
    val armor: Double?,
    val armorperlevel: Double?,
    val attackdamage: Double?,
    val attackdamageperlevel: Double?,
    val attackrange: Double?,
    val attackspeed: Double?,
    val attackspeedperlevel: Double?,
    val crit: Double?,
    val critperlevel: Double?,
    val hp: Double?,
    val hpperlevel: Double?,
    val hpregen: Double?,
    val hpregenperlevel: Double?,
    val movespeed: Double?,
    val mp: Double?,
    val mpperlevel: Double?,
    val mpregen: Double?,
    val mpregenperlevel: Double?,
    val spellblock: Double?,
    val spellblockperlevel: Double?
) {
    fun toStats() = Stats(
        armor,
        armorperlevel,
        attackdamage,
        attackdamageperlevel,
        attackrange,
        attackspeed,
        attackspeedperlevel,
        crit,
        critperlevel,
        hp,
        hpperlevel,
        hpregen,
        hpregenperlevel,
        movespeed,
        mp,
        mpperlevel,
        mpregen,
        mpregenperlevel,
        spellblock,
        spellblockperlevel
    )
    companion object {
        fun fromStats(stats: Stats) = StatsRoom(
            stats.armor,
            stats.armorperlevel,
            stats.attackdamage,
            stats.attackdamageperlevel,
            stats.attackrange,
            stats.attackspeed,
            stats.attackspeedperlevel,
            stats.crit,
            stats.critperlevel,
            stats.hp,
            stats.hpperlevel,
            stats.hpregen,
            stats.hpregenperlevel,
            stats.movespeed,
            stats.mp,
            stats.mpperlevel,
            stats.mpregen,
            stats.mpregenperlevel,
            stats.spellblock,
            stats.spellblockperlevel
        )
    }
}

data class LeveltipRoom(
    val effect: List<String>,
    val label: List<String>
) {
    fun toLevelTip() = Leveltip(
        effect, label
    )
    companion object {
        fun fromLevelTip(leveltip: Leveltip) = LeveltipRoom(
            leveltip.effect, leveltip.label
        )
    }
}
