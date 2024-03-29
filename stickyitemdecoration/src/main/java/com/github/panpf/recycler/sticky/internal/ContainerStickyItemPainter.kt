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
    private var lastStickyItemViewHolder: RecyclerView.ViewHolder? = null
    private var lastStickyItemType: Int? = null
    private var lastParent: RecyclerView? = null

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
            val stickyItemView = showStickyItemView(parent, adapter, stickyItemPosition, logBuilder)
            offsetStickyItemView(
                parent, firstVisibleItemPosition, stickyItemPosition, stickyItemView, logBuilder
            )
        } else {
            lastStickyItemPosition = null
            lastStickyItemViewHolder = null
            lastStickyItemType = null
            lastParent = null
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

    override fun onAdapterDataChanged(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        val lastStickyItemPosition = lastStickyItemPosition ?: return
        val lastStickyItemViewHolder = lastStickyItemViewHolder ?: return
        val lastStickyItemType = lastStickyItemType ?: return
        val lastParent = lastParent ?: return
        if (lastStickyItemPosition >= adapter.itemCount) return
        val stickyItemType = adapter.getItemViewType(lastStickyItemPosition)
        if (stickyItemType == lastStickyItemType) {
            updateViewHolderData(
                lastStickyItemViewHolder,
                lastStickyItemPosition,
                lastParent,
                adapter
            )
        }
    }

    private fun showStickyItemView(
        parent: RecyclerView,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        stickyItemPosition: Int,
        logBuilder: StringBuilder?
    ): View {
        val lastStickyItemViewHolder = lastStickyItemViewHolder
        val lastStickyItemPosition = lastStickyItemPosition
        val lastStickyItemType = lastStickyItemType
        val lastParent = lastParent
        val stickyItemType = adapter.getItemViewType(stickyItemPosition)
        return if (
            lastStickyItemViewHolder == null
            || lastParent == null
            || stickyItemPosition != lastStickyItemPosition
            || stickyItemType != lastStickyItemType
        ) {
            val stickyItemViewHolder = viewHolderCachePool[stickyItemType]
                ?: adapter.createViewHolder(stickyItemContainer, stickyItemType).apply {
                    viewHolderCachePool.put(stickyItemType, this)
                }
            updateViewHolderData(stickyItemViewHolder, stickyItemPosition, parent, adapter)

            stickyItemContainer.apply {
                if (childCount > 0) {
                    removeAllViews()
                }
                addView(stickyItemViewHolder.itemView)
                visibility = View.VISIBLE
            }

            this@ContainerStickyItemPainter.lastStickyItemPosition = stickyItemPosition
            this@ContainerStickyItemPainter.lastStickyItemViewHolder = stickyItemViewHolder
            this@ContainerStickyItemPainter.lastStickyItemType = stickyItemType
            this@ContainerStickyItemPainter.lastParent = parent
            logBuilder?.append(". New")
            stickyItemViewHolder.itemView
        } else {
            logBuilder?.append(". NoChange")
            lastStickyItemViewHolder.itemView
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
        lastStickyItemViewHolder = null
        lastStickyItemPosition = null
        lastStickyItemType = null
        lastParent = null
        if (stickyItemContainer.childCount > 0) {
            stickyItemContainer.removeAllViews()
        }
        stickyItemContainer.visibility = View.INVISIBLE
    }
}