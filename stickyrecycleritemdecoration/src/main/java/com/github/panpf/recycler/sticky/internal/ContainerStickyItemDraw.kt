package com.github.panpf.recycler.sticky.internal

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.recycler.sticky.BaseStickyItemDecoration

class ContainerStickyItemDraw(
    baseStickyItemDecoration: BaseStickyItemDecoration,
    private val stickyItemContainer: ViewGroup
) : BaseStickyItemDraw(baseStickyItemDecoration) {

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = getAdapter(parent)?.takeIf { it.itemCount > 0 } ?: return
        val firstVisibleItemPosition = findFirstVisibleItemPosition(parent) ?: return

        val logBuilder: StringBuilder? = StringBuilder()
        logBuilder?.append("firstVisibleItemPosition: $firstVisibleItemPosition")

        logBuilder?.apply {
            Log.d("ContainerStickyItemDraw", "onDraw. $this")
        }
    }


//    private fun showStickyItemView(
//        canvas: Canvas,
//        parent: RecyclerView,
//        firstVisibleItemPosition: Int,
//        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
//    ) {
//        val oldStickItemPosition = stickyItemPosition
//        val newStickItemPosition = findStickyItemPositionBackward(firstVisibleItemPosition)
//        if (newStickItemPosition != -1) {
//            if (newStickItemPosition != oldStickItemPosition) {
//                val stickyItemType = adapter.getItemViewType(newStickItemPosition)
//                val stickyItemViewHolder = viewHolderCachePool[stickyItemType]
//                    ?: adapter.createViewHolder(parent, stickyItemType)
//                        .apply {
//                            viewHolderCachePool.put(stickyItemType, this)
//                        }
//                adapter.bindViewHolder(stickyItemViewHolder, newStickItemPosition)
//                val itemView = stickyItemViewHolder.itemView
//                val itemLayoutParams = itemView.layoutParams
//                val itemWidthMeasureSpec = ViewGroup.getChildMeasureSpec(
//                    View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.AT_MOST),
//                    parent.paddingLeft + parent.paddingRight,
//                    itemLayoutParams.width
//                )
//                val itemHeightMeasureSpec = ViewGroup.getChildMeasureSpec(
//                    View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.AT_MOST),
//                    parent.paddingTop + parent.paddingBottom,
//                    itemLayoutParams.height
//                )
//                itemView.measure(itemWidthMeasureSpec, itemHeightMeasureSpec)
//
//                itemView.layout(
//                    parent.paddingLeft,
//                    parent.paddingTop,
//                    parent.paddingLeft + itemView.measuredWidth,
//                    parent.paddingTop + itemView.measuredHeight
//                )
//                // 要不停的绘制
////                println("StickyItemDecoration", "onDraw")
//                canvas.save()
//                itemView.draw(canvas)
//                canvas.restore()
//
////                stickyItemContainer.apply {
////                    if (childCount > 0) {
////                        removeAllViews()
////                    }
////                    addView(stickyItemViewHolder.itemView)
////                    visibility = View.VISIBLE
////                }
//
//                stickyItemPosition = newStickItemPosition
//            }
//        } else {
//            stickyItemPosition = -1
////            stickyItemContainer.apply {
////                if (childCount > 0) {
////                    removeAllViews()
////                }
////                visibility = View.INVISIBLE
////            }
//        }
//    }

//    /**
//     * 往上滑动时用新的 sticky item view 一点一点顶掉旧的 sticky item view
//     */
//    private fun offsetStickyItemView(
//        parent: RecyclerView,
//        firstVisibleItemPosition: Int,
//        logBuilder: StringBuilder?
//    ) {
//        /*
//         * 当前有 sticky item 需要显示就从当前位置往前找下一个 sticky item，找到并且已经跟当前 sticky item 的位置重叠了，就往上顶当前 sticky item
//         */
//        var offset = -99999
//        var nextStickyViewTop = -99999
//        var oldStickyViewTop = -99999
//        var stickyContainerHeight = -1
//        var nextStickItemPosition = -1
//        if (stickyItemPosition != -1) {
//            if (!disabledScrollStickyHeader) {
//                offset = 0
//                stickyContainerHeight = stickyItemContainer.height
//                nextStickItemPosition =
//                    findStickyItemPositionForward(parent, firstVisibleItemPosition)
//                if (nextStickItemPosition > stickyItemPosition) {
//                    val nextStickyItemView =
//                        parent.layoutManager?.findViewByPosition(nextStickItemPosition)
//                    if (nextStickyItemView != null) {
//                        nextStickyViewTop = nextStickyItemView.top
//                        if (nextStickyViewTop >= 0 && nextStickyViewTop <= stickyContainerHeight) {
//                            offset = nextStickyViewTop - stickyContainerHeight
//                        }
//                    }
//                }
//                if (stickyItemContainer.childCount > 0) {
//                    val stickyView = stickyItemContainer.getChildAt(0)
//                    oldStickyViewTop = stickyView.top
//                    ViewCompat.offsetTopAndBottom(stickyView, offset - oldStickyViewTop)
//                }
//                logBuilder?.apply {
//                    if (isNotEmpty()) append(", ")
//                    append("nextStickItemPosition: $nextStickItemPosition")
//                    if (isNotEmpty()) append(", ")
//                    append("offset: $offset")
//                }
//            }
//            stickyItemContainer.visibility = View.VISIBLE
//        } else {
//            if (stickyItemContainer.childCount > 0) {
//                stickyItemContainer.removeAllViews()
//            }
//            stickyItemContainer.visibility = View.INVISIBLE
//        }
//    }

    override fun reset() {
        super.reset()
        if (stickyItemContainer.childCount > 0) {
            stickyItemContainer.removeAllViews()
        }
        stickyItemContainer.visibility = View.INVISIBLE
    }
}