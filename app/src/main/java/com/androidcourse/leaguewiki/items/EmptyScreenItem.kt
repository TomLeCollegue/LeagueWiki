package com.androidcourse.leaguewiki.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.EmptyScreenItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class EmptyScreenItem : AbstractBindingItem<EmptyScreenItemBinding>() {

    var onClickRefresh: View.OnClickListener? = null

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): EmptyScreenItemBinding {
        return EmptyScreenItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: EmptyScreenItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.item.refreshButton.setOnClickListener(onClickRefresh)
    }

    override val type: Int = R.id.empty_screen_item
}

fun emptyScreenItem(block: EmptyScreenItem.() -> Unit) = EmptyScreenItem().apply(block)