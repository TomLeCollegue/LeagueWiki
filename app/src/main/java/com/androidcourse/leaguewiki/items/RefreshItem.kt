package com.androidcourse.leaguewiki.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.RefreshDetailsItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class RefreshItem : AbstractBindingItem<RefreshDetailsItemBinding>() {

    var onClick: View.OnClickListener? = null

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): RefreshDetailsItemBinding {
        return RefreshDetailsItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: RefreshDetailsItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.root.setOnClickListener(onClick)
    }

    override val type: Int = R.id.refresh_item

}

fun refreshItem(block: RefreshItem.() -> Unit) = RefreshItem().apply(block)