/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.panpf.recycler.sticky

import android.graphics.Canvas
import android.util.SparseBooleanArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterLocalHelper
import com.github.panpf.recycler.sticky.internal.StickyItemPainter
import com.github.panpf.recycler.sticky.internal.ContainerStickyItemPainter
import com.github.panpf.recycler.sticky.internal.DrawStickyItemPainter

/**
 * You can make the item with the specified position or type always displayed at the top of the RecyclerView.
 * If you want the item to be clickable, you can specify a container dedicated to displaying the sticky item through the stickyItemContainer parameter.
 * Support horizontal and item margin
 */
open class StickyItemDecoration constructor(
    stickyItemPositionList: List<Int>? = null,
    stickyItemTypeList: List<Int>? = null,
    stickyItemContainer: ViewGroup? = null,
) : RecyclerView.ItemDecoration() {

    companion object {
        var debugMode = false
    }

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

    @Suppress("LeakingThis")
    private val stickyItemPainter: StickyItemPainter = if (stickyItemContainer != null) {
        ContainerStickyItemPainter(this, stickyItemContainer)
    } else {
        DrawStickyItemPainter(this)
    }

    var disabledScrollUpStickyItem
        get() = stickyItemPainter.disabledScrollUpStickyItem
        set(value) {
            stickyItemPainter.disabledScrollUpStickyItem = value
        }

    var invisibleOriginItemWhenStickyItemShowing
        get() = stickyItemPainter.invisibleOriginItemWhenStickyItemShowing
        set(value) {
            stickyItemPainter.invisibleOriginItemWhenStickyItemShowing = value
        }

    private var cacheLocalAdapter: RecyclerView.Adapter<*>? = null
    private var cacheLocalPosition: Int? = null
    private var cachePosition: Int? = null

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        stickyItemPainter.onDrawOver(canvas, parent, state)
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

        /**
         * Set the item at the specified position to always be displayed at the top of the RecyclerView
         */
        open fun position(vararg positions: Int): Builder {
            this.stickyItemPositionList = positions.toList()
            return this
        }

        /**
         * Set the item at the specified position to always be displayed at the top of the RecyclerView
         */
        open fun position(positions: List<Int>): Builder {
            this.stickyItemPositionList = positions
            return this
        }

        /**
         * Set the item at the specified type to always be displayed at the top of the RecyclerView
         */
        open fun itemType(vararg itemTypes: Int): Builder {
            this.stickyItemTypeList = itemTypes.toList()
            return this
        }

        /**
         * Set the item at the specified type to always be displayed at the top of the RecyclerView
         */
        open fun itemType(itemTypes: List<Int>): Builder {
            this.stickyItemTypeList = itemTypes
            return this
        }

        /**
         * Set the container for sticky item display so that the sticky item can receive click events
         */
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