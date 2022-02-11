package com.androidcourse.leaguewiki.items

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.SearchBarItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem


class SearchBarItem : AbstractBindingItem<SearchBarItemBinding>() {

    override val type: Int = R.id.search_item
    var onTextChange: ((String) -> Unit)? = null
    var hint: String? = null

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): SearchBarItemBinding {
        return SearchBarItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: SearchBarItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.textField.hint = hint
        binding.textInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                onTextChange?.invoke(s.toString())
            }
        })
    }
}

fun searchBarItem(block: SearchBarItem.() -> Unit) = SearchBarItem().apply(block)
