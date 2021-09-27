package com.github.panpf.recycler.sticky.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.recycler.sticky.sample.base.BaseBindingFragment
import com.github.panpf.recycler.sticky.sample.bean.Link
import com.github.panpf.recycler.sticky.sample.bean.ListSeparator
import com.github.panpf.recycler.sticky.sample.databinding.FragmentMainBinding
import com.github.panpf.recycler.sticky.sample.item.LinkItemFactory
import com.github.panpf.recycler.sticky.sample.item.ListSeparatorItemFactory

class MainFragment : BaseBindingFragment<FragmentMainBinding>() {

    private val links = listOf(
        ListSeparator("Linear"),

        Link(
            "Linear - Position",
            SampleFragment.create(way = SampleFragment.Way.POSITION)
        ),

        Link(
            "Linear - ItemType",
            SampleFragment.create(way = SampleFragment.Way.ITEM_TYPE)
        ),

        Link(
            "Linear - AssemblyItemFactory",
            AssemblySampleFragment.create(stickyItemClickable = false)
        ),

        Link(
            "Linear - Clickable",
            AssemblySampleFragment.create(stickyItemClickable = true)
        ),

        Link(
            "Linear - Horizontal",
            HorAssemblySampleFragment.create(stickyItemClickable = false)
        ),

        Link(
            "Linear - Horizontal - Clickable",
            HorAssemblySampleFragment.create(stickyItemClickable = true)
        ),

        ListSeparator("Grid"),

        Link(
            "Grid",
            GridSampleFragment.create(stickyItemClickable = false)
        ),

        Link(
            "Grid - Clickable",
            GridSampleFragment.create(stickyItemClickable = true)
        ),

        Link(
            "Grid - Horizontal",
            HorGridSampleFragment.create(stickyItemClickable = false)
        ),

        Link(
            "Grid - Horizontal - Clickable",
            HorGridSampleFragment.create(stickyItemClickable = true)
        ),
    )

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ) = FragmentMainBinding.inflate(inflater, parent, false)

    override fun onInitData(binding: FragmentMainBinding, savedInstanceState: Bundle?) {
        binding.mainRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = AssemblyRecyclerAdapter(
                listOf(
                    LinkItemFactory(),
                    ListSeparatorItemFactory(hiddenTapMe = true)
                ),
                links
            )
        }
    }
}