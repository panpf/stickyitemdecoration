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
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.recycler.sticky.StickyItemDecoration
import com.github.panpf.recycler.sticky.assemblyadapter4.addAssemblyStickyItemDecorationWithItemFactory
import com.github.panpf.recycler.sticky.sample.base.ToolbarFragment
import com.github.panpf.recycler.sticky.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.recycler.sticky.sample.databinding.FragmentRecyclerHorBinding
import com.github.panpf.recycler.sticky.sample.item.AppHorItemFactory
import com.github.panpf.recycler.sticky.sample.item.AppsOverviewHorItemFactory
import com.github.panpf.recycler.sticky.sample.item.ListSeparatorHorItemFactory
import com.github.panpf.recycler.sticky.sample.vm.PinyinFlatAppsViewModel

class LinearAssemblyHorFragment : ToolbarFragment<FragmentRecyclerHorBinding>() {

    private val args: LinearAssemblyHorFragmentArgs by navArgs()

    private val viewModel by activityViewModels<PinyinFlatAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerHorBinding {
        return FragmentRecyclerHorBinding.inflate(inflater, parent, false)
    }

    override fun onInitViews(
        toolbar: Toolbar,
        binding: FragmentRecyclerHorBinding,
        savedInstanceState: Bundle?
    ) {
        initMenu(toolbar, binding.recyclerHorRecycler)
    }

    override fun onInitData(
        toolbar: Toolbar, binding: FragmentRecyclerHorBinding, savedInstanceState: Bundle?
    ) {
        toolbar.title = args.title
        toolbar.subtitle = args.subtitle

        binding.recyclerHorStickyContainer.updateLayoutParams<ViewGroup.LayoutParams> {
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(
                AppHorItemFactory(),
                ListSeparatorHorItemFactory(binding.recyclerHorRecycler),
                AppsOverviewHorItemFactory()
            )
        )
        binding.recyclerHorRecycler.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            addAssemblyStickyItemDecorationWithItemFactory(
                ListSeparatorHorItemFactory::class
            ) {
                if (args.stickyItemClickable) {
                    showInContainer(binding.recyclerHorStickyContainer)
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