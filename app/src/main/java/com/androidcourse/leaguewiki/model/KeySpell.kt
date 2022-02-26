package com.androidcourse.leaguewiki.model

import androidx.annotation.DrawableRes
import com.androidcourse.leaguewiki.R

enum class KeySpell(val index: Int, @DrawableRes val res: Int) {
    Q_SPELL(res = R.drawable.q_spell, index = 0),
    Z_SPELL(res = R.drawable.z_spell, index = 1),
    E_SPELL(res = R.drawable.e_spell, index = 2),
    R_SPELL(res = R.drawable.r_spell, index = 3)
}