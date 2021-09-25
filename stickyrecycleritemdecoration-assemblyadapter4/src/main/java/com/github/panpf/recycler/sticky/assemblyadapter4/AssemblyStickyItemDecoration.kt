package com.github.panpf.recycler.sticky.assemblyadapter4

import android.util.SparseBooleanArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterLocalHelper
import com.github.panpf.recycler.sticky.BaseStickyItemDecoration
import kotlin.reflect.KClass

class AssemblyStickyItemDecoration private constructor(
    stickyItemPositionList: List<Int>? = null,
    stickyItemTypeList: List<Int>? = null,
    stickyItemFactoryKClassList: List<KClass<out ItemFactory<out Any>>>?,
    stickyItemContainer: ViewGroup? = null,
) : BaseStickyItemDecoration(stickyItemContainer) {

    private val positionArray: SparseBooleanArray? =
        if (stickyItemPositionList?.isNotEmpty() == true) {
            SparseBooleanArray().apply {
                stickyItemPositionList.forEach { position -> put(position, true) }
            }
        } else {
            null
        }
    private val itemTypeArray: SparseBooleanArray? = if (stickyItemTypeList?.isNotEmpty() == true) {
        SparseBooleanArray().apply {
            stickyItemTypeList.forEach { itemType -> put(itemType, true) }
        }
    } else {
        null
    }
    private val itemFactoryClassList: List<Class<out ItemFactory<out Any>>>? =
        stickyItemFactoryKClassList?.takeIf { it.isNotEmpty() }?.map { it.java }
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
        if (itemTypeArray != null || itemFactoryClassList != null) {
            val local = concatAdapterLocalHelper.findLocalAdapterAndPosition(adapter, position)
            localAdapter = local.first
            localPosition = local.second
        }
        if (itemTypeArray?.get(localAdapter!!.getItemViewType(localPosition!!)) == true) {
            return true
        }

        if (itemFactoryClassList != null) {
            val adapter1 = localAdapter!!
            val itemFactoryClass: Class<*>? = if (adapter1 is AssemblyAdapter<*, *>) {
                adapter1.getItemFactoryByPosition(localPosition!!).javaClass
            } else {
                null
            }
            if (itemFactoryClass != null && itemFactoryClassList.contains(itemFactoryClass)) {
                return true
            }
        }

        return false
    }

    class Builder {

        private var stickyItemPositionList: List<Int>? = null
        private var stickyItemTypeList: List<Int>? = null
        private var stickyItemFactoryKClassList: List<KClass<out ItemFactory<out Any>>>? = null
        private var stickyItemContainer: ViewGroup? = null

        fun position(vararg positions: Int): Builder {
            this.stickyItemPositionList = positions.toList()
            return this
        }

        fun position(positions: List<Int>): Builder {
            this.stickyItemPositionList = positions
            return this
        }

        fun itemType(vararg itemTypes: Int): Builder {
            this.stickyItemTypeList = itemTypes.toList()
            return this
        }

        fun itemType(itemTypes: List<Int>): Builder {
            this.stickyItemTypeList = itemTypes
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

        fun showInContainer(stickyItemContainer: ViewGroup): Builder {
            this.stickyItemContainer = stickyItemContainer
            return this
        }

        fun build(): AssemblyStickyItemDecoration {
            return AssemblyStickyItemDecoration(
                stickyItemPositionList,
                stickyItemTypeList,
                stickyItemFactoryKClassList,
                stickyItemContainer,
            )
        }
    }
}