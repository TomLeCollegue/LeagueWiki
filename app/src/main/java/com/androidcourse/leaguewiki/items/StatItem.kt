package com.androidcourse.leaguewiki.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.StatItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class StatItem: AbstractBindingItem<StatItemBinding>() {

    var text: String? = null

    @DrawableRes
    var icRes: Int? = null

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): StatItemBinding {
        return StatItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: StatItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.textView.text = text

        if(icRes == null) {
            binding.statImageView.isVisible = false
        } else {
            icRes?.let {
                binding.statImageView.setImageResource(it)
            }
        }
    }
    override val type: Int = R.id.stat_item

}

fun statItem(block: StatItem.() -> Unit) = StatItem().apply(block)