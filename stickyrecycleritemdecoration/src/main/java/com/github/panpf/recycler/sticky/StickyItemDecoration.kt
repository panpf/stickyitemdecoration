package com.github.panpf.recycler.sticky

import android.util.SparseBooleanArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterLocalHelper

class StickyItemDecoration private constructor(
    stickyItemPositionList: List<Int>? = null,
    stickyItemTypeList: List<Int>? = null,
    stickyItemContainer: ViewGroup? = null,
) : BaseStickyItemDecoration(stickyItemContainer) {

    private val positionArray: SparseBooleanArray? =
        if (stickyItemPositionList?.isNotEmpty() == true) {
            SparseBooleanArray().apply {
                stickyItemPositionList.forEach { position -> put(position, true) }
            }
        } else {
            null
        }
    private val itemTypeArray: SparseBooleanArray? = if (stickyItemTypeList?.isNotEmpty() == true) {
        SparseBooleanArray().apply {
            stickyItemTypeList.forEach { itemType -> put(itemType, true) }
        }
    } else {
        null
    }
    private val concatAdapterLocalHelper by lazy { ConcatAdapterLocalHelper() }

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

    class Builder {

        private var stickyItemPositionList: List<Int>? = null
        private var stickyItemTypeList: List<Int>? = null
        private var stickyItemContainer: ViewGroup? = null

        fun position(vararg positions: Int): Builder {
            this.stickyItemPositionList = positions.toList()
            return this
        }

        fun position(positions: List<Int>): Builder {
            this.stickyItemPositionList = positions
            return this
        }

        fun itemType(vararg itemTypes: Int): Builder {
            this.stickyItemTypeList = itemTypes.toList()
            return this
        }

        fun itemType(itemTypes: List<Int>): Builder {
            this.stickyItemTypeList = itemTypes
            return this
        }

        fun showInContainer(stickyItemContainer: ViewGroup): Builder {
            this.stickyItemContainer = stickyItemContainer
            return this
        }

        fun build(): StickyItemDecoration {
            return StickyItemDecoration(
                stickyItemPositionList,
                stickyItemTypeList,
                stickyItemContainer
            )
        }
    }
}