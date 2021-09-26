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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.recycler.sticky.assemblyadapter4.addAssemblyStickyItemDecorationWithPosition
import com.github.panpf.recycler.sticky.sample.base.BaseBindingFragment
import com.github.panpf.recycler.sticky.sample.bean.ListSeparator
import com.github.panpf.recycler.sticky.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.recycler.sticky.sample.item.AppItemFactory
import com.github.panpf.recycler.sticky.sample.item.AppsOverviewItemFactory
import com.github.panpf.recycler.sticky.sample.item.ListSeparatorItemFactory
import com.github.panpf.recycler.sticky.sample.vm.PinyinFlatAppsViewModel

class AssemblyPositionFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    companion object {
        fun create(stickyItemClickable: Boolean = false): NormalPositionFragment =
            NormalPositionFragment().apply {
                arguments = bundleOf("stickyItemClickable" to stickyItemClickable)
            }
    }

    private val viewModel by viewModels<PinyinFlatAppsViewModel>()

    private val stickyItemClickable by lazy { arguments?.getBoolean("stickyItemClickable") ?: false }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(
                AppItemFactory(),
                ListSeparatorItemFactory(),
                AppsOverviewItemFactory()
            )
        )
        binding.recyclerRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }

        viewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            val dataList = listOf(viewModel.appsOverviewData.value!!).plus(it ?: emptyList())

            val listSeparatorPositionList = ArrayList<Int>()
            dataList.forEachIndexed { index, item ->
                if (item is ListSeparator) {
                    listSeparatorPositionList.add(index)
                }
            }
            binding.recyclerRecycler.addAssemblyStickyItemDecorationWithPosition(
                listSeparatorPositionList
            ) {
                if (stickyItemClickable) {
                    showInContainer(binding.recyclerStickyContainer)
                }
            }
            recyclerAdapter.submitList(dataList)
        }
    }
}