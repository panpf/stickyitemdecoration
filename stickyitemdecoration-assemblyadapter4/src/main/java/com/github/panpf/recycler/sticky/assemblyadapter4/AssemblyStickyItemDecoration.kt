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
package com.github.panpf.recycler.sticky.assemblyadapter4

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.recycler.sticky.StickyItemDecoration
import kotlin.reflect.KClass

/**
 * Support sticky item judgment based on [ItemFactory] on the basis of [StickyItemDecoration]
 */
open class AssemblyStickyItemDecoration constructor(
    stickyItemPositionList: List<Int>? = null,
    stickyItemTypeList: List<Int>? = null,
    stickyItemFactoryKClassList: List<KClass<*>>?,
    stickyItemContainer: ViewGroup? = null,
) : StickyItemDecoration(stickyItemPositionList, stickyItemTypeList, stickyItemContainer) {

    private val itemFactoryClassList: List<Class<*>>? =
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
            val itemFactoryClass = findItemFactoryClassByPosition(localAdapter, localPosition)
            if (itemFactoryClass != null && itemFactoryClassList.contains(itemFactoryClass)) {
                return true
            }
        }

        return false
    }

    open fun findItemFactoryClassByPosition(
        localAdapter: RecyclerView.Adapter<*>, localPosition: Int
    ): Class<*>? {
        return if (localAdapter is AssemblyAdapter<*, *>) {
            localAdapter.getItemFactoryByPosition(localPosition).javaClass
        } else {
            null
        }
    }

    class Builder : StickyItemDecoration.Builder() {

        private var stickyItemFactoryKClassList: List<KClass<*>>? = null

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

        override fun disabledScrollUpStickyItem(disable: Boolean): Builder {
            super.disabledScrollUpStickyItem(disable)
            return this
        }

        override fun invisibleOriginItemWhenStickyItemShowing(open: Boolean): Builder {
            super.invisibleOriginItemWhenStickyItemShowing(open)
            return this
        }

        /**
         * Set the item at the specified [ItemFactory] to always be displayed at the top of the RecyclerView
         */
        fun itemFactory(vararg itemFactory: KClass<*>): Builder {
            this.stickyItemFactoryKClassList = itemFactory.toList()
            return this
        }

        /**
         * Set the item at the specified [ItemFactory] to always be displayed at the top of the RecyclerView
         */
        fun itemFactory(itemFactoryList: List<KClass<*>>): Builder {
            this.stickyItemFactoryKClassList = itemFactoryList
            return this
        }

        override fun build(): AssemblyStickyItemDecoration {
            return AssemblyStickyItemDecoration(
                stickyItemPositionList,
                stickyItemTypeList,
                stickyItemFactoryKClassList,
                stickyItemContainer,
            ).apply {
                disabledScrollUpStickyItem = this@Builder.disabledScrollUpStickyItem
                invisibleOriginItemWhenStickyItemShowing =
                    this@Builder.invisibleOriginItemWhenStickyItemShowing
            }
        }
    }
}