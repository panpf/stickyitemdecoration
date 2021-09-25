package com.github.panpf.recycler.sticky

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addSimpleStickyItemDecoration(
    stickyItemContainer: ViewGroup,
    config: SimpleStickyItemDecoration.Builder.() -> Unit
) {
    addItemDecoration(
        SimpleStickyItemDecoration.Builder(stickyItemContainer).apply {
            config(this)
        }.build()
    )
}