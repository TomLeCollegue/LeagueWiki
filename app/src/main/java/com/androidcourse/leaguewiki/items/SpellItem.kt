package com.androidcourse.leaguewiki.items

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.SpellItemBinding
import com.androidcourse.leaguewiki.extensions.setTextOrHide
import com.androidcourse.leaguewiki.model.KeySpell
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class SpellItem : AbstractBindingItem<SpellItemBinding>() {

    var title: String? = null
    var description: String? = null
    var urlImage: String? = null
    var spellKey: KeySpell? = null
    var onClickCard: View.OnClickListener? = null

    private var defaultRippleColor: ColorStateList? = null

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): SpellItemBinding {
        return SpellItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: SpellItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.nameTextView.text = title
        binding.descriptionTextView.setTextOrHide(description)
        Glide.with(binding.root.context).load(urlImage).into(binding.imageView)

        if (spellKey == null) {
            binding.spellImageView.isVisible = false
        } else {
            spellKey?.let {
                binding.spellImageView.setImageResource(it.res)
            }
        }

        defaultRippleColor = binding.root.rippleColor
        if (onClickCard == null) {
            binding.root.rippleColor = ColorStateList.valueOf(Color.TRANSPARENT)
        }
        binding.root.setOnClickListener(onClickCard)
    }

    override fun unbindView(binding: SpellItemBinding) {
        super.unbindView(binding)
        binding.root.rippleColor = defaultRippleColor
    }

    override val type: Int = R.id.spell_item
}

fun spellItem(block: SpellItem.() -> Unit) = SpellItem().apply(block)