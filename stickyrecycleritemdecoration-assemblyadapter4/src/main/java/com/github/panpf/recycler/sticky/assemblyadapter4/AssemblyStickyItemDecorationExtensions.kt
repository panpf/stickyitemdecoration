package com.github.panpf.recycler.sticky.assemblyadapter4

import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.ItemFactory
import kotlin.reflect.KClass

fun RecyclerView.addAssemblyStickyItemDecorationWithPosition(
    positionList: List<Int>,
    config: (AssemblyStickyItemDecoration.Builder.() -> Unit)? = null
) {
    addItemDecoration(
        AssemblyStickyItemDecoration.Builder().apply {
            position(positionList)
        }.build()
    )
}

fun RecyclerView.addAssemblyStickyItemDecorationWithPosition(
    vararg positionArray: Int,
    config: (AssemblyStickyItemDecoration.Builder.() -> Unit)? = null
) {
    addItemDecoration(
        AssemblyStickyItemDecoration.Builder().apply {
            position(positionArray.toList())
            config?.invoke(this)
        }.build()
    )
}

fun RecyclerView.addAssemblyStickyItemDecorationWithItemType(
    itemTypeList: List<Int>,
    config: (AssemblyStickyItemDecoration.Builder.() -> Unit)? = null
) {
    addItemDecoration(
        AssemblyStickyItemDecoration.Builder().apply {
            itemType(itemTypeList)
            config?.invoke(this)
        }.build()
    )
}

fun RecyclerView.addAssemblyStickyItemDecorationWithItemType(
    vararg itemTypeArray: Int,
    config: (AssemblyStickyItemDecoration.Builder.() -> Unit)? = null
) {
    addItemDecoration(
        AssemblyStickyItemDecoration.Builder().apply {
            itemType(itemTypeArray.toList())
            config?.invoke(this)
        }.build()
    )
}

fun RecyclerView.addAssemblyStickyItemDecorationWithItemFactory(
    itemFactoryClassList: List<KClass<out ItemFactory<out Any>>>,
    config: (AssemblyStickyItemDecoration.Builder.() -> Unit)? = null
) {
    addItemDecoration(
        AssemblyStickyItemDecoration.Builder().apply {
            itemFactory(itemFactoryClassList)
            config?.invoke(this)
        }.build()
    )
}

fun RecyclerView.addAssemblyStickyItemDecorationWithItemFactory(
    vararg itemFactoryClassArray: KClass<out ItemFactory<out Any>>,
    config: (AssemblyStickyItemDecoration.Builder.() -> Unit)? = null
) {
    addItemDecoration(
        AssemblyStickyItemDecoration.Builder().apply {
            itemFactory(itemFactoryClassArray.toList())
            config?.invoke(this)
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