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
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.recycler.sticky.StickyItemDecoration
import com.github.panpf.recycler.sticky.assemblyadapter4.addAssemblyStickyItemDecorationWithItemFactory
import com.github.panpf.recycler.sticky.sample.base.BaseBindingFragment
import com.github.panpf.recycler.sticky.sample.databinding.FragmentRecyclerHorBinding
import com.github.panpf.recycler.sticky.sample.item.AppHorItemFactory
import com.github.panpf.recycler.sticky.sample.item.AppsOverviewHorItemFactory
import com.github.panpf.recycler.sticky.sample.item.ListSeparatorHorItemFactory
import com.github.panpf.recycler.sticky.sample.vm.MenuViewModel
import com.github.panpf.recycler.sticky.sample.vm.PinyinFlatAppsViewModel

class HorAssemblySampleFragment : BaseBindingFragment<FragmentRecyclerHorBinding>() {

    companion object {
        fun create(stickyItemClickable: Boolean = false) = HorAssemblySampleFragment().apply {
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
    ): FragmentRecyclerHorBinding {
        return FragmentRecyclerHorBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerHorBinding, savedInstanceState: Bundle?) {
        binding.recyclerHorStickyContainer.updateLayoutParams<ViewGroup.LayoutParams> {
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(
                AppHorItemFactory(),
                ListSeparatorHorItemFactory(),
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
                if (stickyItemClickable) {
                    showInContainer(binding.recyclerHorStickyContainer)
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
                    binding.recyclerHorRecycler.apply {
                        (getItemDecorationAt(0) as StickyItemDecoration)
                            .disabledScrollUpStickyItem = disabledScrollUpStickyItem
                        postInvalidate()
                    }
                    menuViewModel.menuInfoListData.postValue(buildMenuInfoList())
                }
                2 -> {
                    invisibleOriginItemWhenStickyItemShowing =
                        !invisibleOriginItemWhenStickyItemShowing
                    binding.recyclerHorRecycler.apply {
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