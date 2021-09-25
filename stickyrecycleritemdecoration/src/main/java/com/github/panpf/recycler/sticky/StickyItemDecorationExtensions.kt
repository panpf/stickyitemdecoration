package com.github.panpf.recycler.sticky

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addStickyItemDecorationWithPosition(
    positionList: List<Int>
) {
    addItemDecoration(
        StickyItemDecoration.Builder().apply {
            position(positionList)
        }.build()
    )
}

fun RecyclerView.addStickyItemDecorationWithPosition(
    vararg positionArray: Int
) {
    addItemDecoration(
        StickyItemDecoration.Builder().apply {
            position(positionArray.toList())
        }.build()
    )
}

fun RecyclerView.addStickyItemDecorationWithItemType(
    itemTypeList: List<Int>
) {
    addItemDecoration(
        StickyItemDecoration.Builder().apply {
            itemType(itemTypeList)
        }.build()
    )
}

fun RecyclerView.addStickyItemDecorationWithItemType(
    vararg itemTypeArray: Int
) {
    addItemDecoration(
        StickyItemDecoration.Builder().apply {
            itemType(itemTypeArray.toList())
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