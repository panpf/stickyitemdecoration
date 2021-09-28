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
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.recycler.sticky.StickyItemDecoration
import com.github.panpf.recycler.sticky.assemblyadapter4.addAssemblyStickyItemDecorationWithItemFactory
import com.github.panpf.recycler.sticky.sample.base.BaseBindingFragment
import com.github.panpf.recycler.sticky.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.recycler.sticky.sample.item.AppItemFactory
import com.github.panpf.recycler.sticky.sample.item.AppsOverviewItemFactory
import com.github.panpf.recycler.sticky.sample.item.ListSeparatorItemFactory
import com.github.panpf.recycler.sticky.sample.vm.MenuViewModel
import com.github.panpf.recycler.sticky.sample.vm.PinyinFlatAppsViewModel

class LinearAssemblyFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    companion object {
        fun create(stickyItemClickable: Boolean = false) = LinearAssemblyFragment().apply {
            arguments = bundleOf("stickyItemClickable" to stickyItemClickable)
        }
    }

    private val stickyItemClickable by lazy {
        arguments?.getBoolean("stickyItemClickable") ?: false
    }

    private val viewModel by viewModels<PinyinFlatAppsViewModel>()
    private val menuViewModel by activityViewModels<MenuViewModel>()

    private var disabledScrollUpStickyItem = false
    private var invisibleOriginItemWhenStickyItemShowing = false

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(
                AppItemFactory(),
                ListSeparatorItemFactory(binding.recyclerRecycler),
                AppsOverviewItemFactory()
            )
        )
        binding.recyclerRecycler.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            addAssemblyStickyItemDecorationWithItemFactory(ListSeparatorItemFactory::class) {
                if (stickyItemClickable) {
                    showInContainer(binding.recyclerStickyContainer)
                }
            }
        }

        viewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            val dataList = listOf(viewModel.appsOverviewData.value!!).plus(it ?: emptyList())
            recyclerAdapter.submitList(dataList)
        }

        menuViewModel.menuClickEvent.listen(viewLifecycleOwner) {
            when (it?.id) {
                1 -> {
                    disabledScrollUpStickyItem = !disabledScrollUpStickyItem
                    binding.recyclerRecycler.apply {
                        (getItemDecorationAt(0) as StickyItemDecoration)
                            .disabledScrollUpStickyItem = disabledScrollUpStickyItem
                        postInvalidate()
                    }
                    menuViewModel.menuInfoListData.postValue(buildMenuInfoList())
                }
                2 -> {
                    invisibleOriginItemWhenStickyItemShowing =
                        !invisibleOriginItemWhenStickyItemShowing
                    binding.recyclerRecycler.apply {
                        (getItemDecorationAt(0) as StickyItemDecoration)
                            .invisibleOriginItemWhenStickyItemShowing =
                            invisibleOriginItemWhenStickyItemShowing
                        postInvalidate()
                    }
                    menuViewModel.menuInfoListData.postValue(buildMenuInfoList())
                }
            }
        }
        menuViewModel.menuInfoListData.postValue(buildMenuInfoList())
    }

    private fun buildMenuInfoList(): List<MenuViewModel.MenuInfo> {
        return listOf(
            MenuViewModel.MenuInfo(
                1,
                if (disabledScrollUpStickyItem) "EnableScrollStickyItem" else "DisableScrollStickyItem"
            ),
            MenuViewModel.MenuInfo(
                2,
                if (invisibleOriginItemWhenStickyItemShowing) "ShowOriginStickyItem" else "HiddenOriginStickyItem"
            ),
        )
    }
}