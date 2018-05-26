package me.panpf.recycler.sticky.sample.adapter.item

import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import me.panpf.adapter.AssemblyItem
import me.panpf.adapter.AssemblyItemFactory
import me.panpf.adapter.ktx.bindView
import me.panpf.recycler.sticky.sample.R
import me.panpf.recycler.sticky.sample.bean.AppInfo
import me.panpf.sketch.SketchImageView
import me.panpf.sketch.uri.AppIconUriModel

class AppGridItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<AppInfo>(itemLayoutId, parent) {

    private val iconImageView: SketchImageView by bindView(R.id.gridAppItem_iconImage)
    private val nameTextView: TextView by bindView(R.id.gridAppItem_nameText)

    override fun onSetData(i: Int, appInfo: AppInfo?) {
        appInfo ?: return

        iconImageView.displayImage(AppIconUriModel.makeUri(appInfo.packageName, appInfo.versionCode))
        nameTextView.text = appInfo.name
    }

    class Factory : AssemblyItemFactory<AppInfo>() {

        init {
            setOnItemClickListener { context, _, _, _, data ->
                val launchIntent = context.packageManager.getLaunchIntentForPackage(data?.packageName)
                if (launchIntent != null) {
                    context.startActivity(launchIntent)
                } else {
                    Toast.makeText(context, "无法打开 ${data?.name}", Toast.LENGTH_LONG).show()
                }
            }
        }

        override fun match(data: Any?): Boolean {
            return data is AppInfo
        }

        override fun createAssemblyItem(viewGroup: ViewGroup): AppGridItem {
            return AppGridItem(R.layout.grid_item_app, viewGroup)
        }
    }
}
