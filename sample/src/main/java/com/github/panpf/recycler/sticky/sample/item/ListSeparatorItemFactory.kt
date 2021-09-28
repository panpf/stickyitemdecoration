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
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.recycler.sticky.sample.bean.ListSeparator
import com.github.panpf.recycler.sticky.sample.databinding.ItemListSeparatorBinding

class ListSeparatorItemFactory(
    private val recyclerView: RecyclerView?,
    private val hiddenTapMe: Boolean = false,
) : BindingItemFactory<ListSeparator, ItemListSeparatorBinding>(ListSeparator::class) {

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemListSeparatorBinding {
        return ItemListSeparatorBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context,
        binding: ItemListSeparatorBinding,
        item: BindingItem<ListSeparator, ItemListSeparatorBinding>
    ) {
        binding.listSeparatorItemActionText.apply {
            setOnClickListener {
                Toast.makeText(context, "You tap me", Toast.LENGTH_LONG).show()
                recyclerView?.adapter?.notifyItemChanged(item.absoluteAdapterPosition)
            }
            isVisible = !hiddenTapMe
        }
    }

    override fun bindItemData(
        context: Context,
        binding: ItemListSeparatorBinding,
        item: BindingItem<ListSeparator, ItemListSeparatorBinding>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: ListSeparator
    ) {
        binding.listSeparatorItemTitleText.text = data.title
    }
}
