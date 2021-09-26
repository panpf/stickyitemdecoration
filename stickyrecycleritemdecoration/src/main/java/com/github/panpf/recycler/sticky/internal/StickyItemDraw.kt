package com.github.panpf.recycler.sticky.internal

import android.graphics.Canvas
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.recycler.sticky.BaseStickyItemDecoration

open class StickyItemDraw(baseStickyItemDecoration: BaseStickyItemDecoration) :
    BaseStickyItemDraw(baseStickyItemDecoration) {

    private var lastStickyItemPosition: Int? = null
    private var lastStickyItemView: View? = null

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = getAdapter(parent)?.takeIf { it.itemCount > 0 } ?: return
        val firstVisibleItemPosition = findFirstVisibleItemPosition(parent) ?: return

        val logBuilder: StringBuilder? =
            if (BaseStickyItemDecoration.debugMode) StringBuilder() else null
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

            showStickyItemView(canvas, stickyItemView, stickyItemViewOffset)

            hiddenOriginItemView(parent, firstVisibleItemPosition, stickyItemPosition)
        } else {
            lastStickyItemPosition = null
            lastStickyItemView = null
            logBuilder?.append(". NoStickyItem")
        }

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
                val itemLayoutParams = layoutParams
                val itemWidthMeasureSpec = ViewGroup.getChildMeasureSpec(
                    View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY),
                    parent.paddingLeft + parent.paddingRight,
                    itemLayoutParams.width
                )
                val itemHeightMeasureSpec = ViewGroup.getChildMeasureSpec(
                    View.MeasureSpec.makeMeasureSpec(
                        parent.height, View.MeasureSpec.UNSPECIFIED
                    ),
                    parent.paddingTop + parent.paddingBottom,
                    itemLayoutParams.height
                )
                measure(itemWidthMeasureSpec, itemHeightMeasureSpec)
                layout(
                    parent.paddingLeft,
                    parent.paddingTop,
                    parent.paddingLeft + measuredWidth,
                    parent.paddingTop + measuredHeight
                )
            }

            this@StickyItemDraw.lastStickyItemPosition = stickItemPosition
            this@StickyItemDraw.lastStickyItemView = stickyItemView
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
        if (disabledScrollStickyHeader) {
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

        val nextStickyViewTop = nextStickyItemView.top
        val stickyItemViewHeight = stickyItemView.height
        return if (nextStickyViewTop in 0..stickyItemViewHeight) {
            nextStickyViewTop - stickyItemViewHeight
        } else {
            null
        }
    }

    private fun showStickyItemView(
        canvas: Canvas,
        stickyItemView: View,
        stickyItemViewOffset: Int?,
    ) {
        canvas.save()
        if (stickyItemViewOffset != null) {
            canvas.translate(0f, stickyItemViewOffset.toFloat())
        }
        stickyItemView.draw(canvas)
        canvas.restore()
    }

    override fun reset() {
        super.reset()
        lastStickyItemPosition = null
        lastStickyItemView = null
    }
}