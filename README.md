# StickyItemDecoration

![Platform][platform_image]
[![API][api_image]][api_link]
[![Release][version_icon]][version_link]
[![License][license_image]][license_link]

[StickyItemDecoration] 可方便的将指定的 item 固定显示在 [RecyclerView] 的顶部并跟随滑动替换新的 sticky item

## 特性

* 用法简单. 只需指定 sticky item 的 position 或 itemType 即可
* 支持点击. 需要点击时只需额外指定一个专门用来显示 sticky item 的 container 即可
* 支持半透明背景. 当 sticky item 的背景半透明时，可以开启将列表内相应的 item 变成 INVISIBLE 状态功能，避免半透明背景下，显示重复内容

## 导入

`该库已发布到 mavenCentral`

```kotlin
dependencies {
    implementation("io.github.panpf.stickyitemdecoration:stickyitemdecoration:${LAST_VERSION}")
    
    // Optional. Support AssemblyAdapter 4.0 version to specify sticky item through ItemFactory
    implementation("io.github.panpf.stickyitemdecoration:stickyitemdecoration-assemblyadapter4:${LAST_VERSION}")
}
```

`${LAST_VERSION}`: [![Download][version_icon]][version_link] (No include 'v')

## 使用

### 示例

```kotlin
// 添加一个 StickyItemDecoration 并指定 position 是 3 和 7 的 item 为 sticky item
recyclerView.addStickyItemDecorationWithPosition(3, 7)

// 添加一个 StickyItemDecoration 并指定 itemType 是 1 的 item 为 sticky item
recyclerView.addStickyItemDecorationWithItemType(1)

// 添加一个 StickyItemDecoration 并指定 position 是 3 和 7 或 itemType 是 1 的 item 为 sticky item
recyclerView.addStickyItemDecoration {
    position(3, 7)
    itemType(1)
}
```

### 支持 AssemblyAdapter 4

`stickyitemdecoration-assemblyadapter4`
模块提供了对 [AssemblyAdapter](https://github.com/panpf/assembly-adapter) 的支持

如下：

```kotlin
// 添加一个 AssemblyStickyItemDecoration 并指定 ItemFactory 是 ListSeparatorItemFactory 的 item 为 sticky item
recyclerView.addAssemblyStickyItemDecorationWithItemFactory(ListSeparatorItemFactory::class)
```

[AssemblyStickyItemDecoration] 还支持 position 和 itemType

### 让 sticky item 可点击

默认情况下 [StickyItemDecoration] 将 sticky item 绘制在 [RecyclerView] 的顶部，这种实现方式的弊端就是 sticky item 无法接收点击事件

我们只需要给 [StickyItemDecoration] 指定一个 [FrameLayout] 用来专门显示 sticky item 即可解决这个问题

首先布局中定义一个顶部和 [RecyclerView] 对齐的 [FrameLayout]，如下：

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent" 
    android:layout_height="match_parent">

    <androidx.recyclerview.widget.RecyclerView 
        android:id="@+id/mainRecyclerView"
        android:layout_width="match_parent" 
        android:layout_height="match_parent" />

    <FrameLayout android:id="@+id/mainStickyContainerLayout" 
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
</FrameLayout>
```

然后在添加 [StickyItemDecoration] 时指定容器即可，如下：

```kotlin
recyclerView.addStickyItemDecorationWithPosition(3, 7) {
    showInContainer(findViewById<FrameLayout>(R.id.mainStickyContainerLayout))
}
```

### 其它功能

```kotlin
recyclerView.addStickyItemDecorationWithPosition(3, 7) {
    // 禁止滑动时新的 sticky item 顶替掉旧的 sticky item 效果
    disabledScrollUpStickyItem()

    // sticky item 显示时原始 item 变为 invisible 状态。此功能适用于 sticky item 背景为半透明时使用
    invisibleOriginItemWhenStickyItemShowing()
}
```

`更多示例请参考 sample 源码`

## License

    Copyright (C) 2021 panpf <panpfpanpf@outlook.me>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[platform_image]: https://img.shields.io/badge/Platform-Android-brightgreen.svg

[api_image]: https://img.shields.io/badge/API-14%2B-orange.svg

[version_icon]: https://img.shields.io/maven-central/v/io.github.panpf.stickyitemdecoration/stickyitemdecoration

[version_link]: https://repo1.maven.org/maven2/io/github/panpf/stickyitemdecoration/

[api_link]: https://android-arsenal.com/api?level=14

[license_image]: https://img.shields.io/badge/License-Apache%202-blue.svg

[license_link]: https://www.apache.org/licenses/LICENSE-2.0

[StickyItemDecoration]: stickyitemdecoration/src/main/java/com/github/panpf/recycler/sticky/StickyItemDecoration.kt

[AssemblyStickyItemDecoration]: stickyitemdecoration-assemblyadapter4/src/main/java/com/github/panpf/recycler/sticky/assemblyadapter4/AssemblyStickyItemDecoration.kt

[RecyclerView]: https://developer.android.google.cn/reference/androidx/recyclerview/widget/RecyclerView

[FrameLayout]: https://developer.android.google.cn/reference/android/widget/FrameLayout
