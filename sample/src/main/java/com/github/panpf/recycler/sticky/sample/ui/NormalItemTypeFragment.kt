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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.recycler.sticky.assemblyadapter4.addAssemblyStickyItemDecoration
import com.github.panpf.recycler.sticky.sample.base.BaseBindingFragment
import com.github.panpf.recycler.sticky.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.recycler.sticky.sample.item.AppListAdapter
import com.github.panpf.recycler.sticky.sample.vm.PinyinFlatAppsViewModel

class NormalItemTypeFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val viewModel by viewModels<PinyinFlatAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val recyclerAdapter = AppListAdapter()
        binding.recyclerRecycler.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addAssemblyStickyItemDecoration(binding.recyclerStickyContainer) {
                itemType(1)
            }
        }

        viewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            val dataList = listOf(viewModel.appsOverviewData.value!!).plus(it ?: emptyList())
            recyclerAdapter.submitList(dataList)
        }
    }
}