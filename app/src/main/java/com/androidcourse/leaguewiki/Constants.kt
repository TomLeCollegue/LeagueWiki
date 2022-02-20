package com.androidcourse.leaguewiki

object Constants {

    object Server {
        const val BASE_URL: String = "https://ddragon.leagueoflegends.com/"
        const val IMAGE_SPASH_URL: String = "cdn/img/champion/splash/%s_%d.jpg"
        const val IMAGE_CHAMP_URL: String = "cdn/12.4.1/img/champion/%s.png"
        const val IMAGE_SPELL_URL: String = "cdn/12.4.1/img/spell/%s"
        const val IMAGE_PASSIVE_URL: String = "cdn/12.4.1/img/passive/%s"
    }

    object Config {
        const val DEFAULT_VERSION: String = "12.4.1"
        const val VERSION_PREF: String = "Version_preference"
    }

    object Champions {
        const val DEFAULT_SKIN_NAME: String = "default"
    }
}