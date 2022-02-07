package com.androidcourse.leaguewiki.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.SpellItemBinding
import com.androidcourse.leaguewiki.setTextOrHide
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class SpellItem: AbstractBindingItem<SpellItemBinding>() {

    var title: String? = null
    var description: String? = null
    var urlImage: String? = null
    var onClickCard: View.OnClickListener? = null

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): SpellItemBinding {
        return SpellItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: SpellItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.nameTextView.text = title
        binding.descriptionTextView.setTextOrHide(description)
        Glide.with(binding.root.context).load(urlImage).into(binding.imageView)
        binding.root.setOnClickListener(onClickCard)
    }

    override val type: Int = R.id.spell_item
}

fun spellItem(block: SpellItem.() -> Unit) = SpellItem().apply(block)