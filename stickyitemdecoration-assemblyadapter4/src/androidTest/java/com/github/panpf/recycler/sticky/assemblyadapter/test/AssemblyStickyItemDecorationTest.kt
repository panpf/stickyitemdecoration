package com.github.panpf.recycler.sticky.assemblyadapter.test

import android.R
import android.widget.FrameLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.recycler.sticky.assemblyadapter4.AssemblyStickyItemDecoration
import com.github.panpf.recycler.sticky.internal.ContainerStickyItemPainter
import com.github.panpf.recycler.sticky.internal.DrawStickyItemPainter
import com.github.panpf.recycler.sticky.internal.StickyItemPainter
import com.github.panpf.tools4j.reflect.ktx.getFieldValue
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AssemblyStickyItemDecorationTest {

    private class StringItemFactory : ViewItemFactory<String>(String::class, R.layout.list_content)
    private class IntItemFactory : ViewItemFactory<Int>(Int::class, R.layout.list_content)
    private class BooleanItemFactory : ViewItemFactory<Boolean>(Boolean::class, R.layout.list_content)

    @Test
    fun testBuilder() {
        val adapter = AssemblyRecyclerAdapter<Any>(
            listOf(StringItemFactory(), IntItemFactory(), BooleanItemFactory()),
            listOf(5, "A", "B", 1, "C", true, "D", false)
        )

        AssemblyStickyItemDecoration.Builder()
            .build()
            .apply {
                Assert.assertFalse(isStickyItemByPosition(adapter, 0))
                Assert.assertFalse(isStickyItemByPosition(adapter, 1))
                Assert.assertFalse(isStickyItemByPosition(adapter, 2))
                Assert.assertFalse(isStickyItemByPosition(adapter, 3))
                Assert.assertFalse(isStickyItemByPosition(adapter, 4))
                Assert.assertFalse(isStickyItemByPosition(adapter, 5))
                Assert.assertFalse(isStickyItemByPosition(adapter, 6))
                Assert.assertFalse(isStickyItemByPosition(adapter, 7))
            }

        AssemblyStickyItemDecoration.Builder()
            .position(1, 5)
            .build()
            .apply {
                Assert.assertFalse(isStickyItemByPosition(adapter, 0))
                Assert.assertTrue(isStickyItemByPosition(adapter, 1))
                Assert.assertFalse(isStickyItemByPosition(adapter, 2))
                Assert.assertFalse(isStickyItemByPosition(adapter, 3))
                Assert.assertFalse(isStickyItemByPosition(adapter, 4))
                Assert.assertTrue(isStickyItemByPosition(adapter, 5))
                Assert.assertFalse(isStickyItemByPosition(adapter, 6))
                Assert.assertFalse(isStickyItemByPosition(adapter, 7))
            }

        AssemblyStickyItemDecoration.Builder()
            .position(listOf(1, 5))
            .build()
            .apply {
                Assert.assertFalse(isStickyItemByPosition(adapter, 0))
                Assert.assertTrue(isStickyItemByPosition(adapter, 1))
                Assert.assertFalse(isStickyItemByPosition(adapter, 2))
                Assert.assertFalse(isStickyItemByPosition(adapter, 3))
                Assert.assertFalse(isStickyItemByPosition(adapter, 4))
                Assert.assertTrue(isStickyItemByPosition(adapter, 5))
                Assert.assertFalse(isStickyItemByPosition(adapter, 6))
                Assert.assertFalse(isStickyItemByPosition(adapter, 7))
            }

        AssemblyStickyItemDecoration.Builder()
            .itemType(1, 2)
            .build()
            .apply {
                Assert.assertTrue(isStickyItemByPosition(adapter, 0))
                Assert.assertFalse(isStickyItemByPosition(adapter, 1))
                Assert.assertFalse(isStickyItemByPosition(adapter, 2))
                Assert.assertTrue(isStickyItemByPosition(adapter, 3))
                Assert.assertFalse(isStickyItemByPosition(adapter, 4))
                Assert.assertTrue(isStickyItemByPosition(adapter, 5))
                Assert.assertFalse(isStickyItemByPosition(adapter, 6))
                Assert.assertTrue(isStickyItemByPosition(adapter, 7))
            }

        AssemblyStickyItemDecoration.Builder()
            .itemType(listOf(1, 2))
            .build()
            .apply {
                Assert.assertTrue(isStickyItemByPosition(adapter, 0))
                Assert.assertFalse(isStickyItemByPosition(adapter, 1))
                Assert.assertFalse(isStickyItemByPosition(adapter, 2))
                Assert.assertTrue(isStickyItemByPosition(adapter, 3))
                Assert.assertFalse(isStickyItemByPosition(adapter, 4))
                Assert.assertTrue(isStickyItemByPosition(adapter, 5))
                Assert.assertFalse(isStickyItemByPosition(adapter, 6))
                Assert.assertTrue(isStickyItemByPosition(adapter, 7))
            }

        AssemblyStickyItemDecoration.Builder()
            .position(1, 5)
            .itemType(1, 2)
            .build()
            .apply {
                Assert.assertTrue(isStickyItemByPosition(adapter, 0))
                Assert.assertTrue(isStickyItemByPosition(adapter, 1))
                Assert.assertFalse(isStickyItemByPosition(adapter, 2))
                Assert.assertTrue(isStickyItemByPosition(adapter, 3))
                Assert.assertFalse(isStickyItemByPosition(adapter, 4))
                Assert.assertTrue(isStickyItemByPosition(adapter, 5))
                Assert.assertFalse(isStickyItemByPosition(adapter, 6))
                Assert.assertTrue(isStickyItemByPosition(adapter, 7))
            }

        AssemblyStickyItemDecoration.Builder()
            .position(listOf(1, 5))
            .itemType(listOf(1, 2))
            .build()
            .apply {
                Assert.assertTrue(isStickyItemByPosition(adapter, 0))
                Assert.assertTrue(isStickyItemByPosition(adapter, 1))
                Assert.assertFalse(isStickyItemByPosition(adapter, 2))
                Assert.assertTrue(isStickyItemByPosition(adapter, 3))
                Assert.assertFalse(isStickyItemByPosition(adapter, 4))
                Assert.assertTrue(isStickyItemByPosition(adapter, 5))
                Assert.assertFalse(isStickyItemByPosition(adapter, 6))
                Assert.assertTrue(isStickyItemByPosition(adapter, 7))
            }

        AssemblyStickyItemDecoration.Builder()
            .itemFactory(StringItemFactory::class, BooleanItemFactory::class)
            .build()
            .apply {
                Assert.assertFalse(isStickyItemByPosition(adapter, 0))
                Assert.assertTrue(isStickyItemByPosition(adapter, 1))
                Assert.assertTrue(isStickyItemByPosition(adapter, 2))
                Assert.assertFalse(isStickyItemByPosition(adapter, 3))
                Assert.assertTrue(isStickyItemByPosition(adapter, 4))
                Assert.assertTrue(isStickyItemByPosition(adapter, 5))
                Assert.assertTrue(isStickyItemByPosition(adapter, 6))
                Assert.assertTrue(isStickyItemByPosition(adapter, 7))
            }

        AssemblyStickyItemDecoration.Builder()
            .itemFactory(listOf(StringItemFactory::class, BooleanItemFactory::class))
            .build()
            .apply {
                Assert.assertFalse(isStickyItemByPosition(adapter, 0))
                Assert.assertTrue(isStickyItemByPosition(adapter, 1))
                Assert.assertTrue(isStickyItemByPosition(adapter, 2))
                Assert.assertFalse(isStickyItemByPosition(adapter, 3))
                Assert.assertTrue(isStickyItemByPosition(adapter, 4))
                Assert.assertTrue(isStickyItemByPosition(adapter, 5))
                Assert.assertTrue(isStickyItemByPosition(adapter, 6))
                Assert.assertTrue(isStickyItemByPosition(adapter, 7))
            }

        AssemblyStickyItemDecoration.Builder()
            .position(3)
            .itemType(0)
            .itemFactory(BooleanItemFactory::class)
            .build()
            .apply {
                Assert.assertFalse(isStickyItemByPosition(adapter, 0))
                Assert.assertTrue(isStickyItemByPosition(adapter, 1))
                Assert.assertTrue(isStickyItemByPosition(adapter, 2))
                Assert.assertTrue(isStickyItemByPosition(adapter, 3))
                Assert.assertTrue(isStickyItemByPosition(adapter, 4))
                Assert.assertTrue(isStickyItemByPosition(adapter, 5))
                Assert.assertTrue(isStickyItemByPosition(adapter, 6))
                Assert.assertTrue(isStickyItemByPosition(adapter, 7))
            }

        AssemblyStickyItemDecoration.Builder()
            .position(listOf(3))
            .itemType(listOf(0))
            .itemFactory(listOf(BooleanItemFactory::class))
            .build()
            .apply {
                Assert.assertFalse(isStickyItemByPosition(adapter, 0))
                Assert.assertTrue(isStickyItemByPosition(adapter, 1))
                Assert.assertTrue(isStickyItemByPosition(adapter, 2))
                Assert.assertTrue(isStickyItemByPosition(adapter, 3))
                Assert.assertTrue(isStickyItemByPosition(adapter, 4))
                Assert.assertTrue(isStickyItemByPosition(adapter, 5))
                Assert.assertTrue(isStickyItemByPosition(adapter, 6))
                Assert.assertTrue(isStickyItemByPosition(adapter, 7))
            }

        AssemblyStickyItemDecoration.Builder()
            .build()
            .apply {
                Assert.assertTrue(this.getFieldValue<StickyItemPainter>("stickyItemPainter") is DrawStickyItemPainter)
            }

        val context = InstrumentationRegistry.getInstrumentation().context
        val frameLayout = FrameLayout(context)
        AssemblyStickyItemDecoration.Builder()
            .showInContainer(frameLayout)
            .build()
            .apply {
                Assert.assertTrue(this.getFieldValue<StickyItemPainter>("stickyItemPainter") is ContainerStickyItemPainter)
            }
    }
}