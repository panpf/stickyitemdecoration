/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.panpf.recycler.sticky.sample.item

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.recycler.sticky.sample.R
import com.github.panpf.recycler.sticky.sample.bean.AppsOverview
import com.github.panpf.recycler.sticky.sample.databinding.ItemAppsOverviewHorBinding

class AppsOverviewHorItemFactory
    : BindingItemFactory<AppsOverview, ItemAppsOverviewHorBinding>(AppsOverview::class) {

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppsOverviewHorBinding {
        return ItemAppsOverviewHorBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context,
        binding: ItemAppsOverviewHorBinding,
        item: BindingItem<AppsOverview, ItemAppsOverviewHorBinding>
    ) {
    }

    override fun bindItemData(
        context: Context,
        binding: ItemAppsOverviewHorBinding,
        item: BindingItem<AppsOverview, ItemAppsOverviewHorBinding>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: AppsOverview
    ) {
        binding.appsOverviewHorItemContentText.text = context.getString(
            R.string.apps_overview_item_hor, data.count, data.userAppCount, data.groupCount
        )
    }
}
