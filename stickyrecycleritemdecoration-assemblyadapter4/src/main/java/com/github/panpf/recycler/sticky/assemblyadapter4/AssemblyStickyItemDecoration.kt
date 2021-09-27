package com.github.panpf.recycler.sticky.assemblyadapter4

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.recycler.sticky.StickyItemDecoration
import kotlin.reflect.KClass

class AssemblyStickyItemDecoration private constructor(
    stickyItemPositionList: List<Int>? = null,
    stickyItemTypeList: List<Int>? = null,
    stickyItemFactoryKClassList: List<KClass<out ItemFactory<out Any>>>?,
    stickyItemContainer: ViewGroup? = null,
) : StickyItemDecoration(stickyItemPositionList, stickyItemTypeList, stickyItemContainer) {

    private val itemFactoryClassList: List<Class<out ItemFactory<out Any>>>? =
        stickyItemFactoryKClassList?.takeIf { it.isNotEmpty() }?.map { it.java }

    override fun isStickyItemByPosition(
        adapter: RecyclerView.Adapter<*>,
        position: Int
    ): Boolean {
        val isStickyItem = super.isStickyItemByPosition(adapter, position)
        if (isStickyItem) {
            return true
        }

        if (itemFactoryClassList != null) {
            val (localAdapter, localPosition) = findLocalAdapterAndPosition(adapter, position)
            val itemFactoryClass: Class<*>? = if (localAdapter is AssemblyAdapter<*, *>) {
                localAdapter.getItemFactoryByPosition(localPosition).javaClass
            } else {
                null
            }
            if (itemFactoryClass != null && itemFactoryClassList.contains(itemFactoryClass)) {
                return true
            }
        }

        return false
    }

    class Builder : StickyItemDecoration.Builder() {

        private var stickyItemFactoryKClassList: List<KClass<out ItemFactory<out Any>>>? = null

        override fun position(vararg positions: Int): Builder {
            super.position(*positions)
            return this
        }

        override fun position(positions: List<Int>): Builder {
            super.position(positions)
            return this
        }

        override fun itemType(vararg itemTypes: Int): Builder {
            super.itemType(*itemTypes)
            return this
        }

        override fun itemType(itemTypes: List<Int>): Builder {
            super.itemType(itemTypes)
            return this
        }

        override fun showInContainer(stickyItemContainer: ViewGroup): Builder {
            super.showInContainer(stickyItemContainer)
            return this
        }

        fun itemFactory(vararg itemFactory: KClass<out ItemFactory<out Any>>): Builder {
            this.stickyItemFactoryKClassList = itemFactory.toList()
            return this
        }

        fun itemFactory(itemFactoryList: List<KClass<out ItemFactory<out Any>>>): Builder {
            this.stickyItemFactoryKClassList = itemFactoryList
            return this
        }

        override fun build(): AssemblyStickyItemDecoration {
            return AssemblyStickyItemDecoration(
                stickyItemPositionList,
                stickyItemTypeList,
                stickyItemFactoryKClassList,
                stickyItemContainer,
            )
        }
    }
}