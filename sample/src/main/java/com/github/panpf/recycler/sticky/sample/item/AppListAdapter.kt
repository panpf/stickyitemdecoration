package com.github.panpf.recycler.sticky.sample.item

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.internal.RecyclerViewHolderWrapper
import com.github.panpf.recycler.sticky.sample.bean.AppInfo
import com.github.panpf.recycler.sticky.sample.bean.AppsOverview
import com.github.panpf.recycler.sticky.sample.bean.ListSeparator

class AppListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataList: List<Any>? = null

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataList!![position]) {
            is AppsOverview -> 0
            is ListSeparator -> 1
            is AppInfo -> 2
            else -> throw IllegalArgumentException("Unsupported data type: ${dataList!![position].javaClass}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val item = when (viewType) {
            0 -> AppsOverviewItemFactory().dispatchCreateItem(parent)
            1 -> ListSeparatorItemFactory(null).dispatchCreateItem(parent)
            2 -> AppItemFactory().dispatchCreateItem(parent)
            else -> throw IllegalArgumentException("Unsupported view type: $viewType")
        }
        return RecyclerViewHolderWrapper(item)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecyclerViewHolderWrapper<Any>).wrappedItem.dispatchBindData(
            position,
            position,
            dataList!![position]
        )
    }

    fun submitList(dataList: List<Any>?) {
        this.dataList = dataList
        notifyDataSetChanged()
    }
}