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

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.SimpleAdapterDataObserver

// todo 可以不需要 container，因为有些不需要点击事件
@SuppressLint("LongLogTag")
open class StickyItemDecoration(
    private val stickyItemJudge: StickyItemJudge,
    private val stickyItemContainer: ViewGroup
) : RecyclerView.ItemDecoration() {

    companion object {
        private const val TAG = "StickyRecyclerItemDecoration"
        var DEBUG = false
    }

    private val viewHolderCachePool = SparseArray<RecyclerView.ViewHolder>()
    private var cacheAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    private var stickyItemPosition = -1
    private var invisibleItemView: View? = null
    private val simpleAdapterDataObserver = SimpleAdapterDataObserver {
        reset()
    }
    private var cacheInto: IntArray? = null

    /**
     * 禁止滑动过程中下一个 sticky header 往上顶当前 sticky header 的效果
     */
    var disabledScrollStickyHeader = false

    /**
     * 滑动过程中 sticky header 显示时隐藏列表中的 sticky item
     */
    private var invisibleStickyItemInList = false

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val adapter = getAdapter(parent)?.takeIf { it.itemCount > 0 } ?: return
        val firstVisibleItemPosition = findFirstVisibleItemPosition(parent)

        val logBuilder = if (DEBUG) StringBuilder() else null
        logBuilder?.apply {
            if (isNotEmpty()) append(", ")
            append("firstVisibleItemPosition: $firstVisibleItemPosition")
        }

        showStickyItemView(firstVisibleItemPosition, adapter)
        logBuilder?.apply {
            if (isNotEmpty()) append(", ")
            append("stickyItemPosition: $stickyItemPosition")
        }

        offsetStickyItemView(parent, firstVisibleItemPosition, logBuilder)
        hiddenOriginItemView(parent, firstVisibleItemPosition)

        logBuilder?.apply {
            Log.d(TAG, "onDraw. $this")
        }
    }

    private fun showStickyItemView(
        firstVisibleItemPosition: Int,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
    ) {
        val oldStickItemPosition = stickyItemPosition
        val newStickItemPosition = findStickyItemPositionBackward(firstVisibleItemPosition)
        if (newStickItemPosition != -1) {
            if (newStickItemPosition != oldStickItemPosition) {
                val stickyItemType = adapter.getItemViewType(newStickItemPosition)
                val stickyItemViewHolder = viewHolderCachePool[stickyItemType]
                    ?: adapter.createViewHolder(stickyItemContainer, stickyItemType).apply {
                        viewHolderCachePool.put(stickyItemType, this)
                    }
                adapter.bindViewHolder(stickyItemViewHolder, newStickItemPosition)

                stickyItemContainer.apply {
                    if (childCount > 0) {
                        removeAllViews()
                    }
                    addView(stickyItemViewHolder.itemView)
                    visibility = View.VISIBLE
                }

                stickyItemPosition = newStickItemPosition
            }
        } else {
            stickyItemPosition = -1
            stickyItemContainer.apply {
                if (childCount > 0) {
                    removeAllViews()
                }
                visibility = View.INVISIBLE
            }
        }
    }

    /**
     * 往上滑动时用新的 sticky item view 一点一点顶掉旧的 sticky item view
     */
    private fun offsetStickyItemView(
        parent: RecyclerView,
        firstVisibleItemPosition: Int,
        logBuilder: StringBuilder?
    ) {
        /*
         * 当前有 sticky item 需要显示就从当前位置往前找下一个 sticky item，找到并且已经跟当前 sticky item 的位置重叠了，就往上顶当前 sticky item
         */
        var offset = -99999
        var nextStickyViewTop = -99999
        var oldStickyViewTop = -99999
        var stickyContainerHeight = -1
        var nextStickItemPosition = -1
        if (stickyItemPosition != -1) {
            if (!disabledScrollStickyHeader) {
                offset = 0
                stickyContainerHeight = stickyItemContainer.height
                nextStickItemPosition =
                    findStickyItemPositionForward(parent, firstVisibleItemPosition)
                if (nextStickItemPosition > stickyItemPosition) {
                    val nextStickyItemView =
                        parent.layoutManager?.findViewByPosition(nextStickItemPosition)
                    if (nextStickyItemView != null) {
                        nextStickyViewTop = nextStickyItemView.top
                        if (nextStickyViewTop >= 0 && nextStickyViewTop <= stickyContainerHeight) {
                            offset = nextStickyViewTop - stickyContainerHeight
                        }
                    }
                }
                if (stickyItemContainer.childCount > 0) {
                    val stickyView = stickyItemContainer.getChildAt(0)
                    oldStickyViewTop = stickyView.top
                    ViewCompat.offsetTopAndBottom(stickyView, offset - oldStickyViewTop)
                }
                logBuilder?.apply {
                    if (isNotEmpty()) append(", ")
                    append("nextStickItemPosition: $nextStickItemPosition")
                    if (isNotEmpty()) append(", ")
                    append("offset: $offset")
                }
            }
            stickyItemContainer.visibility = View.VISIBLE
        } else {
            if (stickyItemContainer.childCount > 0) {
                stickyItemContainer.removeAllViews()
            }
            stickyItemContainer.visibility = View.INVISIBLE
        }
    }

    /*
     * 列表中的 item 需要在 sticky item 显示的时候隐藏，划出列表的时候恢复显示
     */
    private fun hiddenOriginItemView(
        parent: RecyclerView,
        firstVisibleItemPosition: Int
    ) {
        if (invisibleStickyItemInList) {
            if (stickyItemPosition != -1 && stickyItemPosition == firstVisibleItemPosition) {
                val stickyItemView =
                    /**
                     * 根据位置获取其 view
                     */
                    parent.layoutManager?.findViewByPosition(stickyItemPosition)
                // stickyItemView.getTop() == 0 时隐藏 stickyItemView 会导致 sticky header 区域闪烁一下，这是因为在 sticky header 显示出来之前隐藏了 stickyItemView
                // 因此限定 stickyItemView.getTop() < 0 也就是说 sticky item 和 sticky header 错开的时候隐藏 sticky item 可以一定程度上避免闪烁，但滑动的快了还是会闪烁一下
                if (stickyItemView != null && stickyItemView.visibility != View.INVISIBLE && stickyItemView.top < 0) {
                    stickyItemView.visibility = View.INVISIBLE
                    invisibleItemView = stickyItemView
                    if (DEBUG) Log.i(TAG, "hidden in list sticky item: $stickyItemPosition")
                }
            } else if (invisibleItemView != null && invisibleItemView!!.visibility != View.VISIBLE) {
                invisibleItemView!!.visibility = View.VISIBLE
                if (DEBUG) Log.i(TAG, "resume in list sticky item: $stickyItemPosition")
            }
        }
    }

    private fun findStickyItemPositionBackward(formPosition: Int): Int {
        val adapter = getAdapter()
        if (adapter != null && formPosition >= 0) {
            for (position in formPosition downTo 0) {
                if (stickyItemJudge.isStickyItemByPosition(adapter, position)) {
                    return position
                }
            }
        }
        return -1
    }

    private fun findStickyItemPositionForward(recyclerView: RecyclerView, formPosition: Int): Int {
        val adapter = getAdapter()
        if (adapter != null && formPosition >= 0) {
            val lastVisibleItemPosition = findLastVisibleItemPosition(recyclerView)
            if (lastVisibleItemPosition >= 0) {
                for (position in formPosition..lastVisibleItemPosition) {
                    if (stickyItemJudge.isStickyItemByPosition(adapter, position)) {
                        return position
                    }
                }
            }
        }
        return -1
    }

    private fun findFirstVisibleItemPosition(recyclerView: RecyclerView): Int =
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
                }.minOf { it }
            }
            else -> {
                0
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
                }.minOf { it }  // todo 这里貌似要找最大的
            }
            else -> {
                0
            }
        }

    private fun getAdapter(parent: RecyclerView? = null): RecyclerView.Adapter<RecyclerView.ViewHolder>? {
        val oldAdapter = this.cacheAdapter
        return if (parent != null) {
            val newAdapter = parent.adapter
            if (oldAdapter !== newAdapter) {
                reset()
                try {
                    newAdapter?.registerAdapterDataObserver(simpleAdapterDataObserver)
                } catch (e: Exception) {
                }
                this.cacheAdapter = newAdapter
            }
            newAdapter
        } else {
            oldAdapter
        }
    }

    private fun reset() {
        if (stickyItemContainer.childCount > 0) {
            stickyItemContainer.removeAllViews()
        }
        stickyItemContainer.visibility = View.INVISIBLE
        stickyItemPosition = -1
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
                this@StickyItemDecoration.cacheInto = this
            }
        }
    }
}