package com.androidcourse.leaguewiki.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.TagItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class TagItem : AbstractBindingItem<TagItemBinding>() {

    var text: String? = null
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): TagItemBinding {
        return TagItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: TagItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.textView.text = text
    }

    override val type: Int = R.id.tag_item
}

fun tagItem(block: TagItem.() -> Unit) = TagItem().apply(block)