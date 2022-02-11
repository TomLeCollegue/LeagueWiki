package com.androidcourse.leaguewiki.items

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.SkinItemBinding
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class SkinItem : AbstractBindingItem<SkinItemBinding>() {

    var name: String? = null
    var urlImage: String? = null
    var onClickCard: View.OnClickListener? = null

    private var defaultRippleColor: ColorStateList? = null

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): SkinItemBinding {
        return SkinItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: SkinItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.nameTextView.text = name
        Glide.with(binding.root.context).load(urlImage).into(binding.imageView)

        defaultRippleColor = binding.card.rippleColor
        if (onClickCard == null) {
            binding.card.rippleColor = ColorStateList.valueOf(Color.TRANSPARENT)
        }
    }

    override fun unbindView(binding: SkinItemBinding) {
        super.unbindView(binding)
        binding.card.rippleColor = defaultRippleColor
    }

    override val type: Int = R.id.skin_item
}

fun skinItem(block: SkinItem.() -> Unit) = SkinItem().apply(block)