package com.github.panpf.recycler.sticky.assemblyadapter4

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.ItemFactory
import kotlin.reflect.KClass

fun RecyclerView.addAssemblyStickyItemDecoration(
    stickyItemContainer: ViewGroup,
    vararg itemFactoryClass: KClass<out ItemFactory<out Any>>
) {
    addItemDecoration(AssemblyStickyItemDecoration(stickyItemContainer, itemFactoryClass.toList()))
}

fun RecyclerView.addAssemblyStickyItemDecoration(
    stickyItemContainer: ViewGroup,
    stickyItemFactoryList: List<KClass<out ItemFactory<out Any>>>
) {
    addItemDecoration(AssemblyStickyItemDecoration(stickyItemContainer, stickyItemFactoryList))
}

fun RecyclerView.addAssemblyStickyItemDecoration(
    stickyItemContainer: ViewGroup,
    config: AssemblyStickyItemDecoration.Builder.() -> Unit
) {
    addItemDecoration(
        AssemblyStickyItemDecoration.Builder(stickyItemContainer).apply {
            config(this)
        }.build()
    )
}