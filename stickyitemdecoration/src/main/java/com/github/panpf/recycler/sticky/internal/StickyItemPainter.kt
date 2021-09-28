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
package com.github.panpf.recycler.sticky.internal

import android.graphics.Canvas
import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.*
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterLocalHelper
import com.github.panpf.assemblyadapter.recycler.SimpleAdapterDataObserver
import com.github.panpf.recycler.sticky.StickyItemDecoration

abstract class StickyItemPainter(private val stickyItemDecoration: StickyItemDecoration) {

    protected val viewHolderCachePool = SparseArray<RecyclerView.ViewHolder>()
    private var cacheAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    private var invisibleItemView: View? = null
    private var cacheInto: IntArray? = null
    private val simpleAdapterDataObserver = SimpleAdapterDataObserver {
        cacheAdapter?.let { it1 -> onAdapterDataChanged(it1) }
    }
    private val concatAdapterLocalHelper by lazy { ConcatAdapterLocalHelper() }

    var disabledScrollUpStickyItem = false
        set(value) {
            field = value
            reset()
        }

    var invisibleOriginItemWhenStickyItemShowing = false
        set(value) {
            field = value
            reset()
        }

    abstract fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State)
    abstract fun onAdapterDataChanged(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>)

    protected fun updateViewHolderData(
        viewHolder: RecyclerView.ViewHolder,
        position: Int,
        parent: RecyclerView,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    ) {
        val localAdapter =
            concatAdapterLocalHelper.findLocalAdapterAndPosition(adapter, position).first
        viewHolder.apply {
            // Ensure that the ViewHolder getBindingAdapterPosition() and getAbsoluteAdapterPosition methods are valid
            positionCompat = position
            // Ensure that the ViewHolder getBindingAdapterPosition() and getAbsoluteAdapterPosition methods are valid
            ownerRecyclerViewCompat = parent
            // Ensure that the ViewHolder getBindingAdapterPosition() and getAbsoluteAdapterPosition methods are valid
            if (bindingAdapterCompat == null) {
                bindingAdapterCompat = localAdapter
                rootBindInit()
            }
        }
        // Ensure that the ViewHolder getBindingAdapterPosition() and getAbsoluteAdapterPosition methods are valid
        adapter.bindViewHolder(viewHolder, position)
    }

    protected fun hiddenOriginItemView(
        parent: RecyclerView,
        firstVisibleItemPosition: Int,
        stickyItemPosition: Int?
    ) {
        if (!invisibleOriginItemWhenStickyItemShowing) return

        if (stickyItemPosition != null && stickyItemPosition == firstVisibleItemPosition) {
            val originStickyItemView = parent.layoutManager?.findViewByPosition(stickyItemPosition)
            // originStickyItemView.getTop() == 0 时隐藏 originStickyItemView 会导致 sticky header 区域闪烁一下，
            // 这是因为在 sticky header 显示出来之前隐藏了 stickyItemView
            // 因此限定 originStickyItemView.getTop() < 0 也就是说 sticky item 和 sticky header 错开的时候隐藏 sticky item 可以一定程度上避免闪烁，
            // 但滑动的快了还是会闪烁一下
            if (originStickyItemView != null && originStickyItemView.visibility != View.INVISIBLE && originStickyItemView.top < 0) {
                originStickyItemView.visibility = View.INVISIBLE
                invisibleItemView = originStickyItemView
            }
        } else {
            val invisibleItemView = invisibleItemView
            if (invisibleItemView != null && invisibleItemView.visibility != View.VISIBLE) {
                invisibleItemView.visibility = View.VISIBLE
            }
        }
    }

    protected fun getAdapter(parent: RecyclerView? = null): RecyclerView.Adapter<RecyclerView.ViewHolder>? {
        val oldAdapter = this.cacheAdapter
        return if (parent != null) {
            val newAdapter = parent.adapter
            if (oldAdapter !== newAdapter) {
                reset()
                this.cacheAdapter = newAdapter
                try {
                    newAdapter?.registerAdapterDataObserver(simpleAdapterDataObserver)
                } catch (e: Exception) {
                }
            }
            newAdapter
        } else {
            oldAdapter
        }
    }

    protected fun findStickyItemPositionBackward(formPosition: Int): Int? {
        val adapter = getAdapter()
        if (adapter != null && formPosition >= 0) {
            for (position in formPosition downTo 0) {
                if (stickyItemDecoration.isStickyItemByPosition(adapter, position)) {
                    return position
                }
            }
        }
        return null
    }

    protected fun findStickyItemPositionForward(
        recyclerView: RecyclerView,
        formPosition: Int
    ): Int? {
        val adapter = getAdapter()
        if (adapter != null && formPosition >= 0) {
            val lastVisibleItemPosition = findLastVisibleItemPosition(recyclerView)
            if (lastVisibleItemPosition >= 0) {
                for (position in formPosition..lastVisibleItemPosition) {
                    if (stickyItemDecoration.isStickyItemByPosition(adapter, position)) {
                        return position
                    }
                }
            }
        }
        return null
    }

    protected fun findFirstVisibleItemPosition(recyclerView: RecyclerView): Int? =
        when (val layoutManager = recyclerView.layoutManager) {
            is GridLayoutManager -> {
                layoutManager.findFirstVisibleItemPosition()
            }
            is LinearLayoutManager -> {
                layoutManager.findFirstVisibleItemPosition()
            }
            is StaggeredGridLayoutManager -> {
                getInto(layoutManager).apply {
                    layoutManager.findFirstVisibleItemPositions(this)
                }.minOfOrNull { it } ?: 0
            }
            else -> {
                null
            }
        }

    private fun findLastVisibleItemPosition(recyclerView: RecyclerView): Int =
        when (val layoutManager = recyclerView.layoutManager) {
            is GridLayoutManager -> {
                layoutManager.findLastVisibleItemPosition()
            }
            is LinearLayoutManager -> {
                layoutManager.findLastVisibleItemPosition()
            }
            is StaggeredGridLayoutManager -> {
                getInto(layoutManager).apply {
                    layoutManager.findLastVisibleItemPositions(this)
                }.maxOfOrNull { it } ?: 0
            }
            else -> {
                0
            }
        }

    protected open fun reset() {
        viewHolderCachePool.clear()
        val invisibleItemView = this.invisibleItemView
        if (invisibleItemView != null) {
            invisibleItemView.visibility = View.VISIBLE
            this.invisibleItemView = null
        }
    }

    private fun getInto(layoutManager: StaggeredGridLayoutManager): IntArray {
        val intoCache = this.cacheInto
        val spanCount = layoutManager.spanCount
        return if (intoCache != null && intoCache.size == spanCount) {
            intoCache
        } else {
            IntArray(spanCount).apply {
                this@StickyItemPainter.cacheInto = this
            }
        }
    }

    protected fun isVertical(parent: RecyclerView): Boolean {
        return when (val layoutManager = parent.layoutManager) {
            is LinearLayoutManager -> layoutManager.orientation == LinearLayoutManager.VERTICAL
            is GridLayoutManager -> layoutManager.orientation == GridLayoutManager.VERTICAL
            is StaggeredGridLayoutManager -> layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL
            else -> true
        }
    }
}