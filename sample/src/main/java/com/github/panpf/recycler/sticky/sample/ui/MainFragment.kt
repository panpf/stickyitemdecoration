package com.github.panpf.recycler.sticky.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.recycler.sticky.BuildConfig
import com.github.panpf.recycler.sticky.StickyItemDecoration
import com.github.panpf.recycler.sticky.sample.NavMainDirections
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
                        NavMainDirections.actionGlobalLinearFragment(
                            title = "Linear",
                            subtitle = "Position",
                            way = Way.POSITION,
                        )
                    ),

                    Link(
                        "Linear - ItemType",
                        NavMainDirections.actionGlobalLinearFragment(
                            title = "Linear",
                            subtitle = "ItemType",
                            way = Way.ITEM_TYPE
                        )
                    ),

                    Link(
                        "Linear - Assembly",
                        NavMainDirections.actionGlobalLinearAssemblyFragment(
                            title = "Linear",
                            subtitle = "Assembly",
                            stickyItemClickable = false
                        )
                    ),

                    Link(
                        "Linear - Clickable",
                        NavMainDirections.actionGlobalLinearAssemblyFragment(
                            title = "Linear",
                            subtitle = "Clickable",
                            stickyItemClickable = true
                        )
                    ),

                    Link(
                        "Linear - Horizontal",
                        NavMainDirections.actionGlobalLinearAssemblyHorFragment(
                            title = "Linear",
                            subtitle = "Horizontal",
                            stickyItemClickable = false
                        )
                    ),

                    Link(
                        "Linear - Horizontal - Clickable",
                        NavMainDirections.actionGlobalLinearAssemblyHorFragment(
                            title = "Linear",
                            subtitle = "Horizontal - Clickable",
                            stickyItemClickable = true
                        )
                    ),

                    ListSeparator("Grid"),

                    Link(
                        "Grid",
                        NavMainDirections.actionGlobalGridAssemblyFragment(
                            title = "Grid",
                            stickyItemClickable = false
                        )
                    ),

                    Link(
                        "Grid - Clickable",
                        NavMainDirections.actionGlobalGridAssemblyFragment(
                            title = "Grid",
                            subtitle = "Clickable",
                            stickyItemClickable = true
                        )
                    ),

                    Link(
                        "Grid - Horizontal",
                        NavMainDirections.actionGlobalGridAssemblyHorFragment(
                            title = "Grid",
                            subtitle = "Horizontal",
                            stickyItemClickable = false
                        )
                    ),

                    Link(
                        "Grid - Horizontal - Clickable",
                        NavMainDirections.actionGlobalGridAssemblyHorFragment(
                            title = "Grid",
                            subtitle = "Horizontal - Clickable",
                            stickyItemClickable = true
                        )
                    ),

                    ListSeparator("StaggeredGrid"),

                    Link(
                        "StaggeredGrid",
                        NavMainDirections.actionGlobalStaggeredGridAssemblyFragment(
                            title = "StaggeredGrid",
                            stickyItemClickable = false
                        )
                    ),

                    Link(
                        "StaggeredGrid - Clickable",
                        NavMainDirections.actionGlobalStaggeredGridAssemblyFragment(
                            title = "StaggeredGrid",
                            subtitle = "Clickable",
                            stickyItemClickable = true
                        )
                    ),

                    Link(
                        "StaggeredGrid - Horizontal",
                        NavMainDirections.actionGlobalStaggeredGridAssemblyHorFragment(
                            title = "StaggeredGrid",
                            subtitle = "Horizontal",
                            stickyItemClickable = false
                        )
                    ),

                    Link(
                        "StaggeredGrid - Horizontal - Clickable",
                        NavMainDirections.actionGlobalStaggeredGridAssemblyHorFragment(
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