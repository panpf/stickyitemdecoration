package com.github.panpf.recycler.sticky.test

import android.R
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.recycler.sticky.StickyItemDecoration
import com.github.panpf.recycler.sticky.addStickyItemDecoration
import com.github.panpf.recycler.sticky.addStickyItemDecorationWithItemType
import com.github.panpf.recycler.sticky.addStickyItemDecorationWithPosition
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StickyItemDecorationExtensionsTest {

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val adapter = AssemblyRecyclerAdapter<Any>(
            listOf(
                ViewItemFactory(String::class, R.layout.list_content),
                ViewItemFactory(Int::class, R.layout.list_content),
                ViewItemFactory(Boolean::class, R.layout.list_content),
            ),
            listOf(5, "A", "B", 1, "C", true, "D", false)
        )

        (RecyclerView(context).apply {
            addStickyItemDecorationWithPosition(1, 5)
        }.getItemDecorationAt(0) as StickyItemDecoration).apply {
            Assert.assertFalse(isStickyItemByPosition(adapter, 0))
            Assert.assertTrue(isStickyItemByPosition(adapter, 1))
            Assert.assertFalse(isStickyItemByPosition(adapter, 2))
            Assert.assertFalse(isStickyItemByPosition(adapter, 3))
            Assert.assertFalse(isStickyItemByPosition(adapter, 4))
            Assert.assertTrue(isStickyItemByPosition(adapter, 5))
            Assert.assertFalse(isStickyItemByPosition(adapter, 6))
            Assert.assertFalse(isStickyItemByPosition(adapter, 7))
        }
        (RecyclerView(context).apply {
            addStickyItemDecorationWithPosition(listOf(1, 5))
        }.getItemDecorationAt(0) as StickyItemDecoration).apply {
            Assert.assertFalse(isStickyItemByPosition(adapter, 0))
            Assert.assertTrue(isStickyItemByPosition(adapter, 1))
            Assert.assertFalse(isStickyItemByPosition(adapter, 2))
            Assert.assertFalse(isStickyItemByPosition(adapter, 3))
            Assert.assertFalse(isStickyItemByPosition(adapter, 4))
            Assert.assertTrue(isStickyItemByPosition(adapter, 5))
            Assert.assertFalse(isStickyItemByPosition(adapter, 6))
            Assert.assertFalse(isStickyItemByPosition(adapter, 7))
        }

        (RecyclerView(context).apply {
            addStickyItemDecorationWithItemType(1, 2)
        }.getItemDecorationAt(0) as StickyItemDecoration).apply {
            Assert.assertTrue(isStickyItemByPosition(adapter, 0))
            Assert.assertFalse(isStickyItemByPosition(adapter, 1))
            Assert.assertFalse(isStickyItemByPosition(adapter, 2))
            Assert.assertTrue(isStickyItemByPosition(adapter, 3))
            Assert.assertFalse(isStickyItemByPosition(adapter, 4))
            Assert.assertTrue(isStickyItemByPosition(adapter, 5))
            Assert.assertFalse(isStickyItemByPosition(adapter, 6))
            Assert.assertTrue(isStickyItemByPosition(adapter, 7))
        }

        (RecyclerView(context).apply {
            addStickyItemDecorationWithItemType(listOf(1, 2))
        }.getItemDecorationAt(0) as StickyItemDecoration).apply {
            Assert.assertTrue(isStickyItemByPosition(adapter, 0))
            Assert.assertFalse(isStickyItemByPosition(adapter, 1))
            Assert.assertFalse(isStickyItemByPosition(adapter, 2))
            Assert.assertTrue(isStickyItemByPosition(adapter, 3))
            Assert.assertFalse(isStickyItemByPosition(adapter, 4))
            Assert.assertTrue(isStickyItemByPosition(adapter, 5))
            Assert.assertFalse(isStickyItemByPosition(adapter, 6))
            Assert.assertTrue(isStickyItemByPosition(adapter, 7))
        }

        (RecyclerView(context).apply {
            addStickyItemDecoration { }
        }.getItemDecorationAt(0) as StickyItemDecoration).apply {
            Assert.assertFalse(isStickyItemByPosition(adapter, 0))
            Assert.assertFalse(isStickyItemByPosition(adapter, 1))
            Assert.assertFalse(isStickyItemByPosition(adapter, 2))
            Assert.assertFalse(isStickyItemByPosition(adapter, 3))
            Assert.assertFalse(isStickyItemByPosition(adapter, 4))
            Assert.assertFalse(isStickyItemByPosition(adapter, 5))
            Assert.assertFalse(isStickyItemByPosition(adapter, 6))
            Assert.assertFalse(isStickyItemByPosition(adapter, 7))
        }
        (RecyclerView(context).apply {
            addStickyItemDecoration {
                position(1, 5)
                itemType(1, 2)
            }
        }.getItemDecorationAt(0) as StickyItemDecoration).apply {
            Assert.assertTrue(isStickyItemByPosition(adapter, 0))
            Assert.assertTrue(isStickyItemByPosition(adapter, 1))
            Assert.assertFalse(isStickyItemByPosition(adapter, 2))
            Assert.assertTrue(isStickyItemByPosition(adapter, 3))
            Assert.assertFalse(isStickyItemByPosition(adapter, 4))
            Assert.assertTrue(isStickyItemByPosition(adapter, 5))
            Assert.assertFalse(isStickyItemByPosition(adapter, 6))
            Assert.assertTrue(isStickyItemByPosition(adapter, 7))
        }

        (RecyclerView(context).apply {
            addStickyItemDecoration {
                position(listOf(1, 5))
                itemType(listOf(1, 2))
            }
        }.getItemDecorationAt(0) as StickyItemDecoration).apply {
            Assert.assertTrue(isStickyItemByPosition(adapter, 0))
            Assert.assertTrue(isStickyItemByPosition(adapter, 1))
            Assert.assertFalse(isStickyItemByPosition(adapter, 2))
            Assert.assertTrue(isStickyItemByPosition(adapter, 3))
            Assert.assertFalse(isStickyItemByPosition(adapter, 4))
            Assert.assertTrue(isStickyItemByPosition(adapter, 5))
            Assert.assertFalse(isStickyItemByPosition(adapter, 6))
            Assert.assertTrue(isStickyItemByPosition(adapter, 7))
        }
    }
}