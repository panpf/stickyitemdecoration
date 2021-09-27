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

import androidx.recyclerview.widget.RecyclerView

/**
 * Add a [StickyItemDecoration] to [RecyclerView] by specifying the position
 */
fun RecyclerView.addStickyItemDecorationWithPosition(
    positionList: List<Int>,
    config: (StickyItemDecoration.Builder.() -> Unit)? = null
) {
    addItemDecoration(
        StickyItemDecoration.Builder().apply {
            position(positionList)
            config?.invoke(this)
        }.build()
    )
}

/**
 * Add a [StickyItemDecoration] to [RecyclerView] by specifying the position
 */
fun RecyclerView.addStickyItemDecorationWithPosition(
    vararg positionArray: Int,
    config: (StickyItemDecoration.Builder.() -> Unit)? = null
) {
    addItemDecoration(
        StickyItemDecoration.Builder().apply {
            position(positionArray.toList())
            config?.invoke(this)
        }.build()
    )
}

/**
 * Add a [StickyItemDecoration] to [RecyclerView] by specifying the type
 */
fun RecyclerView.addStickyItemDecorationWithItemType(
    itemTypeList: List<Int>,
    config: (StickyItemDecoration.Builder.() -> Unit)? = null
) {
    addItemDecoration(
        StickyItemDecoration.Builder().apply {
            itemType(itemTypeList)
            config?.invoke(this)
        }.build()
    )
}

/**
 * Add a [StickyItemDecoration] to [RecyclerView] by specifying the type
 */
fun RecyclerView.addStickyItemDecorationWithItemType(
    vararg itemTypeArray: Int,
    config: (StickyItemDecoration.Builder.() -> Unit)? = null
) {
    addItemDecoration(
        StickyItemDecoration.Builder().apply {
            itemType(itemTypeArray.toList())
            config?.invoke(this)
        }.build()
    )
}

/**
 * Add a [StickyItemDecoration] to [RecyclerView]
 */
fun RecyclerView.addStickyItemDecoration(
    config: StickyItemDecoration.Builder.() -> Unit
) {
    addItemDecoration(
        StickyItemDecoration.Builder().apply {
            config(this)
        }.build()
    )
}