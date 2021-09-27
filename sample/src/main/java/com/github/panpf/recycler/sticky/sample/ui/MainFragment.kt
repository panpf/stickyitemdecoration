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
        ListSeparator("How To Determine Sticky Item"),
        Link(
            "Position",
            SampleFragment.create(way = SampleFragment.Way.POSITION)
        ),

        Link(
            "ItemType",
            SampleFragment.create(way = SampleFragment.Way.ITEM_TYPE)
        ),

        Link(
            "AssemblyItemFactory",
            AssemblySampleFragment.create(stickyItemClickable = false, horizontal = false)
        ),

        ListSeparator("Clickable"),

        Link(
            "Clickable",
            AssemblySampleFragment.create(stickyItemClickable = true, horizontal = false)
        ),

        ListSeparator("Horizontal"),

        Link(
            "Horizontal",
            AssemblySampleFragment.create(stickyItemClickable = false, horizontal = true)
        ),

        Link(
            "Horizontal - Clickable",
            AssemblySampleFragment.create(stickyItemClickable = true, horizontal = true)
        ),

        // todo grid, staggeredGrid
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