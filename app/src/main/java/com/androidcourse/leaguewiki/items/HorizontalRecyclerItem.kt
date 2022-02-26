package com.androidcourse.leaguewiki.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.HorizontalRecyclerItemBinding
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class HorizontalRecyclerItem : AbstractBindingItem<HorizontalRecyclerItemBinding>() {

    var itemsList: List<GenericItem> = listOf()
    var isPager = false
    var viewPool = RecyclerView.RecycledViewPool()

    @ColorRes
    var colorActiveRes: Int = R.color.marigold

    @ColorRes
    var colorInactiveRes: Int = R.color.black_pearl

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): HorizontalRecyclerItemBinding {
        return HorizontalRecyclerItemBinding.inflate(inflater, parent, false).apply {
            recyclerView.setRecycledViewPool(viewPool)
        }
    }

    override fun bindView(binding: HorizontalRecyclerItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        var fastAdapter = binding.recyclerView.adapter as? GenericFastItemAdapter
        if (fastAdapter == null) {
            fastAdapter = GenericFastItemAdapter()
            binding.recyclerView.adapter = fastAdapter
        }
        fastAdapter.setNewList(itemsList)
        if (binding.recyclerView.onFlingListener == null && isPager) {
            val colorActive = ContextCompat.getColor(binding.root.context, colorActiveRes)
            val colorInactive = ContextCompat.getColor(binding.root.context, colorInactiveRes)
            binding.recyclerView.addItemDecoration(CirclePagerIndicator(colorActive, colorInactive))
            val helper: SnapHelper = PagerSnapHelper()
            helper.attachToRecyclerView(binding.recyclerView)
        }
    }

    override fun unbindView(binding: HorizontalRecyclerItemBinding) {
        super.unbindView(binding)
        if (isPager) {
            binding.recyclerView.removeItemDecorationAt(0)
            binding.recyclerView.onFlingListener = null
        }
    }

    override val type: Int = R.id.horizontal_recycler_item
}

fun horizontalRecyclerItem(block: HorizontalRecyclerItem.() -> Unit) =
    HorizontalRecyclerItem().apply(block)