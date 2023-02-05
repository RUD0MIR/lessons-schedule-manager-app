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
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.adapter.TimeTableAdapter
import com.example.affirmations.data.DataSource
import com.example.affirmations.data.TimeTableItem
import com.example.affirmations.databinding.ActivityMainBinding

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
            showMenu(v, R.menu.menu)
        }

        //Recycler view and data
        val timeTableAdapter = TimeTableAdapter{ timeTableItem -> adapterOnClick(timeTableItem)  }

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = timeTableAdapter

        timeTableListViewModel.timeTableLiveData.observe(this) {
            it?.let {
                timeTableAdapter.submitList(it as MutableList<TimeTableItem>)
            }
        }
    }

    private fun adapterOnClick(timeTableItem: TimeTableItem) {
    }

    //App bar menu
    private fun showMenu(view: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this@MainActivity, view)
        popup.menuInflater.inflate(menuRes, popup.menu)

//        popup.setOnMenuItemClickListener {
//        }

        popup.setOnDismissListener {
            popup.dismiss()
        }
        // Show the popup menu.
        popup.show()
    }

    //Tabs
//    tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//
//        override fun onTabSelected(tab: TabLayout.Tab?) {
//             Handle tab select
//        }
//
//        override fun onTabReselected(tab: TabLayout.Tab?) {
//             Handle tab reselect
//        }
//
//        override fun onTabUnselected(tab: TabLayout.Tab?) {
//             Handle tab unselect
//        }
//    })

    //
}