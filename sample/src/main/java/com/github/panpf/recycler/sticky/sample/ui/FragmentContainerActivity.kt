/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.panpf.recycler.sticky.sample.R
import com.github.panpf.recycler.sticky.sample.databinding.ActivityFragmentContainerBinding
import com.github.panpf.recycler.sticky.sample.vm.MenuViewModel

class FragmentContainerActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context, title: String, subTitle: String?, fragment: Fragment) =
            Intent(context, FragmentContainerActivity::class.java).apply {
                putExtra("title", title)
                putExtra("subTitle", subTitle)
                putExtra("fragmentClassName", fragment.javaClass.name)
                putExtra("fragmentArguments", fragment.arguments)
            }
    }

    private val viewModel by viewModels<MenuViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            ActivityFragmentContainerBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        val pageTitle = intent.getStringExtra("title") ?: getString(R.string.app_name)
        val pageSubTitle = intent.getStringExtra("subTitle")
        val fragmentClassName = intent.getStringExtra("fragmentClassName")
            ?: throw IllegalArgumentException("Not found param 'fragmentClassName'")
        val fragmentArguments = intent.getBundleExtra("fragmentArguments")

        setSupportActionBar(binding.fragmentContainerToolbar.apply {
            title = pageTitle
            subtitle = pageSubTitle
        })

        val fragment = (Class.forName(fragmentClassName).newInstance() as Fragment).apply {
            arguments = fragmentArguments
        }
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerContainer.id, fragment)
            .commit()

        viewModel.menuInfoListData.observe(this) {
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        viewModel.menuInfoListData.value?.forEach { menuInfo ->
            menu?.add(menuInfo.group, menuInfo.id, menuInfo.order, menuInfo.title)?.apply {
                setShowAsAction(menuInfo.showAsAction)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuInfo = viewModel.menuInfoListData.value?.find {
            it.id == item.itemId
        }
        if (menuInfo != null) {
            viewModel.menuClickEvent.postValue(menuInfo)
        }
        return super.onOptionsItemSelected(item)
    }
}