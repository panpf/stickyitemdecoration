package me.panpf.recycler.sticky.sample

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.format.Formatter
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import me.panpf.adapter.ItemHolder
import me.panpf.adapter.recycler.AssemblyGridLayoutManager
import me.panpf.recycler.sticky.StickyRecyclerItemDecoration
import me.panpf.recycler.sticky.sample.adapter.AssemblyStickyRecyclerAdapter
import me.panpf.recycler.sticky.sample.adapter.item.AppGridItem
import me.panpf.recycler.sticky.sample.adapter.item.AppHeaderItem
import me.panpf.recycler.sticky.sample.adapter.item.AppItem
import me.panpf.recycler.sticky.sample.adapter.item.HeaderItem
import me.panpf.recycler.sticky.sample.bean.AppHeader
import me.panpf.recycler.sticky.sample.bean.AppInfo
import me.panpf.recycler.sticky.sample.util.HanziToPinyin
import java.io.File

class MainActivity : AppCompatActivity() {

    private val appsViewModel: AppsViewModel by lazy { ViewModelProviders.of(this).get(AppsViewModel::class.java) }
    private var gridLayout = false

    var gridCountHeaderHolder: ItemHolder<String>? = null
    val gridLayoutManager by lazy { AssemblyGridLayoutManager(requireNotNull(baseContext), 3, main_recyclerView) }
    val gridAdapter = AssemblyStickyRecyclerAdapter().apply {
        gridCountHeaderHolder = addHeaderItem(HeaderItem.Factory().setSpanSize(3), null)
        addItemFactory(AppGridItem.Factory())
        addItemFactory(AppHeaderItem.Factory().setSpanSize(3))
    }

    var listCountHeaderHolder: ItemHolder<String>? = null
    val listLayoutManager by lazy { LinearLayoutManager(requireNotNull(baseContext)) }
    val listAdapter = AssemblyStickyRecyclerAdapter().apply {
        listCountHeaderHolder = addHeaderItem(HeaderItem.Factory(), null)
        addItemFactory(AppItem.Factory())
        addItemFactory(AppHeaderItem.Factory())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        StickyRecyclerItemDecoration.DEBUG = BuildConfig.DEBUG
        main_recyclerView.addItemDecoration(StickyRecyclerItemDecoration(main_stickyContainerLayout))
//        main_recyclerView.addItemDecoration(StickyRecyclerItemDecoration(main_stickyContainerLayout).setDisabledScrollStickyHeader(true))
//        main_recyclerView.addItemDecoration(StickyRecyclerItemDecoration(main_stickyContainerLayout).setInvisibleStickyItemInList(true))
//        main_recyclerView.addItemDecoration(StickyRecyclerItemDecoration(main_stickyContainerLayout).setDisabledScrollStickyHeader(true).setDisabledInvisibleStickyItemInList(true))

        applyLayout()

        appsViewModel.apps.observe(this, android.arch.lifecycle.Observer {
            it ?: return@Observer

            val dataList = ArrayList<Any>()
            var lastInitials: String? = null
            var lastHeader: AppHeader? = null
            it.forEach {
                val currentInitials = it.sortName.substring(0, 1)
                if (lastInitials == null) {
                    val newHeader = AppHeader(currentInitials)
                    dataList.add(newHeader)

                    lastHeader = newHeader
                    lastInitials = currentInitials
                } else if (currentInitials != lastInitials) {
                    val newHeader = AppHeader(currentInitials)
                    dataList.add(newHeader)

                    lastHeader = newHeader
                    lastInitials = currentInitials
                }

                lastHeader?.let { it.count++ }
                dataList.add(it)
            }

            val countTitle = resources.getString(R.string.app_count_title, it.size)
            gridCountHeaderHolder?.data = countTitle
            listCountHeaderHolder?.data = countTitle

            gridAdapter.dataList = dataList
            listAdapter.dataList = dataList
            main_recyclerView.scheduleLayoutAnimation()
        })

        appsViewModel.load()
    }

    fun applyLayout() {
        main_recyclerView.layoutManager = if (gridLayout) gridLayoutManager else listLayoutManager
        main_recyclerView.adapter = if (gridLayout) gridAdapter else listAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.menu_layout)?.setTitle(if (gridLayout) R.string.menu_layout_list else R.string.menu_layout_grid)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_github) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/panpf/StickyRecyclerItemDecoration")))
        } else if (item?.itemId == R.id.menu_layout) {
            gridLayout = !gridLayout
            applyLayout()
            invalidateOptionsMenu()
        }
        return super.onOptionsItemSelected(item)
    }
}

class AppsViewModel(application: Application) : AndroidViewModel(application) {
    val apps = MutableLiveData<List<AppInfo>>()

    fun load() {
        LoadDataTask(apps, getApplication()).execute()
    }
}

class LoadDataTask(private val apps: MutableLiveData<List<AppInfo>>, private val appContext: Application) : AsyncTask<Int, Int, List<AppInfo>>() {
    override fun doInBackground(vararg params: Int?): List<AppInfo> {
        val packageManager = appContext.packageManager
        val packageInfoList = packageManager.getInstalledPackages(0)
        val userAppList = ArrayList<AppInfo>()
        for (packageInfo in packageInfoList) {
            if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
                continue
            }

            val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
            val sortAppName = HanziToPinyin.getInstance().get(appName).joinToString(separator = "", transform = { it.target })
            val appInfo = AppInfo(
                    appName,
                    sortAppName,
                    packageInfo.packageName,
                    packageInfo.versionName,
                    packageInfo.versionCode,
                    Formatter.formatFileSize(appContext, File(packageInfo.applicationInfo.publicSourceDir).length()),
                    packageInfo.applicationInfo.publicSourceDir
            )
            userAppList.add(appInfo)
        }

        userAppList.sortWith(Comparator { lhs, rhs -> lhs.sortName.compareTo(rhs.sortName) })

        return userAppList
    }

    override fun onPostExecute(appInfoLists: List<AppInfo>) {
        apps.value = appInfoLists
    }
}
