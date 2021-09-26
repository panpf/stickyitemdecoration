package com.github.panpf.recycler.sticky

import androidx.recyclerview.widget.RecyclerView

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

fun RecyclerView.addStickyItemDecoration(
    config: StickyItemDecoration.Builder.() -> Unit
) {
    addItemDecoration(
        StickyItemDecoration.Builder().apply {
            config(this)
        }.build()
    )
}