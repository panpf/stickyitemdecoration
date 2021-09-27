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
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.recycler.sticky.StickyItemDecoration

class DrawStickyItemPainter(stickyItemDecoration: StickyItemDecoration) :
    StickyItemPainter(stickyItemDecoration) {

    private var lastStickyItemPosition: Int? = null
    private var lastStickyItemView: View? = null

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = getAdapter(parent)?.takeIf { it.itemCount > 0 } ?: return
        val firstVisibleItemPosition = findFirstVisibleItemPosition(parent) ?: return

        val logBuilder: StringBuilder? =
            if (StickyItemDecoration.debugMode) StringBuilder() else null
        logBuilder?.append("onDraw. FirstItem: $firstVisibleItemPosition")

        val stickyItemPosition = findStickyItemPositionBackward(firstVisibleItemPosition)
        if (stickyItemPosition != null) {
            logBuilder?.append(". StickyItem=$stickyItemPosition")
            val stickyItemView =
                prepareStickyItemView(parent, adapter, stickyItemPosition, logBuilder)
            val stickyItemViewOffset =
                calculateStickyItemOffset(
                    parent, firstVisibleItemPosition, stickyItemPosition, stickyItemView, logBuilder
                )
            logBuilder?.append(". Offset=${stickyItemViewOffset ?: 0}")

            showStickyItemView(canvas, parent, stickyItemView, stickyItemViewOffset)
        } else {
            lastStickyItemPosition = null
            lastStickyItemView = null
            logBuilder?.append(". NoStickyItem")
        }
        hiddenOriginItemView(parent, firstVisibleItemPosition, stickyItemPosition)

        logBuilder?.apply {
            Log.d("StickyItemDraw", this.toString())
        }
    }

    private fun prepareStickyItemView(
        parent: RecyclerView,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        stickItemPosition: Int,
        logBuilder: StringBuilder?,
    ): View {
        val lastStickyItemView = lastStickyItemView
        val lastStickItemPosition = lastStickyItemPosition
        return if (lastStickyItemView == null || stickItemPosition != lastStickItemPosition) {
            val stickyItemType = adapter.getItemViewType(stickItemPosition)
            val stickyItemViewHolder = viewHolderCachePool[stickyItemType]
                ?: adapter.createViewHolder(parent, stickyItemType).apply {
                    viewHolderCachePool.put(stickyItemType, this)
                }
            adapter.bindViewHolder(stickyItemViewHolder, stickItemPosition)
            val stickyItemView = stickyItemViewHolder.itemView.apply {
                val itemLayoutParams = layoutParams as RecyclerView.LayoutParams
                val layoutMarginStart = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    itemLayoutParams.marginStart.takeIf { it != 0 } ?: itemLayoutParams.leftMargin
                } else {
                    itemLayoutParams.leftMargin
                }
                val layoutMarginEnd = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    itemLayoutParams.marginEnd.takeIf { it != 0 } ?: itemLayoutParams.rightMargin
                } else {
                    itemLayoutParams.rightMargin
                }
                val itemWidthMeasureSpec = ViewGroup.getChildMeasureSpec(
                    View.MeasureSpec.makeMeasureSpec(
                        parent.width,
                        if (isVertical(parent)) View.MeasureSpec.EXACTLY else View.MeasureSpec.UNSPECIFIED
                    ),
                    parent.paddingLeft + parent.paddingRight + layoutMarginStart + layoutMarginEnd,
                    itemLayoutParams.width
                )
                val itemHeightMeasureSpec = ViewGroup.getChildMeasureSpec(
                    View.MeasureSpec.makeMeasureSpec(
                        parent.height,
                        if (isVertical(parent)) View.MeasureSpec.UNSPECIFIED else View.MeasureSpec.EXACTLY
                    ),
                    parent.paddingTop + parent.paddingBottom + itemLayoutParams.topMargin + itemLayoutParams.bottomMargin,
                    itemLayoutParams.height
                )
                measure(itemWidthMeasureSpec, itemHeightMeasureSpec)
                layout(
                    parent.paddingLeft + layoutMarginStart,
                    parent.paddingTop + itemLayoutParams.topMargin,
                    parent.paddingLeft + layoutMarginStart + measuredWidth,
                    parent.paddingTop + itemLayoutParams.topMargin + measuredHeight
                )
            }

            this@DrawStickyItemPainter.lastStickyItemPosition = stickItemPosition
            this@DrawStickyItemPainter.lastStickyItemView = stickyItemView
            logBuilder?.append(". New")
            stickyItemView
        } else {
            logBuilder?.append(". NoChange")
            lastStickyItemView
        }
    }

    private fun calculateStickyItemOffset(
        parent: RecyclerView,
        firstVisibleItemPosition: Int,
        stickyItemPosition: Int,
        stickyItemView: View,
        logBuilder: StringBuilder?,
    ): Int? {
        if (disabledScrollUpStickyItem) {
            logBuilder?.append(". DisabledOffset")
            return null
        }

        val nextStickItemPosition = findStickyItemPositionForward(parent, firstVisibleItemPosition)
        if (nextStickItemPosition == null || nextStickItemPosition <= stickyItemPosition) {
            logBuilder?.append(". NoNextStickyItem")
            return null
        }

        val nextStickyItemView = parent.layoutManager?.findViewByPosition(nextStickItemPosition)
        if (nextStickyItemView == null) {
            logBuilder?.append(". NoNextStickyItemView")
            return null
        }

        if (isVertical(parent)) {
            val nextStickyViewTop = nextStickyItemView.top
            val stickyItemViewHeight = stickyItemView.height
            return if (nextStickyViewTop in 0..stickyItemViewHeight) {
                nextStickyViewTop - stickyItemViewHeight
            } else {
                null
            }
        } else {
            val nextStickyViewLeft = nextStickyItemView.left
            val stickyItemViewWidth = stickyItemView.width
            return if (nextStickyViewLeft in 0..stickyItemViewWidth) {
                nextStickyViewLeft - stickyItemViewWidth
            } else {
                null
            }
        }
    }

    private fun showStickyItemView(
        canvas: Canvas,
        parent: RecyclerView,
        stickyItemView: View,
        stickyItemViewOffset: Int?,
    ) {
        canvas.save()
        if (stickyItemViewOffset != null) {
            if (isVertical(parent)) {
                canvas.translate(0f, stickyItemViewOffset.toFloat())
            } else {
                canvas.translate(stickyItemViewOffset.toFloat(), 0f)
            }
        }
        canvas.translate(stickyItemView.left.toFloat(), stickyItemView.top.toFloat())
        stickyItemView.draw(canvas)
        canvas.restore()
    }

    override fun reset() {
        super.reset()
        lastStickyItemPosition = null
        lastStickyItemView = null
    }
}