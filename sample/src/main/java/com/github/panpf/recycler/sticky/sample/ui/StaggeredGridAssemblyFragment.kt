/*
 * Copyright (C) 2021 panpf <panpfpanpf@oulook.com>
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
package com.github.panpf.recycler.sticky.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblyStaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.addAssemblyStaggeredGridDividerItemDecoration
import com.github.panpf.recycler.sticky.StickyItemDecoration
import com.github.panpf.recycler.sticky.assemblyadapter4.addAssemblyStickyItemDecorationWithItemFactory
import com.github.panpf.recycler.sticky.sample.base.ToolbarFragment
import com.github.panpf.recycler.sticky.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.recycler.sticky.sample.item.AppCardGridItemFactory
import com.github.panpf.recycler.sticky.sample.item.AppsOverviewItemFactory
import com.github.panpf.recycler.sticky.sample.item.ListSeparatorItemFactory
import com.github.panpf.recycler.sticky.sample.vm.PinyinFlatAppsViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px

class StaggeredGridAssemblyFragment : ToolbarFragment<FragmentRecyclerBinding>() {

    private val args: StaggeredGridAssemblyFragmentArgs by navArgs()

    private val viewModel by activityViewModels<PinyinFlatAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitViews(
        toolbar: Toolbar,
        binding: FragmentRecyclerBinding,
        savedInstanceState: Bundle?
    ) {
        initMenu(toolbar, binding.recyclerRecycler)
    }

    override fun onInitData(
        toolbar: Toolbar, binding: FragmentRecyclerBinding, savedInstanceState: Bundle?
    ) {
        toolbar.title = args.title
        toolbar.subtitle = args.subtitle

        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(
                AppCardGridItemFactory(),
                ListSeparatorItemFactory(binding.recyclerRecycler),
                AppsOverviewItemFactory()
            )
        )
        binding.recyclerRecycler.apply {
            adapter = recyclerAdapter
            layoutManager = AssemblyStaggeredGridLayoutManager(
                3,
                GridLayoutManager.VERTICAL,
                listOf(
                    AppsOverviewItemFactory::class,
                    ListSeparatorItemFactory::class
                )
            )
            addAssemblyStickyItemDecorationWithItemFactory(ListSeparatorItemFactory::class) {
                if (args.stickyItemClickable) {
                    showInContainer(binding.recyclerStickyContainer)
                }
            }
            addAssemblyStaggeredGridDividerItemDecoration {
                divider(Divider.space(16.dp2px)) {
                    disableByItemFactoryClass(AppsOverviewItemFactory::class)
                }
                footerDivider(Divider.space(20.dp2px)) {
                    disableByItemFactoryClass(AppsOverviewItemFactory::class)
                    disableByItemFactoryClass(ListSeparatorItemFactory::class)
                }
                sideDivider(Divider.space(16.dp2px))
                sideHeaderDivider(Divider.space(20.dp2px)) {
                    disableByItemFactoryClass(AppsOverviewItemFactory::class)
                    disableByItemFactoryClass(ListSeparatorItemFactory::class)
                }
                sideFooterDivider(Divider.space(20.dp2px)) {
                    disableByItemFactoryClass(AppsOverviewItemFactory::class)
                    disableByItemFactoryClass(ListSeparatorItemFactory::class)
                }
            }
        }

        viewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            val dataList = listOf(viewModel.appsOverviewData.value!!).plus(it ?: emptyList())
            recyclerAdapter.submitList(dataList)
        }
    }

    private fun initMenu(toolbar: Toolbar, recyclerView: RecyclerView) {
        toolbar.menu.add(
            0, 1, 0,
            "DisableScrollStickyItem"
        ).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        }.setOnMenuItemClickListener {
            val stickyItemDecoration = recyclerView.getItemDecorationAt(0) as StickyItemDecoration
            stickyItemDecoration.disabledScrollUpStickyItem =
                !stickyItemDecoration.disabledScrollUpStickyItem
            recyclerView.postInvalidate()
            it.title =
                if (stickyItemDecoration.disabledScrollUpStickyItem) "EnableScrollStickyItem" else "DisableScrollStickyItem"
            true
        }

        toolbar.menu.add(
            0, 2, 1,
            "HiddenOriginStickyItem"
        ).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        }.setOnMenuItemClickListener {
            val stickyItemDecoration = recyclerView.getItemDecorationAt(0) as StickyItemDecoration
            stickyItemDecoration.invisibleOriginItemWhenStickyItemShowing =
                !stickyItemDecoration.invisibleOriginItemWhenStickyItemShowing
            recyclerView.postInvalidate()
            it.title =
                if (stickyItemDecoration.invisibleOriginItemWhenStickyItemShowing) "ShowOriginStickyItem" else "HiddenOriginStickyItem"
            true
        }
    }
}