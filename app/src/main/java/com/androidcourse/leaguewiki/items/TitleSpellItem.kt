package com.androidcourse.leaguewiki.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.TitleSpellItemBinding
import com.androidcourse.leaguewiki.model.KeySpell
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class TitleSpellItem : AbstractBindingItem<TitleSpellItemBinding>() {

    var title: String? = null
    var urlImage: String? = null
    var spellKey: KeySpell? = null

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): TitleSpellItemBinding {
        return TitleSpellItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: TitleSpellItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.nameTextView.text = title
        Glide.with(binding.root.context).load(urlImage).into(binding.spellImageView)

        if (spellKey == null) {
            binding.spellKeyImageView.isVisible = false
        } else {
            spellKey?.let {
                binding.spellKeyImageView.setImageResource(it.res)
            }
        }
    }

    override val type: Int = R.id.title_spell_item
}

fun titleSpellItem(block: TitleSpellItem.() -> Unit) = TitleSpellItem().apply(block)