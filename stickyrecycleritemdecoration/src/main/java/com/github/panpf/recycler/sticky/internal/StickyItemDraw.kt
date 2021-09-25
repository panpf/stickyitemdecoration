package com.github.panpf.recycler.sticky.internal

import android.graphics.Canvas
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.recycler.sticky.BaseStickyItemDecoration

class StickyItemDraw(baseStickyItemDecoration: BaseStickyItemDecoration) :
    BaseStickyItemDraw(baseStickyItemDecoration) {

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = getAdapter(parent)?.takeIf { it.itemCount > 0 } ?: return
        val firstVisibleItemPosition = findFirstVisibleItemPosition(parent) ?: return

        val logBuilder: StringBuilder? = StringBuilder()
        logBuilder?.append("firstVisibleItemPosition: $firstVisibleItemPosition")

        prepareStickyItemView(parent, firstVisibleItemPosition, adapter, logBuilder)
        calculateStickyItemOffset(parent, firstVisibleItemPosition, logBuilder)
        drawStickyItemView(canvas, logBuilder)
        hiddenOriginItemView(parent, firstVisibleItemPosition)

        logBuilder?.apply {
            Log.d("StickyItemDraw", "onDraw. $this")
        }
    }

    private fun prepareStickyItemView(
        parent: RecyclerView,
        firstVisibleItemPosition: Int,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        logBuilder: StringBuilder?,
    ) {
        val oldStickItemPosition = stickyItemPosition
        val newStickItemPosition = findStickyItemPositionBackward(firstVisibleItemPosition)
        if (newStickItemPosition == null) {
            logBuilder?.append(", ")?.append("No Sticky Item")
            stickyItemPosition = null
            stickyItemView = null
            return
        }

        // Changed
        if (newStickItemPosition != oldStickItemPosition) {
            stickyItemPosition = newStickItemPosition
            val stickyItemType = adapter.getItemViewType(newStickItemPosition)
            val stickyItemViewHolder = viewHolderCachePool[stickyItemType]
                ?: adapter.createViewHolder(parent, stickyItemType).apply {
                    viewHolderCachePool.put(stickyItemType, this)
                }
            adapter.bindViewHolder(stickyItemViewHolder, newStickItemPosition)
            stickyItemView = stickyItemViewHolder.itemView.apply {
                val itemLayoutParams = layoutParams
                val itemWidthMeasureSpec = ViewGroup.getChildMeasureSpec(
                    View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY),
                    parent.paddingLeft + parent.paddingRight,
                    itemLayoutParams.width
                )
                val itemHeightMeasureSpec = ViewGroup.getChildMeasureSpec(
                    View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED),
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
            logBuilder?.append(", ")?.append("New Sticky Item: position=$newStickItemPosition")
        } else {
            logBuilder?.append(", ")?.append("No Change")
        }
    }

    /**
     * 往上滑动时用新的 sticky item view 一点一点顶掉旧的 sticky item view
     */
    private fun calculateStickyItemOffset(
        parent: RecyclerView,
        firstVisibleItemPosition: Int,
        logBuilder: StringBuilder?,
    ) {
        if (disabledScrollStickyHeader) {
            logBuilder?.append(", ")?.append("disabledOffset")
            return
        }

        val stickyItemPosition = stickyItemPosition
        val stickyItemView = stickyItemView
        if (stickyItemPosition == null || stickyItemView == null) {
            logBuilder?.append(", ")?.append("No Sticky Item")
            stickyItemViewOffset = null
            return
        }

        val nextStickItemPosition = findStickyItemPositionForward(parent, firstVisibleItemPosition)
        if (nextStickItemPosition == null || nextStickItemPosition <= stickyItemPosition) {
            logBuilder?.append(", ")?.append("No Next Sticky Item")
            stickyItemViewOffset = null
            return
        }

        val nextStickyItemView = parent.layoutManager?.findViewByPosition(nextStickItemPosition)
        if (nextStickyItemView == null) {
            logBuilder?.append(", ")?.append("No Next Sticky Item View")
            stickyItemViewOffset = null
            return
        }

        val nextStickyViewTop = nextStickyItemView.top
        val stickyItemViewHeight = stickyItemView.height
        stickyItemViewOffset = if (nextStickyViewTop in 0..stickyItemViewHeight) {
            nextStickyViewTop - stickyItemViewHeight
        } else {
            null
        }
        logBuilder?.append(", ")?.append("offset=${stickyItemViewOffset ?: 0}")
    }

    private fun drawStickyItemView(canvas: Canvas, logBuilder: StringBuilder?) {
        val stickyItemPosition = stickyItemPosition
        val stickyItemView = stickyItemView
        if (stickyItemPosition == null || stickyItemView == null) {
            logBuilder?.append(", ")?.append("No Sticky Item")
            return
        }

        canvas.save()
        val stickyItemViewOffset = stickyItemViewOffset
        if (stickyItemViewOffset != null) {
            canvas.translate(0f, stickyItemViewOffset.toFloat())
        }
        stickyItemView.draw(canvas)
        canvas.restore()

        logBuilder?.append(", ")?.append("Draw Sticky Item")
    }
}