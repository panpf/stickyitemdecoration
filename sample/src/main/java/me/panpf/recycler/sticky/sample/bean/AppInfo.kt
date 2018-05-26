package me.panpf.recycler.sticky.sample.bean

/**
 * App 信息
 */
data class AppInfo(
    val name: String,
    val sortName: String,
    val packageName: String,
    val versionName: String,
    val versionCode: Int,
    val appSize: String,
    val apkFilePath: String
)
