package me.panpf.recycler.sticky.sample.adapter.item

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.ktx.bindView
import me.panpf.recycler.sticky.sample.R
import me.panpf.recycler.sticky.sample.adapter.AssemblyStickyRecyclerAdapter
import me.panpf.recycler.sticky.sample.bean.AppHeader
import me.panpf.recycler.sticky.sample.widget.AttachedConstraintLayout

class AppHeaderItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<AppHeader>(itemLayoutId, parent) {

    private val rootLayout: AttachedConstraintLayout by bindView(R.id.appHeaderItem_root)
    private val titleTextView: TextView by bindView(R.id.appHeaderItem_titleText)
    private val moreTextView: TextView by bindView(R.id.appHeaderItem_moreText)
    private val expandTextView: TextView by bindView(R.id.appHeaderItem_expandText)

    val appHeaderStatusListener = object : AppHeader.StatusListener {
        override fun onExpandChanged() {
            data?.let { setExpandStatus(it) }
        }
    }

    override fun onConfigViews(context: Context) {
        super.onConfigViews(context)

        expandTextView.setOnClickListener {
            data?.run {
                expand = !expand
                setExpandStatus(this)
            }
        }

        rootLayout.onAttachedListener = object : AttachedConstraintLayout.OnAttachedListener {
            override fun onAttachedToWindow() {
                data?.listenrs?.add(appHeaderStatusListener)
            }

            override fun onDetachedFromWindow() {
                data?.listenrs?.remove(appHeaderStatusListener)
            }
        }
    }

    override fun setData(position: Int, data: AppHeader?) {
        // 从旧的 data 里移除监听
        data?.listenrs?.remove(appHeaderStatusListener)
        super.setData(position, data)
    }

    override fun onSetData(i: Int, data: AppHeader?) {
        titleTextView.text = data?.title
        moreTextView.text = moreTextView.resources.getString(R.string.app_child_count, data?.count)
        setExpandStatus(data)

        // 注册新 data 的监听
        data?.listenrs?.add(appHeaderStatusListener)
    }

    private fun setExpandStatus(data: AppHeader?) {
        moreTextView.visibility = if (data?.expand == true) View.VISIBLE else View.GONE
        expandTextView.text = if (data?.expand == true) " | " else "－"
    }

    class Factory : AssemblyItemFactory<AppHeader>(), AssemblyStickyRecyclerAdapter.StickyItemFactory {

        override fun match(data: Any?): Boolean {
            return data is AppHeader
        }

        override fun createAssemblyItem(viewGroup: ViewGroup): AppHeaderItem {
            return AppHeaderItem(R.layout.list_item_app_list_header, viewGroup)
        }
    }
}
