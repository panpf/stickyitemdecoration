package com.github.panpf.recycler.sticky.assemblyadapter4

import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.ItemFactory
import kotlin.reflect.KClass

fun RecyclerView.addAssemblyStickyItemDecorationWithPosition(
    positionList: List<Int>
) {
    addItemDecoration(
        AssemblyStickyItemDecoration.Builder().apply {
            position(positionList)
        }.build()
    )
}

fun RecyclerView.addAssemblyStickyItemDecorationWithPosition(
    vararg positionArray: Int
) {
    addItemDecoration(
        AssemblyStickyItemDecoration.Builder().apply {
            position(positionArray.toList())
        }.build()
    )
}

fun RecyclerView.addAssemblyStickyItemDecorationWithItemType(
    itemTypeList: List<Int>
) {
    addItemDecoration(
        AssemblyStickyItemDecoration.Builder().apply {
            itemType(itemTypeList)
        }.build()
    )
}

fun RecyclerView.addAssemblyStickyItemDecorationWithItemType(
    vararg itemTypeArray: Int
) {
    addItemDecoration(
        AssemblyStickyItemDecoration.Builder().apply {
            itemType(itemTypeArray.toList())
        }.build()
    )
}

fun RecyclerView.addAssemblyStickyItemDecorationWithItemFactory(
    itemFactoryClassList: List<KClass<out ItemFactory<out Any>>>
) {
    addItemDecoration(
        AssemblyStickyItemDecoration.Builder().apply {
            itemFactory(itemFactoryClassList)
        }.build()
    )
}

fun RecyclerView.addAssemblyStickyItemDecorationWithItemFactory(
    vararg itemFactoryClassArray: KClass<out ItemFactory<out Any>>
) {
    addItemDecoration(
        AssemblyStickyItemDecoration.Builder().apply {
            itemFactory(itemFactoryClassArray.toList())
        }.build()
    )
}

fun RecyclerView.addAssemblyStickyItemDecoration(
    config: AssemblyStickyItemDecoration.Builder.() -> Unit
) {
    addItemDecoration(
        AssemblyStickyItemDecoration.Builder().apply {
            config(this)
        }.build()
    )
}