package com.androidcourse.leaguewiki.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.CaptionItemBinding
import com.androidcourse.leaguewiki.extensions.setOnClickListenerOrHideRipple
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class CaptionItem : AbstractBindingItem<CaptionItemBinding>() {

    var text: String? = null
    var maxLine: Int = Int.MAX_VALUE
    var onClick: View.OnClickListener? = null

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): CaptionItemBinding {
        return CaptionItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: CaptionItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.caption.text = text
        binding.caption.maxLines = maxLine
        binding.caption.setOnClickListenerOrHideRipple(onClick)
    }

    override val type: Int = R.id.caption_item
}

fun captionItem(block: CaptionItem.() -> Unit) = CaptionItem().apply(block)