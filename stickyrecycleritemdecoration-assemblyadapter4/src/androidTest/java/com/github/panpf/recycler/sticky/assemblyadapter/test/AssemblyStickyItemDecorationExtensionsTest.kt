package com.github.panpf.recycler.sticky.assemblyadapter.test

import android.R
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.recycler.sticky.assemblyadapter4.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AssemblyStickyItemDecorationExtensionsTest {

    private class StringItemFactory : ViewItemFactory<String>(String::class, R.layout.list_content)
    private class IntItemFactory : ViewItemFactory<Int>(Int::class, R.layout.list_content)
    private class BooleanItemFactory :
        ViewItemFactory<Boolean>(Boolean::class, R.layout.list_content)

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val adapter = AssemblyRecyclerAdapter<Any>(
            listOf(StringItemFactory(), IntItemFactory(), BooleanItemFactory()),
            listOf(5, "A", "B", 1, "C", true, "D", false)
        )

        (RecyclerView(context).apply {
            addAssemblyStickyItemDecorationWithPosition(1, 5)
        }.getItemDecorationAt(0) as AssemblyStickyItemDecoration).apply {
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
            addAssemblyStickyItemDecorationWithPosition(listOf(1, 5))
        }.getItemDecorationAt(0) as AssemblyStickyItemDecoration).apply {
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
            addAssemblyStickyItemDecorationWithItemType(1, 2)
        }.getItemDecorationAt(0) as AssemblyStickyItemDecoration).apply {
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
            addAssemblyStickyItemDecorationWithItemType(listOf(1, 2))
        }.getItemDecorationAt(0) as AssemblyStickyItemDecoration).apply {
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
            addAssemblyStickyItemDecoration { }
        }.getItemDecorationAt(0) as AssemblyStickyItemDecoration).apply {
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
            addAssemblyStickyItemDecoration {
                position(1, 5)
                itemType(1, 2)
            }
        }.getItemDecorationAt(0) as AssemblyStickyItemDecoration).apply {
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
            addAssemblyStickyItemDecoration {
                position(listOf(1, 5))
                itemType(listOf(1, 2))
            }
        }.getItemDecorationAt(0) as AssemblyStickyItemDecoration).apply {
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
            addAssemblyStickyItemDecorationWithItemFactory(
                StringItemFactory::class,
                BooleanItemFactory::class
            )
        }.getItemDecorationAt(0) as AssemblyStickyItemDecoration).apply {
            Assert.assertFalse(isStickyItemByPosition(adapter, 0))
            Assert.assertTrue(isStickyItemByPosition(adapter, 1))
            Assert.assertTrue(isStickyItemByPosition(adapter, 2))
            Assert.assertFalse(isStickyItemByPosition(adapter, 3))
            Assert.assertTrue(isStickyItemByPosition(adapter, 4))
            Assert.assertTrue(isStickyItemByPosition(adapter, 5))
            Assert.assertTrue(isStickyItemByPosition(adapter, 6))
            Assert.assertTrue(isStickyItemByPosition(adapter, 7))
        }

        (RecyclerView(context).apply {
            addAssemblyStickyItemDecorationWithItemFactory(
                listOf(StringItemFactory::class, BooleanItemFactory::class)
            )
        }.getItemDecorationAt(0) as AssemblyStickyItemDecoration).apply {
            Assert.assertFalse(isStickyItemByPosition(adapter, 0))
            Assert.assertTrue(isStickyItemByPosition(adapter, 1))
            Assert.assertTrue(isStickyItemByPosition(adapter, 2))
            Assert.assertFalse(isStickyItemByPosition(adapter, 3))
            Assert.assertTrue(isStickyItemByPosition(adapter, 4))
            Assert.assertTrue(isStickyItemByPosition(adapter, 5))
            Assert.assertTrue(isStickyItemByPosition(adapter, 6))
            Assert.assertTrue(isStickyItemByPosition(adapter, 7))
        }

        (RecyclerView(context).apply {
            addAssemblyStickyItemDecoration {
                position(3)
                itemType(0)
                itemFactory(BooleanItemFactory::class)
            }
        }.getItemDecorationAt(0) as AssemblyStickyItemDecoration).apply {
            Assert.assertFalse(isStickyItemByPosition(adapter, 0))
            Assert.assertTrue(isStickyItemByPosition(adapter, 1))
            Assert.assertTrue(isStickyItemByPosition(adapter, 2))
            Assert.assertTrue(isStickyItemByPosition(adapter, 3))
            Assert.assertTrue(isStickyItemByPosition(adapter, 4))
            Assert.assertTrue(isStickyItemByPosition(adapter, 5))
            Assert.assertTrue(isStickyItemByPosition(adapter, 6))
            Assert.assertTrue(isStickyItemByPosition(adapter, 7))
        }

        (RecyclerView(context).apply {
            addAssemblyStickyItemDecoration {
                position(listOf(3))
                itemType(listOf(0))
                itemFactory(listOf(BooleanItemFactory::class))
            }
        }.getItemDecorationAt(0) as AssemblyStickyItemDecoration).apply {
            Assert.assertFalse(isStickyItemByPosition(adapter, 0))
            Assert.assertTrue(isStickyItemByPosition(adapter, 1))
            Assert.assertTrue(isStickyItemByPosition(adapter, 2))
            Assert.assertTrue(isStickyItemByPosition(adapter, 3))
            Assert.assertTrue(isStickyItemByPosition(adapter, 4))
            Assert.assertTrue(isStickyItemByPosition(adapter, 5))
            Assert.assertTrue(isStickyItemByPosition(adapter, 6))
            Assert.assertTrue(isStickyItemByPosition(adapter, 7))
        }
    }
}