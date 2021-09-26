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
        ListSeparator("Simple"),
        Link(
            "Simple - Position",
            NormalPositionFragment.create(stickyItemClickable = false)
        ),
        Link(
            "Simple - ItemType",
            NormalItemTypeFragment.create(stickyItemClickable = false)
        ),
        Link(
            "Simple - Clickable",
            NormalItemTypeFragment.create(stickyItemClickable = true)
        ),

        ListSeparator("Assembly"),
        Link(
            "Assembly - Position",
            AssemblyPositionFragment.create(stickyItemClickable = false)
        ),
        Link(
            "Assembly - ItemType",
            AssemblyItemTypeFragment.create(stickyItemClickable = false)
        ),
        Link(
            "Assembly - ItemFactory",
            AssemblyItemFactoryFragment.create(stickyItemClickable = false)
        ),
        Link(
            "Assembly - Clickable",
            AssemblyItemFactoryFragment.create(stickyItemClickable = true)
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