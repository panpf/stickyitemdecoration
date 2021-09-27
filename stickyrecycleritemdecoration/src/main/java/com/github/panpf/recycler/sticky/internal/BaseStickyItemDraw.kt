package com.github.panpf.recycler.sticky.internal

import android.graphics.Canvas
import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.SimpleAdapterDataObserver
import com.github.panpf.recycler.sticky.StickyItemDecoration

abstract class BaseStickyItemDraw(private val baseStickyItemDecoration: StickyItemDecoration) {

    protected val viewHolderCachePool = SparseArray<RecyclerView.ViewHolder>()
    private var cacheAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    private val simpleAdapterDataObserver = SimpleAdapterDataObserver {
        reset()
    }

    private var invisibleItemView: View? = null
    private var cacheInto: IntArray? = null

    /**
     * 禁止滑动过程中下一个 sticky header 往上顶当前 sticky header 的效果
     */
    var disabledScrollUpStickyItem = false
        set(value) {
            field = value
            reset()
        }

    /**
     * 滑动过程中 sticky header 显示时隐藏列表中的 sticky item
     */
    var invisibleOriginItemWhenStickyItemShowing = false
        set(value) {
            field = value
            reset()
        }

    abstract fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State)

    /*
     * 列表中的 item 需要在 sticky item 显示的时候隐藏，划出列表的时候恢复显示
     */
    protected fun hiddenOriginItemView(
        parent: RecyclerView,
        firstVisibleItemPosition: Int,
        stickyItemPosition: Int?
    ) {
        if (!invisibleOriginItemWhenStickyItemShowing) return

        if (stickyItemPosition != null && stickyItemPosition == firstVisibleItemPosition) {
            val originStickyItemView = parent.layoutManager?.findViewByPosition(stickyItemPosition)
            // stickyItemView.getTop() == 0 时隐藏 stickyItemView 会导致 sticky header 区域闪烁一下，这是因为在 sticky header 显示出来之前隐藏了 stickyItemView
            // 因此限定 stickyItemView.getTop() < 0 也就是说 sticky item 和 sticky header 错开的时候隐藏 sticky item 可以一定程度上避免闪烁，但滑动的快了还是会闪烁一下
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

    protected fun findStickyItemPositionBackward(formPosition: Int): Int? {
        val adapter = getAdapter()
        if (adapter != null && formPosition >= 0) {
            for (position in formPosition downTo 0) {
                if (baseStickyItemDecoration.isStickyItemByPosition(adapter, position)) {
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
                    if (baseStickyItemDecoration.isStickyItemByPosition(adapter, position)) {
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
                }.minOf { it }
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
                }.minOf { it }  // todo 这里貌似要找最大的
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
                this@BaseStickyItemDraw.cacheInto = this
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