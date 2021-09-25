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
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.recycler.sticky.internal.ContainerStickyItemDraw
import com.github.panpf.recycler.sticky.internal.StickyItemDraw

// todo 支持横向
abstract class BaseStickyItemDecoration(
    stickyItemContainer: ViewGroup?
) : RecyclerView.ItemDecoration() {

    private val sticky = if (stickyItemContainer != null) {
        ContainerStickyItemDraw(this, stickyItemContainer)
    } else {
        StickyItemDraw(this)
    }

    /**
     * 禁止滑动过程中下一个 sticky header 往上顶当前 sticky header 的效果
     */
    var disabledScrollStickyHeader
        get() = sticky.disabledScrollStickyHeader
        set(value) {
            sticky.disabledScrollStickyHeader = value
        }

    /**
     * 滑动过程中 sticky header 显示时隐藏列表中的 sticky item
     */
    var invisibleStickyItemInList
        get() = sticky.invisibleStickyItemInList
        set(value) {
            sticky.invisibleStickyItemInList = value
        }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        sticky.onDrawOver(canvas, parent, state)
    }

    abstract fun isStickyItemByPosition(adapter: RecyclerView.Adapter<*>, position: Int): Boolean
}