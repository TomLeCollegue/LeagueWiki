package com.androidcourse.leaguewiki.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.core.view.updateLayoutParams
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.SpaceItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class SpaceItem: AbstractBindingItem<SpaceItemBinding>() {

    override val type: Int = R.id.space_item

    @DimenRes
    var spaceRes: Int = 0
    var orientation: Orientation = Orientation.VERTICAL

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): SpaceItemBinding {
        return SpaceItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: SpaceItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)

        if (spaceRes == 0) {
            binding.space.updateLayoutParams {
                height = 0
                width = 0
            }
        } else {
            binding.space.updateLayoutParams {
                if (orientation == Orientation.VERTICAL) {
                    height = binding.root.context.resources.getDimensionPixelSize(spaceRes)
                } else {
                    width = binding.root.context.resources.getDimensionPixelSize(spaceRes)
                }
            }
        }
    }

    enum class Orientation {
        VERTICAL, HORIZONTAL
    }
}

fun spaceItem(block: SpaceItem.() -> Unit) = SpaceItem().apply(block)