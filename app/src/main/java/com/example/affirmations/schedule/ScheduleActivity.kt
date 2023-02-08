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

package com.example.affirmations.schedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.affirmations.R
import com.example.affirmations.adapter.DaysPagerAdapter
import com.example.affirmations.adapter.TimeTableAdapter
import com.example.affirmations.data.TimeTableItem
import com.example.affirmations.data.daysOfWeek
import com.example.affirmations.databinding.ActivityScheduleBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val TAG = "MainActivity"

class ScheduleActivity : FragmentActivity() {

    private lateinit var adapter: DaysPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private lateinit var binding: ActivityScheduleBinding

    private val timeTableListViewModel by viewModels<TimeTableListViewModel> {
        TimeTableListViewModelFactory(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.topAppBar.setNavigationOnClickListener {v: View ->
            showAppBarMenu(v, R.menu.app_bar_menu)
        }

        //ViewPager
        adapter = DaysPagerAdapter(this)
        viewPager = findViewById(R.id.schedule_pager)
        viewPager.adapter = adapter

        tabLayout = binding.weekTabLayout

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            //TODO:??
            tab.text = daysOfWeek()[position]
        }.attach()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.app_bar_menu, menu)
    }

    private fun showAppBarMenu(view: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this@ScheduleActivity, view)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            TODO("menuItem: MenuItem ->")
        }

        popup.show()
    }
}