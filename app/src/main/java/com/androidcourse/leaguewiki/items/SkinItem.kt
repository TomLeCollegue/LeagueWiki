package com.androidcourse.leaguewiki.items

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

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): SkinItemBinding {
        return SkinItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: SkinItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.nameTextView.text = name
        Glide.with(binding.root.context).load(urlImage).into(binding.imageView)


        binding.root.setOnClickListener(onClickCard)
        binding.root.isClickable = onClickCard != null
    }

    override val type: Int = R.id.skin_item
}

fun skinItem(block: SkinItem.() -> Unit) = SkinItem().apply(block)