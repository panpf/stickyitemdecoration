package com.github.panpf.recycler.sticky

import android.util.SparseBooleanArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterLocalHelper

class SimpleStickyItemDecoration(
    stickyItemContainer: ViewGroup,
    positionArray: SparseBooleanArray?,
    itemTypeArray: SparseBooleanArray?,
) : StickyItemDecoration(
    SimpleStickyItemJudge(positionArray, itemTypeArray),
    stickyItemContainer
) {

    class Builder(private val stickyItemContainer: ViewGroup) {

        private val positionArray = SparseBooleanArray()
        private val itemTypeArray = SparseBooleanArray()

        fun position(vararg positions: Int): Builder {
            positions.forEach { position ->
                positionArray.put(position, true)
            }
            return this
        }

        fun position(positions: List<Int>): Builder {
            positions.forEach { position ->
                positionArray.put(position, true)
            }
            return this
        }

        fun itemType(vararg itemTypes: Int): Builder {
            itemTypes.forEach { itemType ->
                itemTypeArray.put(itemType, true)
            }
            return this
        }

        fun itemType(itemTypes: List<Int>): Builder {
            itemTypes.forEach { itemType ->
                itemTypeArray.put(itemType, true)
            }
            return this
        }

        fun build(): SimpleStickyItemDecoration {
            return SimpleStickyItemDecoration(
                stickyItemContainer,
                if (positionArray.size() > 0) positionArray else null,
                if (itemTypeArray.size() > 0) itemTypeArray else null,
            )
        }
    }

    class SimpleStickyItemJudge(
        private val positionArray: SparseBooleanArray?,
        private val itemTypeArray: SparseBooleanArray?,
    ) : StickyItemJudge {

        private val concatAdapterLocalHelper = ConcatAdapterLocalHelper()

        override fun isStickyItemByPosition(
            adapter: RecyclerView.Adapter<*>,
            position: Int
        ): Boolean {
            if (positionArray?.get(position) == true) {
                return true
            }

            if (itemTypeArray != null) {
                val (localAdapter, localPosition) = concatAdapterLocalHelper
                    .findLocalAdapterAndPosition(adapter, position)
                if (itemTypeArray.get(localAdapter.getItemViewType(localPosition))) {
                    return true
                }
            }

            return false
        }
    }
}