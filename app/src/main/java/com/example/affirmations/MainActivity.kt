/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.affirmations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.adapter.TimeTableAdapter
import com.example.affirmations.data.TimeTableItem
import com.example.affirmations.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val newTimeTAbleActivityRequestCode = 1
    private val timeTableListViewModel by viewModels<TimeTableListViewModel> {
        TimeTableListViewModelFactory(this)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.topAppBar.setNavigationOnClickListener {v: View ->
            showAppBarMenu(v, R.menu.app_bar_menu)
        }

        //Recycler view and data
        val timeTableAdapter = TimeTableAdapter(
            context = this@MainActivity
        ) { itemView -> adapterOnLongClick(itemView) }

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = timeTableAdapter

        timeTableListViewModel.timeTableLiveData.observe(this) {
            it?.let {
                timeTableAdapter.submitList(it as MutableList<TimeTableItem>)
            }
        }

        binding.weekTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                //TODO:Handle tabs select
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.app_bar_menu, menu)
    }

    private fun adapterOnLongClick(view: View) {
        showContextMenu(view, R.menu.list_item_menu)
    }

    //Methods for menus
    private fun showContextMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this@MainActivity, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            TODO("menuItem: MenuItem ->")
        }

        popup.show()
    }

    private fun showAppBarMenu(view: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this@MainActivity, view)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            TODO("menuItem: MenuItem ->")
        }

        popup.show()
    }
}