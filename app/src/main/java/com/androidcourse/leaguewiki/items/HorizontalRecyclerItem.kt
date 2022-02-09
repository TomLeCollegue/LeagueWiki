package com.androidcourse.leaguewiki.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.HorizontalRecyclerItemBinding
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class HorizontalRecyclerItem : AbstractBindingItem<HorizontalRecyclerItemBinding>() {

    var itemsList: List<GenericItem> = listOf()
    private val fastAdapter = GenericFastItemAdapter()
    var isPager = false

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): HorizontalRecyclerItemBinding {
        return HorizontalRecyclerItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: HorizontalRecyclerItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.recyclerView.adapter = fastAdapter
        fastAdapter.setNewList(itemsList)
        if (binding.recyclerView.onFlingListener == null && isPager) {
            binding.recyclerView.addItemDecoration(CirclePagerIndicatorDecoration())
            val helper: SnapHelper = PagerSnapHelper()
            helper.attachToRecyclerView(binding.recyclerView)
        }
    }

    override fun unbindView(binding: HorizontalRecyclerItemBinding) {
        super.unbindView(binding)
        if(isPager) {
            binding.recyclerView.removeItemDecorationAt(0)
            binding.recyclerView.onFlingListener = null
        }
    }

    override val type: Int = R.id.horizontal_recycler_item
}

fun horizontalRecyclerItem(block: HorizontalRecyclerItem.() -> Unit) =
    HorizontalRecyclerItem().apply(block)