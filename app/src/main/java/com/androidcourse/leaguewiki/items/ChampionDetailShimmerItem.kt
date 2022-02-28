package com.androidcourse.leaguewiki.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.ChampionDetailShimmerLayoutBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ChampionDetailShimmerItem : AbstractBindingItem<ChampionDetailShimmerLayoutBinding>() {

    override val type: Int = R.id.champion_detail_shimmer_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ChampionDetailShimmerLayoutBinding {
        return ChampionDetailShimmerLayoutBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ChampionDetailShimmerLayoutBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.shimmerLayout.startShimmer()
    }

    override fun unbindView(binding: ChampionDetailShimmerLayoutBinding) {
        super.unbindView(binding)
        binding.shimmerLayout.stopShimmer()
    }

}

fun championDetailShimmerItem(block: ChampionDetailShimmerItem.() -> Unit) = ChampionDetailShimmerItem().apply(block)