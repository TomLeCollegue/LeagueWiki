package com.androidcourse.leaguewiki.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.ChampionListItemBinding
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ChampionListItem : AbstractBindingItem<ChampionListItemBinding>() {
    override val type: Int = R.id.champion_list_item

    var name: String? = null
    var description: String? = null
    var urlImage: String? = null
    var onClickCard: View.OnClickListener? = null

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ChampionListItemBinding {
        return ChampionListItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ChampionListItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.nameTextView.text = name
        binding.descriptionTextView.text = description
        Glide.with(binding.root.context).load(urlImage).into(binding.imageView)
        binding.root.setOnClickListener(onClickCard)
    }
}

fun championListItem(block: ChampionListItem.() -> Unit) = ChampionListItem().apply(block)