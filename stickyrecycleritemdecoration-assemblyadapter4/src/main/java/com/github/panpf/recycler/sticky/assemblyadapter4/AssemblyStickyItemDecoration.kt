package com.github.panpf.recycler.sticky.assemblyadapter4

import android.util.SparseBooleanArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterLocalHelper
import com.github.panpf.recycler.sticky.StickyItemJudge
import com.github.panpf.recycler.sticky.StickyItemDecoration
import kotlin.reflect.KClass

class AssemblyStickyItemDecoration(
    stickyItemContainer: ViewGroup,
    positionArray: SparseBooleanArray?,
    itemTypeArray: SparseBooleanArray?,
    stickyItemFactoryList: List<KClass<out ItemFactory<out Any>>>?,
) : StickyItemDecoration(
    AssemblyStickyItemJudge(positionArray, itemTypeArray, stickyItemFactoryList?.map { it.java }),
    stickyItemContainer
) {

    constructor(
        stickyItemContainer: ViewGroup,
        stickyItemFactoryList: List<KClass<out ItemFactory<out Any>>>
    ) : this(stickyItemContainer, null, null, stickyItemFactoryList)

    constructor(
        stickyItemContainer: ViewGroup,
        vararg itemFactoryClass: KClass<out ItemFactory<out Any>>
    ) : this(stickyItemContainer, null, null, itemFactoryClass.toList())

    class Builder(private val stickyItemContainer: ViewGroup) {

        private var stickyItemFactoryList: List<KClass<out ItemFactory<out Any>>>? = null
        private val itemTypeArray = SparseBooleanArray()
        private val positionArray = SparseBooleanArray()

        fun position(vararg positions: Int): Builder {
            positions.forEach { position ->
                positionArray.put(position, true)
            }
            return this
        }

        fun position(positions: List<Int>): Builder {
            positions.forEach { position ->
                positionArray.put(position, true)
            }
            return this
        }

        fun itemType(vararg itemTypes: Int): Builder {
            itemTypes.forEach { itemType ->
                itemTypeArray.put(itemType, true)
            }
            return this
        }

        fun itemType(itemTypes: List<Int>): Builder {
            itemTypes.forEach { itemType ->
                itemTypeArray.put(itemType, true)
            }
            return this
        }

        fun itemFactory(vararg itemFactory: KClass<out ItemFactory<out Any>>): Builder {
            this.stickyItemFactoryList = itemFactory.toList()
            return this
        }

        fun itemFactory(itemFactoryList: List<KClass<out ItemFactory<out Any>>>): Builder {
            this.stickyItemFactoryList = itemFactoryList
            return this
        }

        fun build(): AssemblyStickyItemDecoration {
            return AssemblyStickyItemDecoration(
                stickyItemContainer,
                if (positionArray.size() > 0) positionArray else null,
                if (itemTypeArray.size() > 0) itemTypeArray else null,
                if (stickyItemFactoryList?.isNotEmpty() == true) stickyItemFactoryList else null,
            )
        }
    }

    private class AssemblyStickyItemJudge(
        private val positionArray: SparseBooleanArray?,
        private val itemTypeArray: SparseBooleanArray?,
        private val stickyItemFactoryList: List<Class<out ItemFactory<out Any>>>?,
    ) : StickyItemJudge {

        private val concatAdapterLocalHelper = ConcatAdapterLocalHelper()

        override fun isStickyItemByPosition(
            adapter: RecyclerView.Adapter<*>,
            position: Int
        ): Boolean {
            if (positionArray?.get(position) == true) {
                return true
            }

            var localAdapter: RecyclerView.Adapter<*>? = null
            var localPosition: Int? = null
            if (itemTypeArray != null || stickyItemFactoryList != null) {
                val local = concatAdapterLocalHelper.findLocalAdapterAndPosition(adapter, position)
                localAdapter = local.first
                localPosition = local.second
            }
            if (itemTypeArray?.get(localAdapter!!.getItemViewType(localPosition!!)) == true) {
                return true
            }

            if (stickyItemFactoryList != null) {
                val adapter1 = localAdapter!!
                val itemFactoryClass: Class<*>? = if (adapter1 is AssemblyAdapter<*, *>) {
                    adapter1.getItemFactoryByPosition(localPosition!!).javaClass
                } else {
                    null
                }
                if (itemFactoryClass != null && stickyItemFactoryList.contains(itemFactoryClass)) {
                    return true
                }
            }

            return false
        }
    }
}