# StickyRecyclerItemDecoration

![Platform][platform_image]
[![API][api_image]][api_link]
[![Release][release_icon]][release_link]
[![License][license_image]][license_link]

StickyRecyclerItemDecoration 是 Android 上黏性头部列表的 RecyclerView 实现版本，可方便的将 item 固定显示在列表的顶部并可跟随滑动替换新的 header item，参考自 [StickyItemDecoration]

## 特点

* 用法简单，定义一个 ViewGroup 跟 RecyclerView 顶部保持一致，然后 adapter 实现 StickyRecyclerAdapter 接口即可
* 支持一个列表中有多种 item type 的 sticky item
* sticky item 可以接收点击事件
* 支持 sticky item 背景全透明或半透明，当显示 sticky item 的时候列表内相应的 item 会变成 INVISIBLE 状态，避免半透明背景下，显示重复内容
* 支持动态更改 sticky item 的高度，但需要你自己做好状态同步，详情请参考 sample 中的 [AppHeaderItem]

对比 [StickyItemDecoration]

* 用法更简单，不需要事先在 header ViewGroup 中放入 header 布局以及指定 header item type 的值
* [StickyItemDecoration] 同时只能有一种类型的 header，StickyRecyclerItemDecoration 支持 N 种
* 支持 sticky item 背景全透明或半透明

## 使用指南

### 1. 从 JCenter 导入

```groovy
dependencies {
    compile 'me.panpf:sticky-recycler-item-decoration:$lastVersionName'
}
```

`$lastVersionName`：[![Release][release_icon]][release_link]`（不带v）`

### 2. 定义布局

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/white">

    <android.support.v7.widget.RecyclerView
        android:id="@+id/main_recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:listitem="@layout/list_item_app" />

    <FrameLayout
        android:id="@+id/main_stickyContainerLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
</FrameLayout>
```

如上你需要在布局中定义一个用来显示 header 的 layout，你需要保证其顶部跟 RecyclerView 保持一致

### 3. 实现 StickyRecyclerAdapter 接口

```java
public class BaseStickyRecyclerAdapter extends RecyclerView.Adapter implements StickyRecyclerAdapter {

    @Override
    public boolean isStickyItemByType(int type) {
        return type == 4;
    }
}
```

如上让你的 adapter 实现 [StickyRecyclerAdapter] 接口并根据你的实际情况实现 isStickyItemByType(int) 方法

### 4. 使用 StickyRecyclerItemDecoration

```kotlin
val recyclerView = findViewById(R.id.main_recyclerView)
val stickyContainerLayout = findViewById(R.id.main_stickyContainerLayout)

recyclerView.addItemDecoration(StickyRecyclerItemDecoration(stickyContainerLayout))

recyclerView.adapter = BaseStickyRecyclerAdapter()
```

如上将 stickyContainerLayout 交给  [StickyRecyclerItemDecoration] 然后将 [StickyRecyclerItemDecoration] 添加到 RecyclerView 中，最后再配合实现了 [StickyRecyclerAdapter] 接口的 adapter 即可

`完整示例请参考 sample 源码`

## License
    Copyright (C) 2018 Peng fei Pan <sky@panpf.me>

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
[api_link]: https://android-arsenal.com/api?level=14
[release_icon]: https://img.shields.io/github/release/panpf/sticky-recycler-item-decoration.svg
[release_link]: https://github.com/panpf/sticky-recycler-item-decoration/releases
[license_image]: https://img.shields.io/badge/License-Apache%202-blue.svg
[license_link]: https://www.apache.org/licenses/LICENSE-2.0
[StickyItemDecoration]: https://github.com/oubowu/StickyItemDecoration
[StickyRecyclerAdapter]:  https://github.com/panpf/sticky-recycler-item-decoration/blob/master/sticky-recycler-item-decoration/src/main/java/me/panpf/recycler/sticky/StickyRecyclerAdapter.java
[StickyRecyclerItemDecoration]: https://github.com/panpf/sticky-recycler-item-decoration/blob/master/sticky-recycler-item-decoration/src/main/java/me/panpf/recycler/sticky/StickyRecyclerItemDecoration.java
[AppHeaderItem]: https://github.com/panpf/sticky-recycler-item-decoration/blob/master/sample/src/main/java/me/panpf/recycler/sticky/sample/adapter/item/AppHeaderItem.kt
