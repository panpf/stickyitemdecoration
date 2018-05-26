package me.panpf.recycler.sticky.sample.adapter.item

import android.view.ViewGroup
import android.widget.TextView
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.recycler.sticky.sample.R

class HeaderItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<String>(itemLayoutId, parent) {

    override fun onSetData(position: Int, s: String?) {
        (itemView as TextView).text = s
    }

    class Factory : AssemblyItemFactory<String>() {
        override fun match(data: Any?): Boolean {
            return data is String
        }

        override fun createAssemblyItem(parent: ViewGroup): HeaderItem {
            return HeaderItem(R.layout.list_item_header, parent)
        }
    }
}
