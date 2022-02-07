package com.androidcourse.leaguewiki.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.SectionTitleItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class SectionTitleItem: AbstractBindingItem<SectionTitleItemBinding>() {

    var title: String? = null

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): SectionTitleItemBinding {
        return SectionTitleItemBinding.inflate(inflater,parent, false)
    }

    override fun bindView(binding: SectionTitleItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.titleTextView.text = title
    }

    override val type: Int = R.id.section_title_item
}

fun sectionTitleItem(block: SectionTitleItem.() -> Unit) = SectionTitleItem().apply(block)