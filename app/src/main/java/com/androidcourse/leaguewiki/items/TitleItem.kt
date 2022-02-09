package com.androidcourse.leaguewiki.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.TitleItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class TitleItem: AbstractBindingItem<TitleItemBinding>() {

    var text: String? = null

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): TitleItemBinding {
        return TitleItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: TitleItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.title.text = text
    }

    override val type: Int = R.id.title_item
}

fun titleItem(block: TitleItem.() -> Unit ) = TitleItem().apply(block)