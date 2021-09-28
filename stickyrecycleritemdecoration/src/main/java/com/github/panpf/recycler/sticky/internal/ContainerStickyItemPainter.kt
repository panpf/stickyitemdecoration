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
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.recycler.sticky.StickyItemDecoration

class ContainerStickyItemPainter(
    baseStickyItemDecoration: StickyItemDecoration,
    private val stickyItemContainer: ViewGroup
) : StickyItemPainter(baseStickyItemDecoration) {

    private var lastStickyItemPosition: Int? = null
    private var lastStickyItemView: View? = null
    private var lastStickyItemType: Int? = null

    init {
        stickyItemContainer.isClickable = true
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = getAdapter(parent)?.takeIf { it.itemCount > 0 } ?: return
        val firstVisibleItemPosition = findFirstVisibleItemPosition(parent) ?: return

        val logBuilder: StringBuilder? =
            if (StickyItemDecoration.debugMode) StringBuilder() else null
        logBuilder?.append("onDraw. FirstItem: $firstVisibleItemPosition")

        val stickyItemPosition = findStickyItemPositionBackward(firstVisibleItemPosition)
        if (stickyItemPosition != null) {
            logBuilder?.append(". StickyItem=$stickyItemPosition")
            val stickyItemView = showStickyItemView(stickyItemPosition, adapter, logBuilder)
            offsetStickyItemView(
                parent, firstVisibleItemPosition, stickyItemPosition, stickyItemView, logBuilder
            )
        } else {
            lastStickyItemPosition = null
            lastStickyItemView = null
            lastStickyItemType = null
            stickyItemContainer.apply {
                if (childCount > 0) {
                    removeAllViews()
                }
                visibility = View.INVISIBLE
            }
            logBuilder?.append(". NoStickyItem")
        }
        hiddenOriginItemView(parent, firstVisibleItemPosition, stickyItemPosition)

        logBuilder?.apply {
            Log.d("ContainerStickyItem", this.toString())
        }
    }

    private fun showStickyItemView(
        stickItemPosition: Int,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        logBuilder: StringBuilder?
    ): View {
        val lastStickyItemView = lastStickyItemView
        val lastStickItemPosition = lastStickyItemPosition
        val lastStickyItemType = lastStickyItemType
        val stickyItemType = adapter.getItemViewType(stickItemPosition)
        return if (lastStickyItemView == null
            || stickItemPosition != lastStickItemPosition
            || stickyItemType != lastStickyItemType
        ) {
            val stickyItemViewHolder = viewHolderCachePool[stickyItemType]
                ?: adapter.createViewHolder(stickyItemContainer, stickyItemType).apply {
                    viewHolderCachePool.put(stickyItemType, this)
                }
            adapter.bindViewHolder(stickyItemViewHolder, stickItemPosition)
            val stickyItemView = stickyItemViewHolder.itemView
            stickyItemContainer.apply {
                if (childCount > 0) {
                    removeAllViews()
                }
                addView(stickyItemView)
                visibility = View.VISIBLE
            }

            this@ContainerStickyItemPainter.lastStickyItemPosition = stickItemPosition
            this@ContainerStickyItemPainter.lastStickyItemView = stickyItemView
            this@ContainerStickyItemPainter.lastStickyItemType = stickyItemType
            logBuilder?.append(". New")
            stickyItemView
        } else {
            logBuilder?.append(". NoChange")
            lastStickyItemView
        }
    }

    private fun offsetStickyItemView(
        parent: RecyclerView,
        firstVisibleItemPosition: Int,
        stickyItemPosition: Int,
        stickyItemView: View,
        logBuilder: StringBuilder?
    ) {
        if (disabledScrollUpStickyItem) {
            logBuilder?.append(". DisabledOffset")
            return
        }

        val nextStickItemPosition =
            findStickyItemPositionForward(parent, firstVisibleItemPosition)
        if (nextStickItemPosition == null || nextStickItemPosition <= stickyItemPosition) {
            logBuilder?.append(". NoNextStickyItem")
            return
        }

        val nextStickyItemView =
            parent.layoutManager?.findViewByPosition(nextStickItemPosition)
        if (nextStickyItemView == null) {
            logBuilder?.append(". NoNextStickyItemView")
            return
        }

        if (isVertical(parent)) {
            val nextStickyViewTop = nextStickyItemView.top
            val stickyContainerHeight = stickyItemContainer.height
            val stickyItemViewOffset = if (nextStickyViewTop in 0..stickyContainerHeight) {
                nextStickyViewTop - stickyContainerHeight
            } else {
                0
            }
            ViewCompat.offsetTopAndBottom(stickyItemView, stickyItemViewOffset - stickyItemView.top)
            logBuilder?.append(". Offset=${stickyItemViewOffset}")
        } else {
            val nextStickyViewLeft = nextStickyItemView.left
            val stickyContainerWidth = stickyItemContainer.width
            val stickyItemViewOffset = if (nextStickyViewLeft in 0..stickyContainerWidth) {
                nextStickyViewLeft - stickyContainerWidth
            } else {
                0
            }
            ViewCompat.offsetLeftAndRight(
                stickyItemView,
                stickyItemViewOffset - stickyItemView.left
            )
            logBuilder?.append(". Offset=${stickyItemViewOffset}")
        }
    }

    override fun reset() {
        super.reset()
        lastStickyItemView = null
        lastStickyItemPosition = null
        lastStickyItemType = null
        if (stickyItemContainer.childCount > 0) {
            stickyItemContainer.removeAllViews()
        }
        stickyItemContainer.visibility = View.INVISIBLE
    }
}