package com.github.panpf.recycler.sticky

import android.graphics.Canvas
import android.util.SparseBooleanArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterLocalHelper
import com.github.panpf.recycler.sticky.internal.ContainerStickyItemDraw
import com.github.panpf.recycler.sticky.internal.StickyItemDraw

open class StickyItemDecoration constructor(
    stickyItemPositionList: List<Int>? = null,
    stickyItemTypeList: List<Int>? = null,
    stickyItemContainer: ViewGroup? = null,
) : RecyclerView.ItemDecoration() {

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

    companion object {
        var debugMode = true
    }

    @Suppress("LeakingThis")
    private val sticky = if (stickyItemContainer != null) {
        ContainerStickyItemDraw(this, stickyItemContainer)
    } else {
        StickyItemDraw(this)
    }

    var disabledScrollUpStickyItem
        get() = sticky.disabledScrollUpStickyItem
        set(value) {
            sticky.disabledScrollUpStickyItem = value
        }

    var invisibleOriginItemWhenStickyItemShowing
        get() = sticky.invisibleOriginItemWhenStickyItemShowing
        set(value) {
            sticky.invisibleOriginItemWhenStickyItemShowing = value
        }

    private var cacheLocalAdapter: RecyclerView.Adapter<*>? = null
    private var cacheLocalPosition: Int? = null
    private var cachePosition: Int? = null

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        sticky.onDrawOver(canvas, parent, state)
    }

    open fun isStickyItemByPosition(
        adapter: RecyclerView.Adapter<*>,
        position: Int
    ): Boolean {
        cacheLocalAdapter = null
        cacheLocalPosition = null
        cachePosition = null

        if (positionArray?.get(position) == true) {
            return true
        }

        if (itemTypeArray != null) {
            val (localAdapter, localPosition) = findLocalAdapterAndPosition(adapter, position)
            if (itemTypeArray.get(localAdapter.getItemViewType(localPosition))) {
                return true
            }
        }

        return false
    }

    protected fun findLocalAdapterAndPosition(
        adapter: RecyclerView.Adapter<*>,
        position: Int
    ): Pair<RecyclerView.Adapter<*>, Int> {
        val cacheLocalAdapter = cacheLocalAdapter
        val cacheLocalPosition = cacheLocalPosition
        val cachePosition = cachePosition
        return if (
            cacheLocalAdapter != null
            && cacheLocalPosition != null
            && cachePosition != null
            && cachePosition == position
        ) {
            cacheLocalAdapter to cacheLocalPosition
        } else {
            concatAdapterLocalHelper
                .findLocalAdapterAndPosition(adapter, position).apply {
                    this@StickyItemDecoration.cacheLocalAdapter = first
                    this@StickyItemDecoration.cacheLocalPosition = second
                    this@StickyItemDecoration.cachePosition = position
                }
        }
    }

    open class Builder {

        protected var stickyItemPositionList: List<Int>? = null
        protected var stickyItemTypeList: List<Int>? = null
        protected var stickyItemContainer: ViewGroup? = null

        open fun position(vararg positions: Int): Builder {
            this.stickyItemPositionList = positions.toList()
            return this
        }

        open fun position(positions: List<Int>): Builder {
            this.stickyItemPositionList = positions
            return this
        }

        open fun itemType(vararg itemTypes: Int): Builder {
            this.stickyItemTypeList = itemTypes.toList()
            return this
        }

        open fun itemType(itemTypes: List<Int>): Builder {
            this.stickyItemTypeList = itemTypes
            return this
        }

        open fun showInContainer(stickyItemContainer: ViewGroup): Builder {
            this.stickyItemContainer = stickyItemContainer
            return this
        }

        open fun build(): StickyItemDecoration {
            return StickyItemDecoration(
                stickyItemPositionList,
                stickyItemTypeList,
                stickyItemContainer
            )
        }
    }
}