package com.github.panpf.recycler.sticky.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.recycler.sticky.BuildConfig
import com.github.panpf.recycler.sticky.StickyItemDecoration
import com.github.panpf.recycler.sticky.sample.base.ToolbarFragment
import com.github.panpf.recycler.sticky.sample.bean.Link
import com.github.panpf.recycler.sticky.sample.bean.ListSeparator
import com.github.panpf.recycler.sticky.sample.bean.Way
import com.github.panpf.recycler.sticky.sample.databinding.FragmentMainBinding
import com.github.panpf.recycler.sticky.sample.item.LinkItemFactory
import com.github.panpf.recycler.sticky.sample.item.ListSeparatorItemFactory

class MainFragment : ToolbarFragment<FragmentMainBinding>() {

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ) = FragmentMainBinding.inflate(inflater, parent, false)

    override fun onInitData(
        toolbar: Toolbar, binding: FragmentMainBinding, savedInstanceState: Bundle?
    ) {
        StickyItemDecoration.debugMode = BuildConfig.DEBUG

        MainFragmentDirections.actionMainFragmentToGridAssemblyFragment()

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
                        MainFragmentDirections.actionMainFragmentToLinearFragment(
                            title = "Linear",
                            subtitle = "Position",
                            way = Way.POSITION,
                        )
                    ),

                    Link(
                        "Linear - ItemType",
                        MainFragmentDirections.actionMainFragmentToLinearFragment(
                            title = "Linear",
                            subtitle = "ItemType",
                            way = Way.ITEM_TYPE
                        )
                    ),

                    Link(
                        "Linear - Assembly",
                        MainFragmentDirections.actionMainFragmentToLinearAssemblyFragment(
                            title = "Linear",
                            subtitle = "Assembly",
                            stickyItemClickable = false
                        )
                    ),

                    Link(
                        "Linear - Clickable",
                        MainFragmentDirections.actionMainFragmentToLinearAssemblyFragment(
                            title = "Linear",
                            subtitle = "Clickable",
                            stickyItemClickable = true
                        )
                    ),

                    Link(
                        "Linear - Horizontal",
                        MainFragmentDirections.actionMainFragmentToLinearAssemblyHorFragment(
                            title = "Linear",
                            subtitle = "Horizontal",
                            stickyItemClickable = false
                        )
                    ),

                    Link(
                        "Linear - Horizontal - Clickable",
                        MainFragmentDirections.actionMainFragmentToLinearAssemblyHorFragment(
                            title = "Linear",
                            subtitle = "Horizontal - Clickable",
                            stickyItemClickable = true
                        )
                    ),

                    ListSeparator("Grid"),

                    Link(
                        "Grid",
                        MainFragmentDirections.actionMainFragmentToGridAssemblyFragment(
                            title = "Grid",
                            stickyItemClickable = false
                        )
                    ),

                    Link(
                        "Grid - Clickable",
                        MainFragmentDirections.actionMainFragmentToGridAssemblyFragment(
                            title = "Grid",
                            subtitle = "Clickable",
                            stickyItemClickable = true
                        )
                    ),

                    Link(
                        "Grid - Horizontal",
                        MainFragmentDirections.actionMainFragmentToGridAssemblyHorFragment(
                            title = "Grid",
                            subtitle = "Horizontal",
                            stickyItemClickable = false
                        )
                    ),

                    Link(
                        "Grid - Horizontal - Clickable",
                        MainFragmentDirections.actionMainFragmentToGridAssemblyHorFragment(
                            title = "Grid",
                            subtitle = "Horizontal - Clickable",
                            stickyItemClickable = true
                        )
                    ),

                    ListSeparator("StaggeredGrid"),

                    Link(
                        "StaggeredGrid",
                        MainFragmentDirections.actionMainFragmentToStaggeredGridAssemblyFragment(
                            title = "StaggeredGrid",
                            stickyItemClickable = false
                        )
                    ),

                    Link(
                        "StaggeredGrid - Clickable",
                        MainFragmentDirections.actionMainFragmentToStaggeredGridAssemblyFragment(
                            title = "StaggeredGrid",
                            subtitle = "Clickable",
                            stickyItemClickable = true
                        )
                    ),

                    Link(
                        "StaggeredGrid - Horizontal",
                        MainFragmentDirections.actionMainFragmentToStaggeredGridAssemblyHorFragment(
                            title = "StaggeredGrid",
                            subtitle = "Horizontal",
                            stickyItemClickable = false
                        )
                    ),

                    Link(
                        "StaggeredGrid - Horizontal - Clickable",
                        MainFragmentDirections.actionMainFragmentToStaggeredGridAssemblyHorFragment(
                            title = "StaggeredGrid",
                            subtitle = "Horizontal - Clickable",
                            stickyItemClickable = true
                        )
                    ),
                )
            )
        }
    }
}