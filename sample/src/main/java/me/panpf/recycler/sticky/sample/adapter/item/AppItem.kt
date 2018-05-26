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

class AppItem(itemLayoutId: Int, parent: ViewGroup) : AssemblyItem<AppInfo>(itemLayoutId, parent) {

    private val iconImageView: SketchImageView by bindView(R.id.appItem_iconImage)
    private val nameTextView: TextView by bindView(R.id.appItem_nameText)
    private val versionTextView: TextView by bindView(R.id.appItem_versionText)

    override fun onSetData(i: Int, appInfo: AppInfo?) {
        appInfo ?: return

        iconImageView.displayImage(AppIconUriModel.makeUri(appInfo.packageName, appInfo.versionCode))
        nameTextView.text = appInfo.name
        versionTextView.text = versionTextView.resources.getString(R.string.app_version, appInfo.versionName)
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

        override fun createAssemblyItem(viewGroup: ViewGroup): AppItem {
            return AppItem(R.layout.list_item_app, viewGroup)
        }
    }
}
