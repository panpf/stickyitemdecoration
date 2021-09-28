package com.github.panpf.recycler.sticky.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.recycler.sticky.BuildConfig
import com.github.panpf.recycler.sticky.StickyItemDecoration
import com.github.panpf.recycler.sticky.sample.base.BaseBindingFragment
import com.github.panpf.recycler.sticky.sample.bean.Link
import com.github.panpf.recycler.sticky.sample.bean.ListSeparator
import com.github.panpf.recycler.sticky.sample.databinding.FragmentMainBinding
import com.github.panpf.recycler.sticky.sample.item.LinkItemFactory
import com.github.panpf.recycler.sticky.sample.item.ListSeparatorItemFactory

class MainFragment : BaseBindingFragment<FragmentMainBinding>() {

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ) = FragmentMainBinding.inflate(inflater, parent, false)

    override fun onInitData(binding: FragmentMainBinding, savedInstanceState: Bundle?) {
        StickyItemDecoration.debugMode = BuildConfig.DEBUG

        binding.mainRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = AssemblyRecyclerAdapter(
                listOf(
                    LinkItemFactory(),
                    ListSeparatorItemFactory(null, hiddenTapMe = true)
                ),
                listOf(
                    ListSeparator("Linear"),

                    Link(
                        "Linear - Position",
                        LinearFragment.create(way = LinearFragment.Way.POSITION)
                    ),

                    Link(
                        "Linear - ItemType",
                        LinearFragment.create(way = LinearFragment.Way.ITEM_TYPE)
                    ),

                    Link(
                        "Linear - Assembly",
                        LinearAssemblyFragment.create(stickyItemClickable = false)
                    ),

                    Link(
                        "Linear - Clickable",
                        LinearAssemblyFragment.create(stickyItemClickable = true)
                    ),

                    Link(
                        "Linear - Horizontal",
                        LinearAssemblyHorFragment.create(stickyItemClickable = false)
                    ),

                    Link(
                        "Linear - Horizontal - Clickable",
                        LinearAssemblyHorFragment.create(stickyItemClickable = true)
                    ),

                    ListSeparator("Grid"),

                    Link(
                        "Grid",
                        GridAssemblyFragment.create(stickyItemClickable = false)
                    ),

                    Link(
                        "Grid - Clickable",
                        GridAssemblyFragment.create(stickyItemClickable = true)
                    ),

                    Link(
                        "Grid - Horizontal",
                        GridAssemblyHorFragment.create(stickyItemClickable = false)
                    ),

                    Link(
                        "Grid - Horizontal - Clickable",
                        GridAssemblyHorFragment.create(stickyItemClickable = true)
                    ),

                    ListSeparator("StaggeredGrid"),

                    Link(
                        "StaggeredGrid",
                        StaggeredGridAssemblyFragment.create(stickyItemClickable = false)
                    ),

                    Link(
                        "StaggeredGrid - Clickable",
                        StaggeredGridAssemblyFragment.create(stickyItemClickable = true)
                    ),

                    Link(
                        "StaggeredGrid - Horizontal",
                        StaggeredGridAssemblyHorFragment.create(stickyItemClickable = false)
                    ),

                    Link(
                        "StaggeredGrid - Horizontal - Clickable",
                        StaggeredGridAssemblyHorFragment.create(stickyItemClickable = true)
                    ),
                )
            )
        }
    }
}