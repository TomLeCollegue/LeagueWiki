package com.androidcourse.leaguewiki.model

data class ChampionDetail (
    val allytips: List<String>,
    val blurb: String,
    val enemytips: List<String>,
    val id: String,
    val info: Info,
    val key: String,
    val lore: String,
    val name: String,
    val partype: String,
    val passive: Passive,
    val skins: List<Skin>,
    val tags: List<String>,
    val title: String,
    val spells: List<Spell>,
    val stats: Stats,
)

data class Info(
    val attack: Double,
    val defense: Double,
    val difficulty: Double,
    val magic: Double
)

data class Passive(
    val description: String,
    val image: String,
    val name: String
)

data class Skin(
    val chromas: Boolean,
    val id: String,
    val name: String,
    val num: Int
)

data class Spell(
    val cooldown: List<Double>,
    val cooldownBurn: String,
    val cost: List<Double>,
    val costBurn: String,
    val costType: String,
    val description: String,
    val effect: List<List<Double>?>,
    val id: String,
    val image: String,
    val leveltip: Leveltip,
    val maxammo: String,
    val maxrank: Int,
    val name: String,
    val range: List<Double>,
    val rangeBurn: String,
    val resource: String,
    val tooltip: String,
)

data class Stats(
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
)

data class Leveltip(
    val effect: List<String>,
    val label: List<String>
)